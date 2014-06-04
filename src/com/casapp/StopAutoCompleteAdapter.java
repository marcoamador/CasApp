package com.casapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import data.objects.Stop;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class StopAutoCompleteAdapter extends ArrayAdapter<Stop> implements Filterable {
	private ArrayList<Stop> stops;
	private ArrayList<Stop> filteredStops;
	private String constr;
	private String prevConstr;
	private Context currentContext;
		
	public StopAutoCompleteAdapter(Context context, int resource,
			int textViewResourceId) {
		super(context, resource, textViewResourceId);
		filteredStops = new ArrayList<Stop>();
		stops = new ArrayList<Stop>();
		constr = "";
		prevConstr = "";
		currentContext = context;
	}
	
	public StopAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		filteredStops = new ArrayList<Stop>();
		stops = new ArrayList<Stop>();
		constr = "";
		prevConstr = "";
		currentContext = context;
	}
	
	public StopAutoCompleteAdapter(Context context, int textViewResourceId, List<Stop> st) {
		super(context, textViewResourceId);
		filteredStops = ((ArrayList<Stop>) st);
		stops = new ArrayList<Stop>();
		constr = "";
		prevConstr = "";
		currentContext = context;
	}	

	@Override
	public int getCount() {
		return filteredStops.size();
	}

	@Override
	public Stop getItem(int position) {
		Log.d("STOPAUTOCOMPLETEADAPTER", "getItem(int position)= " + position);
		if(filteredStops == null)
			return new Stop();
		
		if(filteredStops.isEmpty())
			return new Stop();		
		
		if(filteredStops.size() > position)
			return filteredStops.get(position);
		return new Stop();
	}
	
	public Stop getItem(Stop s) {
		Log.d("STOPAUTOCOMPLETEADAPTER", "getItem(Stop s)= " + (s == null));
		if(filteredStops == null)
			return new Stop();
		if(filteredStops.isEmpty() || !filteredStops.contains(s))
			return new Stop();		
		
		int pos = filteredStops.indexOf(s);
		if(pos == -1 || pos >= filteredStops.size())
			return new Stop();
		return filteredStops.get(pos);
	}
	
	public Stop getItemStops(int position) {
		Log.d("STOPAUTOCOMPLETEADAPTER", "getItemStops(int position)= " + position);		
		
		if(stops == null)
			return new Stop();
		if(stops.size() > position)
			return stops.get(position);
		return new Stop();
	}	
	
	public Stop getItemStops(Stop s) {
		Log.d("STOPAUTOCOMPLETEADAPTER", "getItemStops(Stop s)= " + (s == null));
		
		if(stops == null)
			return new Stop();
		if(stops.isEmpty() || !stops.contains(s))
			return new Stop();
		
		int pos = stops.indexOf(s);
		if(pos == -1 || pos >= stops.size())
			return new Stop();
		return stops.get(pos);
	}
		

	@Override
	public int getPosition(Stop item) {
		Log.d("STOPAUTOCOMPLETEADAPTER", "getPosition(Stop s)= " + (item == null));
		
		if(filteredStops == null)
			return -1;
		
		if(filteredStops.isEmpty() || !filteredStops.contains(item))
			return -1;
		
		
		return filteredStops.indexOf(item);
	}	

	@Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
            	
                FilterResults filterResults = new FilterResults();
                
                if(constraint != null) {
                	boolean needRefresh = false;
                	constr = constraint.toString();
                	
                	//System.out.println("CONSTRAINT: " + constraint.toString());                	
                	//System.out.println("CONSTR: " + constr);
                	//System.out.println("PREVCONSTR: " + prevConstr.toString());
                	//if(filteredStops.size() > 0)
                		//System.out.println("FIRST: " + filteredStops.get(0).getName());
                	
                	if(constr.length() > 2) {
                		if(!constr.toUpperCase().startsWith(prevConstr.substring(0, 2).toUpperCase())) {
                			needRefresh = true;
                			constr = constr.substring(0, 2);
                		}
                		
                		
	                	/*if(constr.length() < prevConstr.length()) {
	                		if(!prevConstr.startsWith(constr.substring(0, 2))) {
	                			needRefresh = true;
	                			constr = constr.substring(0, 2);
	                		}
	                			
	                	}
	                	else if(prevConstr.length() >= constr.length()) {
	                		if(!constr.startsWith(prevConstr.substring(0, 2))) {
	                			needRefresh = true;
	                			constr = constr.substring(0, 2);
	                		}
	            		}*/
                	}
                	else if(constr.length() == 2) {
                		if(filteredStops != null && stops != null) {
	                		if(filteredStops.size() > 0) {
	                    		if(!filteredStops.get(0).getName().toUpperCase().startsWith(constr.toUpperCase())) {                		
	                    			needRefresh = true;
	                    		}
	                    		else if(stops.size() == 0)
	                    			needRefresh = true;
	                		}
	                		else if(stops.size() == 0)
	                			needRefresh = true;
                		}
                	}
                	if(filteredStops != null) {
	            		if(filteredStops.size() > 0) {
	                		if(!filteredStops.get(0).getName().toUpperCase().startsWith(constr.toUpperCase())) {
	                			filteredStops.clear();
	                		}
	            		}
                	}
            		Log.d("STOPAUTOCOMPLETEADAPTER", "Need refresh: " + needRefresh);
            		Log.d("STOPAUTOCOMPLETEADAPTER", "CONSTRAINT: " + constraint.toString());
            		Log.d("STOPAUTOCOMPLETEADAPTER", "CONSTR: " + constr);
            		Log.d("STOPAUTOCOMPLETEADAPTER", "PREVCONSTR: " + prevConstr.toString());         	
                	
                	prevConstr = constraint.toString();
                	
                	if(needRefresh == true) {
        				String uriGetStops = "stop/" + constr.toString();
        				try {
        					if (isOnline()) {
								stops = CasApp.doGet(currentContext, uriGetStops, new HashMap<String,Object>(), new TypeToken<ArrayList<Stop>>() {}.getType());
								filteredStops = new ArrayList<Stop>(stops);
        					}
        					else {
        			    		handler.sendEmptyMessage(1);
        					}
        				}
		    			catch(Exception e) {
							handler.sendEmptyMessage(0);
		    			}
                	}
                	else if(constraint.length() >= 2) {
                		filteredStops.clear();
                		for(int i = 0; i < stops.size(); i++) {
                			if(stops != null && filteredStops != null) {
                				if(stops.get(i) != null) {
		                			if(stops.get(i).toString().toUpperCase().startsWith(constraint.toString().toUpperCase()))
		                				filteredStops.add(stops.get(i));
                				}
                			}
                		}
                		Log.d("STOPAUTOCOMPLETEADAPTER", "COUNT FilteredStops: " + filteredStops.size());
                		Log.d("STOPAUTOCOMPLETEADAPTER", "COUNT stops: " + stops.size());
                	}
                	else {
                		filteredStops.clear();
                		stops.clear();
                	}
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = filteredStops;
                    filterResults.count = filteredStops.size();
                }
                
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null ) {
                	if(results.count > 0)
                		notifyDataSetChanged();
                	else
                		notifyDataSetInvalidated();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
	
	
	
	//---------- UI HANDLER -------------	
	//Provides the user with information in case of failure of the web service requests 
	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			//0 - does not do anything
			//1 - no internet connection
			//2 - ok
			switch(msg.what) {
				case 0:
					//Exception
					break;
				case 1:
					Toast.makeText(currentContext.getApplicationContext(), currentContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	
	//---------- IS ONLINE -------------
	//Checks if there is internet connection
	private boolean isOnline() {		
		ConnectivityManager conMgr = (ConnectivityManager) currentContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
				&& conMgr.getActiveNetworkInfo().isConnected())
			return true;
		else return false;
	}	
}

