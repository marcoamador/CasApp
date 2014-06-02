package data.objects;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class JourneyPath {
	private Stop origin;
	private Stop destination;
	private ArrayList<Network> networks;
	private ArrayList<Stop> via;
	private ArrayList<Line> lines;
	private int numberStops;
	
	private String startTime;
	private int idJourney;
	
	public JourneyPath() {
		this.setOrigin(new Stop());
		this.setDestination(new Stop());
		this.setNetworks(new ArrayList<Network>());
		this.setVia(new ArrayList<Stop>());
		this.setLines(new ArrayList<Line>());
		this.numberStops = 0;
		this.startTime = "";
		this.idJourney = 0;
	}

	public Stop getOrigin() {
		return origin;
	}

	public void setOrigin(Stop origin) {
		this.origin = origin;
	}

	public ArrayList<Network> getNetworks() {
		return networks;
	}

	public void setNetworks(ArrayList<Network> networks) {
		this.networks = networks;
	}

	public Stop getDestination() {
		return destination;
	}

	public void setDestination(Stop destination) {
		this.destination = destination;
	}

	public ArrayList<Stop> getVia() {
		return via;
	}

	public void setVia(ArrayList<Stop> via) {
		this.via = via;
	}

	public int getNumberStops() {
		return numberStops;
	}

	public void setNumberStops(int numberStops) {
		this.numberStops = numberStops;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public String getStartTime() {
		Log.d("JOURNEYPATH", "getDate - string= " + startTime + " LocalTimeZone: " + TimeZone.getDefault().toString() + " GMTTimeZone:" + TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dateFormatLocale = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatLocale.setTimeZone(TimeZone.getDefault());
		Date localDate = new Date();
		String ret = "";
	    try {
	    	localDate = dateFormatGMT.parse(this.startTime);
	    	Log.d("JOURNEYPATH", "getDate - localDate= " + localDate.toString());
	    	
	    	ret = dateFormatLocale.format(localDate);
	    	Log.d("JOURNEYPATH", "getDate - dateFormatLocale= " + dateFormatLocale.toString());
	    	Log.d("JOURNEYPATH", "getDate - ret= " + ret);
	    }
	    catch(Exception e) {
	    	Log.d("JOURNEYPATH", "getDateDate Exception " + this.startTime);
    		Calendar c = Calendar.getInstance(TimeZone.getDefault());
	    	return dateFormatLocale.format(c.getTime());
	    }
		return ret;
	}

	public void setStartTime(String startTimep) {
		SimpleDateFormat dateFormatDefault = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatDefault.setTimeZone(TimeZone.getDefault());		
		Date startTimeD = new Date();
		try {
			startTimeD = dateFormatDefault.parse(startTimep);
		}
		catch(Exception e) {
			
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.startTime = dateFormat.format(startTimeD);
	}
	
	
	public void setStartTime(Date startTimeD) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		this.startTime = dateFormat.format(startTimeD);
	}
	
	public String toString() {
		return this.origin.getName() + " to " + this.destination.getName();
	}

	public int getIdJourney() {
		return idJourney;
	}

	public void setIdJourney(int idJourney) {
		this.idJourney = idJourney;
	}

}
