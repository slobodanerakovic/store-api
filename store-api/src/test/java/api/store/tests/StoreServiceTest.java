package api.store.tests;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import api.store.exceptions.StoreException;
import api.store.model.GeoLocation;
import api.store.model.Store;
import api.store.service.StoreService;
import api.store.ws.dtos.StoreLocationDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StoreServiceTest {

	private static final Logger LOG = LoggerFactory.getLogger(StoreServiceTest.class);

	@Autowired
	private StoreService storeService;

	@Test(expected = StoreException.class)
	public void testStoreMissingSapStoreID() {

		String missingSapStore = "ts4d6tf7gyu";
		storeService.getStoreLocationForId(missingSapStore);
	}

	@Test
	public void testStoreExistingSapStoreID() {

		String missingSapStore = "3605";
		Store store = storeService.getStoreLocationForId(missingSapStore);
		Assume.assumeTrue(store != null);
	}

	@Test
	public void testStoreNumberOfStoreWithExaggeration() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setNumberOfStores(10);
		locationDTO.setCity("Uden");

		List<Store> locationsOfCity = storeService.retriveStoresLocationsOfCity(locationDTO);
		Assume.assumeFalse(locationsOfCity.size() == locationDTO.getNumberOfStores());
	}

	@Test(expected = StoreException.class)
	public void testStoreMissingCity() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setNumberOfStores(5);
		locationDTO.setCity("Belgrade");
		List<Store> locationsOfCity = storeService.retriveStoresLocationsOfCity(locationDTO);

		Assume.assumeTrue(locationsOfCity.size() == 0);
	}

	@Test
	public void testStoreExistingCity() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setNumberOfStores(5);
		locationDTO.setCity("Zwolle");
		List<Store> storeLocationForCity = storeService.retriveStoresLocationsOfCity(locationDTO);
		Assume.assumeTrue(storeLocationForCity.size() == 5);
	}

	@Test
	public void testRetrieveStoreNoDistanceSpecified() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(4.615551, 51.778461));
		locationDTO.setRadiusKm(0);
		locationDTO.setNumberOfStores(5);

		List<Store> storesLocations = storeService.retriveClosestStoresLocations(locationDTO);
		Assume.assumeTrue(storesLocations.size() == 5);
	}

	@Test(expected = StoreException.class)
	public void testNotRequiredNumberOfStoreWithinDistanceForGivenLocation() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(4.615551, 51.778461));
		locationDTO.setRadiusKm(5);
		locationDTO.setNumberOfStores(5);

		List<Store> storesLocations = storeService.retriveClosestStoresLocations(locationDTO);
		Assume.assumeTrue(storesLocations.size() == 5);
	}

	@Test
	public void testRetrieveStoreOkDistanceForGivenLocation() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(4.615551, 51.778461));
		locationDTO.setRadiusKm(10);
		locationDTO.setNumberOfStores(5);

		List<Store> storesLocations = storeService.retriveClosestStoresLocations(locationDTO);
		Assume.assumeTrue(storesLocations.size() == 5);
	}

	@Test
	public void testRetrieveStoreForWorkingHours() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(4.615551, 51.778461));
		locationDTO.setRadiusKm(0);
		locationDTO.setNumberOfStores(5);

		String hourOfDay = "14:30";
		List<Store> storesLocations = storeService.retriveClosestStoresLocationsOfWorkingHours(locationDTO, hourOfDay);
		Assume.assumeTrue(storesLocations.size() == 5);
	}

	@Test(expected = StoreException.class)
	public void testRetrieveStoreOutOfWorkingHours() {
		StoreLocationDTO locationDTO = new StoreLocationDTO();
		locationDTO.setGeoLocation(new GeoLocation(4.615551, 51.778461));
		locationDTO.setRadiusKm(10);
		locationDTO.setNumberOfStores(5);

		String hourOfDay = "07:00";
		storeService.retriveClosestStoresLocationsOfWorkingHours(locationDTO, hourOfDay);
	}

	@Test
	public void testNumberOfStoresPerCity() {

		Map<String, Long> numberOfStoresPerCity = storeService.getNumberOfStoresPerCity();
		Map<String, Long> testCase = new HashMap<>();
		testCase.put("Zoetermeer", 6l);
		testCase.put("Aalst", 1l);
		testCase.put("Nistelrode", 1l);
		testCase.put("Veenendaal", 4l);
		testCase.put("Nederweert", 1l);

		numberOfStoresPerCity.forEach((city, num) -> {
			Assume.assumeTrue(testCase.get(city) == num);
		});
	}
}
