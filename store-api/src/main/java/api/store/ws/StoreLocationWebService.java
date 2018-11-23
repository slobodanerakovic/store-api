package api.store.ws;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import api.store.exceptions.StoreException;
import api.store.model.GeoLocation;
import api.store.model.Store;
import api.store.service.StoreService;
import api.store.util.Util;
import api.store.ws.dtos.StoreLocationDTO;
import api.store.ws.dtos.StoreNotFoundDTO;

@RestController
@RequestMapping("/api")
public class StoreLocationWebService {
	/**
	 * Store API web service class, consists of the 6 features, which can be
	 * provided by service. Some of them are hybrid (since the requirement was just
	 * to return closest 5 stores), like one related to closest store location,
	 * include also applied business logic for additional parameters like: radiusKm,
	 * numberOfStores, which are defaulted by some values (number of stores with 5,
	 * as required). <br />
	 * I took liberty to extend business logic of requirement, and implement them
	 * within an application. Comments scattered over code, describe why something
	 * is done in such a way.<br />
	 * arguments marked as @PathVariable are mandatory, where @RequestParam
	 * represent optional query string to resource
	 * 
	 */

	@Autowired
	private StoreService storeService;

	@GetMapping(value = "/coordinatesStoresLocation/{latitude}/{longitude}", produces = { "application/json" })
	public List<Store> getStoreLocation(@PathVariable double latitude, @PathVariable double longitude,
			@RequestParam(value = "radiusKm", required = false, defaultValue = "0") Integer radiusKm,
			@RequestParam(value = "numberOfStores", required = false, defaultValue = "5") Integer numberOfStores) {

		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(longitude, latitude));
		locationDTO.setRadiusKm(radiusKm);
		locationDTO.setNumberOfStores(numberOfStores);

		return storeService.retriveClosestStoresLocations(locationDTO);
	}

	@GetMapping(value = "closestsStoreLocation/worktime/{latitude}/{longitude}/{hourOfDay}", produces = {
			"application/json" })
	public List<Store> getOpenedStoreLocationNearBy(@PathVariable double latitude, @PathVariable double longitude,
			@PathVariable(required = true) String hourOfDay,
			@RequestParam(value = "radiusKm", required = false, defaultValue = "0") Integer radiusKm,
			@RequestParam(value = "numberOfStores", required = false, defaultValue = "5") Integer numberOfStores) {

		Util.validateHourFormat(hourOfDay);

		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(longitude, latitude));
		locationDTO.setRadiusKm(radiusKm);
		locationDTO.setNumberOfStores(numberOfStores);

		return storeService.retriveClosestStoresLocationsOfWorkingHours(locationDTO, hourOfDay);
	}

	@GetMapping(value = "/cityStoresLocation/{city}", produces = { "application/json" })
	public List<Store> getStoreLocationOfCity(@PathVariable String city,
			@RequestParam(value = "numberOfStores", required = false, defaultValue = "5") Integer numberOfStores) {

		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setNumberOfStores(numberOfStores);
		locationDTO.setCity(city);

		return storeService.retriveStoresLocationsOfCity(locationDTO);
	}

	@GetMapping(value = "/storeid/{sapStoreID}", produces = { "application/json" })
	public Store getStoreLocationOfId(@PathVariable String sapStoreID) {

		return storeService.getStoreLocationForId(sapStoreID);
	}

	@GetMapping(value = "/stats/storespercity", produces = { "application/json" })
	public Map<String, Long> numberOfStoresPerCity() {

		return storeService.getNumberOfStoresPerCity();
	}

	/**
	 * Generic StoreException which thrown from any part of the code, will be
	 * handled here, returning StoreNotFoundDTO object as a response, with details
	 * about what goes wrong
	 */
	@ExceptionHandler(StoreException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public StoreNotFoundDTO customerNotFound(StoreException e) {

		String template = "store with supplied data %s has not found requested stores. Please, reconfigure your search criteria";
		StoreLocationDTO dto = e.getLocationDTO();
		if (dto != null) {
			return new StoreNotFoundDTO(String.format(template, dto.toString()));
		} else {
			return new StoreNotFoundDTO(e.getMessage());
		}
	}
}
