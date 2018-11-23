package api.store.ws.dtos;

import api.store.model.GeoLocation;

public class StoreLocationDTO {
	private GeoLocation geoLocation;
	private Integer radiusKm;
	private Integer numberOfStores;
	private String city;

	public GeoLocation getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}

	public Integer getRadiusKm() {
		return radiusKm;
	}

	public void setRadiusKm(Integer radiusKm) {
		this.radiusKm = radiusKm;
	}

	public Integer getNumberOfStores() {
		return numberOfStores;
	}

	public void setNumberOfStores(Integer numberOfStores) {
		this.numberOfStores = numberOfStores;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "StoreLocationDTO [geoLocation=" + geoLocation + ", radiusKm=" + radiusKm + ", numberOfStores="
				+ numberOfStores + ", city=" + city + "]";
	}

}
