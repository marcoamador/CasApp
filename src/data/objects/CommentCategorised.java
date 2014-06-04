package data.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class CommentCategorised {
	private int idComment;
	private int idUser;
	private int type;
	private int idCategorisedComment;
	private int idCheckIn;
	private String date;
	private String latitude;
	private String longitude;
	
	private int classification;
	private int discreteClassification;
	private int idTypeCategorisedComment;
	private Boolean validated;
	
	private Line line;
	//private Rate rate;
	
	public CommentCategorised() {
		idComment = 0;
		idUser = 0;
		type = 1;
		idCategorisedComment = 0;
		idCheckIn = 0;
		date = "";
		latitude = "0.0";
		longitude = "0.0";
		classification = 0;
		discreteClassification = 0;
		idTypeCategorisedComment = 0;
		validated = false;
		line = new Line();
		//rate = new Rate();
	}
	
	public int getIdComment() {
		return idComment;
	}
	public void setIdComment(int idComment) {
		this.idComment = idComment;
	}
	public int getIdUser() {
		return idUser;
	}
	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIdCheckIn() {
		return idCheckIn;
	}
	public void setIdCheckIn(int idCheckIn) {
		this.idCheckIn = idCheckIn;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getDate() {
		SimpleDateFormat dateFormatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatLocale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatLocale.setTimeZone(TimeZone.getDefault());
		Date localDate = new Date();
		String ret = "";
	    try {
	    	localDate = dateFormatGMT.parse(this.date);
	    	
	    	ret = dateFormatLocale.format(localDate);
	    }
	    catch(Exception e) {
    		Calendar c = Calendar.getInstance(TimeZone.getDefault());
	    	return dateFormatLocale.format(c.getTime());
	    }
		return ret;
	}
	public void setDate() {
		Calendar c = Calendar.getInstance();
		Date dateD = c.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.date = dateFormat.format(dateD);
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public int getIdCategorisedComment() {
		return idCategorisedComment;
	}

	public void setIdCategorisedComment(int idCategorisedComment) {
		this.idCategorisedComment = idCategorisedComment;
	}

	public int getClassification() {
		return classification;
	}

	public void setClassification(int classification) {
		this.classification = classification;
	}

	public int getDiscreteClassification() {
		return discreteClassification;
	}

	public void setDiscreteClassification(int discreteClassification) {
		this.discreteClassification = discreteClassification;
	}

	public int getIdTypeCategorisedComment() {
		return idTypeCategorisedComment;
	}

	public void setIdTypeCategorisedComment(int idTypeCategorisedComment) {
		this.idTypeCategorisedComment = idTypeCategorisedComment;
	}

	public Boolean getValidated() {
		return validated;
	}

	public void setValidated(Boolean validated) {
		this.validated = validated;
	}
	
	
	public Date getDateDate() {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getDefault());
	    Date convertedDate = new Date();
	    try {
	    	convertedDate = dateFormat.parse(getDate()); 
	    }
	    catch(Exception e) {
    		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	    	return c.getTime();
	    }
	    return convertedDate;
	}

	public static int calculateDiscreteClassification(int comment_progress, int array_length, int seekbar) {
		return (int)Math.ceil(((double)((double)(comment_progress * array_length)) / seekbar));
	}
	
	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	/*public Rate getRate() {
		return rate;
	}

	public void setRate(Rate rate) {
		this.rate = rate;
	}	*/
}
