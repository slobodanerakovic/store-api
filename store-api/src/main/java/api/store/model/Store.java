package api.store.model;

public class Store {

	private String uuid;
	private String sapStoreID;
	private int complexNumber;
	private Address address;
	private StoreInformation storeInformation;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getSapStoreID() {
		return sapStoreID;
	}

	public void setSapStoreID(String sapStoreID) {
		this.sapStoreID = sapStoreID;
	}

	public int getComplexNumber() {
		return complexNumber;
	}

	public void setComplexNumber(int complexNumber) {
		this.complexNumber = complexNumber;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public StoreInformation getStoreInformation() {
		return storeInformation;
	}

	public void setStoreInformation(StoreInformation storeInformation) {
		this.storeInformation = storeInformation;
	}

	@Override
	public String toString() {
		return "Store [uuid=" + uuid + ", sapStoreID=" + sapStoreID + ", complexNumber=" + complexNumber + ", address="
				+ address + ", storeInformation=" + storeInformation + "]";
	}

}
