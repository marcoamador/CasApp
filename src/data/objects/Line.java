package data.objects;

public class Line {
	private int idLine;
	private String lineName;
	private String internalName;
	private int direction;
	private int idNetwork;
	
	public Line() {
        this.setIdLine(0);
        this.setLineName("");
        this.setInternalName("");
        this.setDirection(0);
        this.setIdNetwork(0);
	}

	public int getIdLine() {
		return idLine;
	}

	public void setIdLine(int idLine) {
		this.idLine = idLine;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public String getInternalName() {
		return internalName;
	}

	public void setInternalName(String internalName) {
		this.internalName = internalName;
	}

	public int getIdNetwork() {
		return idNetwork;
	}

	public void setIdNetwork(int idNetwork) {
		this.idNetwork = idNetwork;
	}
	
}

