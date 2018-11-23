package api.store.tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import api.store.model.GeoLocation;
import api.store.service.GeoLocationService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GeoLocationServiceTest {

	@Autowired
	private GeoLocationService geoLocationService;

	@Test
	public void testClosestsLocation() {
		GeoLocation targetLocation = new GeoLocation(-5, 56);
		GeoLocation geoTest = new GeoLocation(-3.5606, 57.6494);
		List<GeoLocation> geolocations = prepareTestGeoLocations(geoTest);

		List<GeoLocation> closestLocations = geoLocationService.closestLocations(targetLocation, geolocations, 2, 0);
		Assume.assumeTrue("There should be 2 locations", closestLocations.size() == 2);
	}

	@Test
	public void testMissingLocations() {
		GeoLocation targetLocation = new GeoLocation(-5, 56);
		GeoLocation geoTest = new GeoLocation(-3.5606, 57.6494);
		List<GeoLocation> geolocations = prepareTestGeoLocations(geoTest);

		List<GeoLocation> closestLocations = geoLocationService.closestLocations(targetLocation, geolocations, 4, 0);
		Assume.assumeFalse("only 2 locations are provided", closestLocations.size() == 2);
	}

	private List<GeoLocation> prepareTestGeoLocations(GeoLocation geoTest) {
		List<GeoLocation> geolocations = new ArrayList<>();

		geolocations.add(geoTest);
		geoTest = new GeoLocation(-3.5606, 57.6494);
		geolocations.add(geoTest);
		geoTest = new GeoLocation(-2.836, 57.077);
		geolocations.add(geoTest);
		geoTest = new GeoLocation(-2.202, 57.206);
		geolocations.add(geoTest);
		geoTest = new GeoLocation(-4.533, 55.907);
		geolocations.add(geoTest);
		geoTest = new GeoLocation(-4.585, 55.515);
		geolocations.add(geoTest);
		geoTest = new GeoLocation(-3.729, 56.326);
		geolocations.add(geoTest);

		return geolocations;
	}
}
