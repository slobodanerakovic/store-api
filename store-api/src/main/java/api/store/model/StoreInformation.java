package api.store.model;

public class StoreInformation {
	private String showWarningMessage;
	private String todayOpen;
	private String locationType;
	private String collectionPoint;
	private String todayClose;

	public String getShowWarningMessage() {
		return showWarningMessage;
	}

	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	public String getTodayOpen() {
		return todayOpen;
	}

	public void setTodayOpen(String todayOpen) {
		this.todayOpen = todayOpen;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getCollectionPoint() {
		return collectionPoint;
	}

	public void setCollectionPoint(String collectionPoint) {
		this.collectionPoint = collectionPoint;
	}

	public String getTodayClose() {
		return todayClose;
	}

	public void setTodayClose(String todayClose) {
		this.todayClose = todayClose;
	}

	@Override
	public String toString() {
		return "StoreInformation [showWarningMessage=" + showWarningMessage + ", todayOpen=" + todayOpen
				+ ", locationType=" + locationType + ", collectionPoint=" + collectionPoint + ", todayClose="
				+ todayClose + "]";
	}

}
