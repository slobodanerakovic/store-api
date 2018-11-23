package api.store.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import api.store.exceptions.StoreException;
import api.store.model.Address;
import api.store.model.GeoLocation;
import api.store.model.Store;
import api.store.model.StoreInformation;

public class Util {

	/**
	 * Store util methods, used for initial reading and transformation to Store
	 * model object, just as another util methods
	 */
	public static List<Store> convertToStore(String fileContent) {

		List<Store> storeList = new ArrayList<>();
		try {
			JSONObject obj = new JSONObject(fileContent);
			JSONArray stores = (JSONArray) obj.get("stores");

			for (int i = 0; i < stores.length(); i++) {
				JSONObject json = stores.getJSONObject(i);
				Store store = new Store();

				Address address = new Address();
				address.setAddressName(json.getString("addressName"));
				address.setCity(json.getString("city"));
				address.setStreet(json.getString("street"));
				address.setStreetNumber(json.getString("street2"));
				address.setSecondStreet(json.optString("street3"));
				address.setPostalCode(json.getString("postalCode"));

				GeoLocation location = new GeoLocation();
				location.setLongitude(json.getDouble("longitude"));
				location.setLatitude(json.getDouble("latitude"));
				address.setGeoLocation(location);

				StoreInformation storeInfo = new StoreInformation();

				storeInfo.setCollectionPoint(json.optString("collectionPoint"));
				storeInfo.setLocationType(json.optString("locationType"));
				storeInfo.setShowWarningMessage(json.optString("showWarningMessage"));
				storeInfo.setTodayClose(json.getString("todayClose"));
				storeInfo.setTodayOpen(json.getString("todayOpen"));

				store.setAddress(address);
				store.setStoreInformation(storeInfo);
				store.setUuid(json.getString("uuid"));
				store.setComplexNumber(json.getInt("complexNumber"));
				store.setSapStoreID(json.getString("sapStoreID"));

				storeList.add(store);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return storeList;
	}

	/**
	 * Must read packed file this way, since Spring expect it to be available on the
	 * file system, i.e. it can't be nested inside a jar file.<br />
	 * ClassPathResource.getInputStream() instead. That'll allow you to read the
	 * resource's content regardless of where it's located.
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String readFile() throws IOException {
		ClassPathResource cpr = new ClassPathResource("stores.json");
		String data = "";
		try {
			byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
			data = new String(bdata, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();

		}
		return data;
	}

	public static boolean filterWorkingHours(StoreInformation storeInformation, String hourOfDay) {

		String todayOpen = storeInformation.getTodayOpen();
		if (todayOpen.equals(Constants.CLOSED))
			return false;

		String todayClose = storeInformation.getTodayClose();

		/**
		 * minusMinutes and plusMinutes are included to cover border cases like 08:00
		 * and 21:00/22:00
		 */
		LocalTime openTime = LocalTime.parse(todayOpen).minusMinutes(1);
		LocalTime closedTime = LocalTime.parse(todayClose).plusMinutes(1);
		LocalTime targetTime = LocalTime.parse(hourOfDay);

		return (targetTime.isAfter(openTime) && targetTime.isBefore(closedTime));
	}

	public static void validateHourFormat(String hourOfDay) {
		if (!Pattern.matches("[0-9]{2}:[0-9]{2}", hourOfDay)) {
			throw new StoreException("Bad hour format ! Hour of day, must be in format: hh:mm");
		}
	}
}
