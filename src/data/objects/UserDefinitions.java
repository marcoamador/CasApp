package data.objects;

public class UserDefinitions {
	private boolean checkedIn;
    private int totalPoints;
    private Line line;
    private boolean anonymous;
    private String nickname;
    private String name;
    private int feedbackPoints;
    private String username;
	
	
	public UserDefinitions() {
		setCheckedIn(false);
		totalPoints = -1;
		line = new Line();
		nickname = "";
		anonymous = true;
	}

	public boolean isCheckedIn() {
		return checkedIn;
	}

	public void setCheckedIn(boolean checkedIn) {
		this.checkedIn = checkedIn;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFeedbackPoints() {
		return feedbackPoints;
	}

	public void setFeedbackPoints(int feedbackPoints) {
		this.feedbackPoints = feedbackPoints;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
