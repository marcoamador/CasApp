package data.objects;


public class AppException {

	private String description;
	
	public AppException() {
		this.setDescription("");
		
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
