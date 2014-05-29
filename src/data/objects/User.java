package data.objects;

public class User {
	private int userId;
	private String name;
	private String username;
	private String password;
	private String birthday;
	private String email;
	private String address;
	private String notificationId;
	private int totalPoints;
	private int reputationPoints;
	private boolean anonymous;
	private String nickname;
	
	
	public User() {
        this.userId = -1;
        this.name = "";
        this.email = "";
        this.username = "";
        this.password = "";
        this.birthday = "";
        this.address = "";
        this.setNotificationId("");
        this.totalPoints = 0;
        this.anonymous = false;
        this.nickname = "";
	}
	
	public int getUserId() {
		return this.userId;
	}
	
	public void setUserId(int i) {
		this.userId = i;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String n) {
		this.name = n;
	}	
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String p) {
		this.password = p;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String e) {
		this.email = e;
	}		
	
	public String getAddress() {
		return this.address;
	}
	
	public void setAddress(String a) {
		this.address = a;
	}	

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}	
	
	public String getNotificationId() {
		return notificationId;
	}

	public void setNotificationId(String notificationId) {
		this.notificationId = notificationId;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getReputationPoints() {
		return reputationPoints;
	}

	public void setReputationPoints(int reputationPoints) {
		this.reputationPoints = reputationPoints;
	}
}
