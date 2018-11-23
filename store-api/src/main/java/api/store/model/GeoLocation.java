package api.store.model;

public class GeoLocation {

	private double longitude;
	private double latitude;

	public GeoLocation() {
	}

	public GeoLocation(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "GeoLocation [longitude=" + longitude + ", latitude=" + latitude + "]";
	}

}
