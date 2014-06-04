package data.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class CommentWritten {
	private int idComment;
	private int idUser;
	private int type;
	private int idWrittenComment;
	private int idCheckIn;
	private String date;
	private String latitude;
	private String longitude;
	private String comment;
	
	public CommentWritten() {
		idComment = 0;
		idUser = 0;
		type = 0;
		idWrittenComment = 0;
		idCheckIn = 0;
		date = "";
		latitude = "0.0";
		longitude = "0.0";
		comment = "";				
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
	public int getIdWrittenComment() {
		return idWrittenComment;
	}
	public void setIdWrittenComment(int idWrittenComment) {
		this.idWrittenComment = idWrittenComment;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}
