package api.store.ws.dtos;

public class StoreNotFoundDTO {

	private String errorMessage;

	public StoreNotFoundDTO(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
