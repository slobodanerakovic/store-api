package api.store.model;

public class Address {
	private String city;
	private String postalCode;
	private String street;
	// this field is String, because number can be range of numbers, which
	// semantically better to be expressed just as they are in the json file, as a
	// String
	private String streetNumber;
	private String secondStreet;
	private String addressName;
	private GeoLocation geoLocation;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getSecondStreet() {
		return secondStreet;
	}

	public void setSecondStreet(String secondStreet) {
		this.secondStreet = secondStreet;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	@Override
	public String toString() {
		return "Address [city=" + city + ", postalCode=" + postalCode + ", street=" + street + ", streetNumber="
				+ streetNumber + ", secondStreet=" + secondStreet + ", addressName=" + addressName + ", geoLocation="
				+ geoLocation + "]";
	}

}
