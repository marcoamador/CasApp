package data.objects;

public class Stop {
	private int idStop;
	private String name;
	private String externalIdCode;
	private String description;
	private String transportType;
	
	public Stop() {
		this.setIdStop(0);
		this.setName("");
		this.setExternalIdCode("");
		this.setDescription("");
		this.setTransportType("");
	}
	
	public Stop(Stop s) {
		this.setIdStop(s.getIdStop());
		this.setName(s.getName());
		this.setExternalIdCode(s.getExternalIdCode());
		this.setDescription(s.getDescription());
		this.setTransportType(s.getTransportType());
	}	

	public int getIdStop() {
		return idStop;
	}

	public void setIdStop(int id) {
		this.idStop = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExternalIdCode() {
		return externalIdCode;
	}

	public void setExternalIdCode(String externalIdCode) {
		this.externalIdCode = externalIdCode;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
	public String toString() {
		return this.name;
		//return this.name + " [" + this.transportType + "]"; 
	}
	
	
	public boolean equals(Object obj) {
		if(this.toString().equals(((Stop)obj).toString()))
			return true;
		return false;
	}
}
