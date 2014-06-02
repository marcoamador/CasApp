package data.objects;

import java.util.ArrayList;

public class Network {
	private int networkId;
	private String networkName;
	private ArrayList<User> users;
	
	public Network() {
		this.networkId = 0;
		this.networkName = "";
		this.setUsers(new ArrayList<User>());
	}
	


	public ArrayList<User> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}



	public int getNetworkId() {
		return networkId;
	}



	public void setNetworkId(int networkId) {
		this.networkId = networkId;
	}



	public String getNetworkName() {
		return networkName;
	}



	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}	
}
