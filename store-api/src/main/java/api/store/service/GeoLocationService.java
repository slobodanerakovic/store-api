package api.store.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import api.store.model.GeoLocation;

@Service
public class GeoLocationService {
	private static final Logger LOG = LoggerFactory.getLogger(GeoLocationService.class);

	private final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
	int counter = 0;

	/**
	 * Finding given number of closest geolocation of stores, regarding the given
	 * targetLocation, within certain radius if any given (optional params)
	 * 
	 * @param GeoLocation targetLocation
	 * @param             List<GeoLocation> geolocations
	 * @param             int numberOfStores
	 * @param             int radiusKm
	 * @return
	 */
	public List<GeoLocation> closestLocations(GeoLocation targetLocation, List<GeoLocation> geolocations,
			int numberOfStores, int radiusKm) {
		List<GeoLocation> targetlist = new ArrayList<>();

		while (targetlist.size() < numberOfStores && geolocations.size() > 0) {
			GeoLocation geoLocation = geolocations.stream().reduce((previousLocation, currentLocation) -> {
				double prevDistance = locationDistance(targetLocation, previousLocation);
				double currDistance = locationDistance(targetLocation, currentLocation);

				return (prevDistance < currDistance) ? previousLocation : currentLocation;
			}).orElse(null);

			if (geoLocation != null) {
				/**
				 * If not specific distance set, just return the closest one. If we specify
				 * targetDistanceKm value, it might potentially return those stores, which are
				 * members with radius circle, but mandatory the closest one, because of short
				 * circuit stream iteration, where, once it found closest numberOfStores, it
				 * will start searching further - CHECK THIS
				 */
				if (radiusKm != 0) {
					int currentDistanceInKm = calculateDistanceInKilometer(targetLocation.getLatitude(),
							targetLocation.getLongitude(), geoLocation.getLatitude(), geoLocation.getLongitude());

					if (currentDistanceInKm <= radiusKm) {
						targetlist.add(geoLocation);
					}
				} else {
					targetlist.add(geoLocation);
				}
				geolocations.remove(geoLocation);
			}
		}
		LOG.info("Found {} stores, of required {}, within radius", targetlist.size(), numberOfStores, radiusKm);
		return targetlist;
	}

	/**
	 * Calculation of the distance between given locations, using vector math where
	 * you get the hypotenuse (with the vectorDistance method)
	 * 
	 * @param GeoLocation location1
	 * @param GeoLocation location2
	 * @return
	 */
	private double locationDistance(GeoLocation location1, GeoLocation location2) {
		double dx = location1.getLatitude() - location2.getLatitude();
		double dy = location1.getLongitude() - location2.getLongitude();

		return vectorDistance(dx, dy);
	}

	private double vectorDistance(double dx, double dy) {
		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Appliance of Haversine formula, for determination of the great-circle
	 * distance between two points on a sphere given their longitudes and latitudes.
	 * 
	 * @param double givenLat
	 * @param double givenLng
	 * @param double currentLat
	 * @param double currentLng
	 * @return
	 */
	private int calculateDistanceInKilometer(double givenLat, double givenLng, double currentLat, double currentLng) {

		double latDistance = Math.toRadians(givenLat - currentLat);
		double lngDistance = Math.toRadians(givenLng - currentLng);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(givenLat))
				* Math.cos(Math.toRadians(currentLat)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
	}

}
