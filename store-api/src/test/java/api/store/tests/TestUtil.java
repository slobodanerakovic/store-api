package api.store.tests;

import static org.junit.Assume.assumeTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api.store.model.Address;
import api.store.model.GeoLocation;
import api.store.model.Store;
import api.store.model.StoreInformation;
import api.store.util.Util;

public class TestUtil {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtil.class);

	@Test
	public void testBorderCase() {
		StoreInformation st = new StoreInformation();
		st.setTodayOpen("08:00");
		st.setTodayClose("21:00");

		assumeTrue(Util.filterWorkingHours(st, "08:00"));
	}

	@Test
	public void testJsonRead() {
		List<String> fileContent = readFile();
		List<Store> stores = convertToStore(fileContent);

		stores.stream().filter(js -> js.getAddress().getCity().equals("Apeldoorn")).forEach(System.out::println);
		LOG.info("Found {} stores within document", stores.size());
	}

	private List<Store> convertToStore(List<String> fileContent) {
		List<Store> storesList = new ArrayList<>();
		try {
			JSONObject obj = new JSONObject(fileContent.get(0));
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

				storesList.add(store);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return storesList;
	}

	private List<String> readFile() {

		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("stores.json").getFile());
		List<String> fileLines = new ArrayList<>();

		try {
			fileLines = Files.readAllLines(Paths.get(file.toURI()), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileLines;
	}

}
