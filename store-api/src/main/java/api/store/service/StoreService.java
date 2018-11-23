package api.store.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.store.exceptions.StoreException;
import api.store.model.Address;
import api.store.model.GeoLocation;
import api.store.model.Store;
import api.store.repository.StoreRepository;
import api.store.util.Util;
import api.store.ws.dtos.StoreLocationDTO;

@Service
public class StoreService {
	private static final Logger LOG = LoggerFactory.getLogger(StoreService.class);

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private GeoLocationService geoLocationService;

	/**
	 * Get only those stores, which requested hourOfDay is between todayOpen and
	 * todayClosed hours
	 * 
	 * @param StoreLocationDTO locationDTO
	 * @return
	 */
	public List<Store> retriveClosestStoresLocations(StoreLocationDTO locationDTO) {
		List<Store> stores = storeRepository.getStores();
		LOG.info("retriveClosestStoresLocations of store size={}", stores.size());

		List<GeoLocation> geoLocations = stores.stream().map(js -> js.getAddress().getGeoLocation())
				.collect(Collectors.toList());

		List<GeoLocation> closestLocations = geoLocationService.closestLocations(locationDTO.getGeoLocation(),
				geoLocations, locationDTO.getNumberOfStores(), locationDTO.getRadiusKm());

		List<Store> targetStores = stores.stream()
				.filter(js -> closestLocations.contains(js.getAddress().getGeoLocation())).collect(Collectors.toList());

		LOG.info("Actual found stores size={}", stores.size());
		/**
		 * It is not documented, what should be in case of lower number but 5 stores. so
		 * here I just implement business logic, that 5 is mandatory. Changing default
		 * value of this variable, can adapt app to throw no error even if none of the
		 * store is found (however this up to business requirement)
		 */
		if (targetStores.size() < locationDTO.getNumberOfStores())
			throw new StoreException(locationDTO);

		return targetStores;
	}

	/**
	 * Retrieve store based on the sapStoreID parameter
	 * 
	 * @param String sapStoreID
	 * @return
	 */
	public Store getStoreLocationForId(String sapStoreID) {
		LOG.info("getStoreLocationForId of sapStoreID={}", sapStoreID);

		Optional<Store> store = storeRepository.getStores().stream().filter(js -> js.getSapStoreID().equals(sapStoreID))
				.findFirst();

		if (!store.isPresent())
			throw new StoreException("Not found Store for sapStoreID: " + sapStoreID);

		return store.get();
	}

	/**
	 * Retrieve stores based on the city parameter
	 * 
	 * @param StoreLocationDTO locationDTO
	 * @return
	 */
	public List<Store> retriveStoresLocationsOfCity(StoreLocationDTO locationDTO) {
		LOG.info("retriveStoresLocationsOfCity of city={}", locationDTO.getCity());

		List<Store> targetStores = storeRepository.getStores().stream()
				.filter(js -> js.getAddress().getCity().toLowerCase().equals(locationDTO.getCity().toLowerCase()))
				.limit(locationDTO.getNumberOfStores()).collect(Collectors.toList());

		if (targetStores.size() == 0)
			throw new StoreException("There is not Stores in City: " + locationDTO.getCity());
		return targetStores;
	}

	/**
	 * Get closest stores, which requested hourOfDay is between todayOpen and
	 * todayClosed hours, within certain distance
	 * 
	 * @param StoreLocationDTO locationDTO
	 * @param String           hourOfDay
	 * @return
	 */
	public List<Store> retriveClosestStoresLocationsOfWorkingHours(StoreLocationDTO locationDTO, String hourOfDay) {
		List<Store> stores = storeRepository.getStores();
		LOG.info("retriveClosestStoresLocationsOfWorkingHours of store size={}, for hour={}", stores.size(), hourOfDay);

		List<GeoLocation> geoLocations = stores.stream()
				.filter(js -> Util.filterWorkingHours(js.getStoreInformation(), hourOfDay))
				.map(js -> js.getAddress().getGeoLocation()).collect(Collectors.toList());

		List<GeoLocation> closestLocations = geoLocationService.closestLocations(locationDTO.getGeoLocation(),
				geoLocations, locationDTO.getNumberOfStores(), locationDTO.getRadiusKm());

		List<Store> targetStores = stores.stream()
				.filter(js -> closestLocations.contains(js.getAddress().getGeoLocation())).collect(Collectors.toList());

		if (targetStores.size() < locationDTO.getNumberOfStores())
			throw new StoreException(locationDTO);

		return targetStores;
	}

	public Map<String, Long> getNumberOfStoresPerCity() {
		List<Store> stores = storeRepository.getStores();

		Map<String, Long> storesPerCity = stores.stream().map(js -> js.getAddress())
				.collect(Collectors.groupingBy(Address::getCity, Collectors.counting()));

		LinkedHashMap<String, Long> storesPerCityOrdered = storesPerCity.entrySet().stream()
				.sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		return storesPerCityOrdered;
	}
}
