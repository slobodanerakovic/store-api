package api.store.exceptions;

import api.store.ws.dtos.StoreLocationDTO;

public class StoreException extends RuntimeException {

	private static final long serialVersionUID = 807238185585245943L;
	private StoreLocationDTO locationDTO;
	private String message;

	public StoreException(StoreLocationDTO locationDTO) {
		super();
		this.locationDTO = locationDTO;
	}

	public StoreException(String errMessage) {
		super();
		this.message = errMessage;
	}

	public StoreLocationDTO getLocationDTO() {
		return locationDTO;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setLocationDTO(StoreLocationDTO locationDTO) {
		this.locationDTO = locationDTO;
	}

	@Override
	public String toString() {
		if (message == null) {
			return "StoreException [locationDTO=" + locationDTO + "]";
		} else {
			return message;
		}
	}

}
