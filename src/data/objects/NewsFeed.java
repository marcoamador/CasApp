package data.objects;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.util.Log;

public class NewsFeed {

	public int commentId;
	public int commentType;
	public int subCommentId;
	public String message;
	public String date;
	public int idCategorisedType;
	public User user;
	public Line line;
	public int networkId;
	//public Boolean star;
	public int commentPoints;
	public int discreteClassificationCategorisedComment;
	
	public NewsFeed() {
        commentId = 0;
        commentType = -1;
        subCommentId = 0;
        message = "";
        user = null;
        line = null;
        //star = false;
        commentPoints = 0;
        idCategorisedType = 0;
        networkId = 0;
        date = "";
        discreteClassificationCategorisedComment = -1;
	}
	
	public int getDiscreteClassificationCategorisedComment() {
		return discreteClassificationCategorisedComment;
	}

	public void setDiscreteClassificationCategorisedComment(
			int discreteClassificationCategorisedComment) {
		this.discreteClassificationCategorisedComment = discreteClassificationCategorisedComment;
	}

	public int getCommentId() {
		return this.commentId;
	}
	
	public int getCommentType() {
		return commentType;
	}

	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}

	public int getSubCommentId() {
		return subCommentId;
	}

	public void setSubCommentId(int subCommentId) {
		this.subCommentId = subCommentId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getIdCategorisedType() {
		return idCategorisedType;
	}

	public void setIdCategorisedType(int idCategorisedType) {
		this.idCategorisedType = idCategorisedType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Line getLine() {
		return line;
	}

	public void setLine(Line line) {
		this.line = line;
	}

	/*public Boolean getStar() {
		return star;
	}

	public void setStar(Boolean star) {
		this.star = star;
	}*/

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	
	public String getDate() {
		//Log.d("NEWSFEED", "getDate - string= " + date + " LocalTimeZone: " + TimeZone.getDefault().toString() + " GMTTimeZone:" + TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		dateFormatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatLocale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		dateFormatLocale.setTimeZone(TimeZone.getDefault());
		Date localDate = new Date();
		String ret = "";
	    try {
	    	localDate = dateFormatGMT.parse(this.date);
	    	//Log.d("NEWSFEED", "getDate - localDate= " + localDate.toString());
	    	
	    	ret = dateFormatLocale.format(localDate);
	    	//Log.d("NEWSFEED", "getDate - dateFormatLocale= " + dateFormatLocale.toString());
	    	//Log.d("NEWSFEED", "getDate - ret= " + ret);
	    }
	    catch(Exception e) {
	    	//Log.d("NEWSFEED", "getDateDate Exception " + this.date);
    		Calendar c = Calendar.getInstance(TimeZone.getDefault());
	    	return dateFormatLocale.format(c.getTime());
	    }
		return ret;
	}

	public void setDate() {
		Calendar c = Calendar.getInstance();
		Date dateD = c.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.date = dateFormat.format(dateD);
	}	
	
	public Date getDateDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getDefault());
	    Date convertedDate = new Date();
	    try {
	    	convertedDate = dateFormat.parse(getDate()); 
	    }
	    catch(Exception e) {
	    	Log.d("NEWSFEED", "getDateDate Exception " + this.date);
    		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
	    	return c.getTime();
	    }
	    return convertedDate;
	}
	
	public boolean equals(Object obj) {
		NewsFeed nf = (NewsFeed)obj;
		if(nf.getCommentId() == this.getCommentId())
			return true;
		return false;
	}
	
	public int getCommentPoints(){
		return commentPoints;
	}
	
	public void setCommentPoints(int points){
		commentPoints = points;
	}
	
}
