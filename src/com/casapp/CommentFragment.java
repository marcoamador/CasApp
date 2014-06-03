package com.casapp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.casapp.FeedFragment.SearchFeedTask;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import data.objects.JourneyPath;
import data.objects.Stop;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommentFragment extends Fragment{
	
		ListView checkInResultList;
		CheckInResultsAdapter checkInResultAdapter;
		ArrayList<JourneyPath> checkInResults = new ArrayList<JourneyPath>();
		View view;
		String checkInOrigin = "";
		String checkInDestination = "";
		public static int selectedRow = -1;
		String written = "";

	    // newInstance constructor for creating fragment with arguments
	    public static CommentFragment newInstance() {
	    	CommentFragment commentFragment = new CommentFragment();
	        Bundle args = new Bundle();
	        commentFragment.setArguments(args);
	        return commentFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        view = inflater.inflate(R.layout.comment_fragment, container, false);
	        
	        LinearLayout notLoggedInFrame = (LinearLayout) view.findViewById(R.id.notLoggedInComment);
	        final LinearLayout notCheckedInFrame = (LinearLayout) view.findViewById(R.id.notCheckedInComment);
	        final LinearLayout noCheckInResultsFrame = (LinearLayout) view.findViewById(R.id.noCheckInResultsFrame);
	        final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
	        final LinearLayout commentFrame = (LinearLayout) view.findViewById(R.id.commentMenuFrame);
	        
	        final Button commentLoginButton = (Button) view.findViewById(R.id.CommentLoginButton);
	        final Button checkInSwitch = (Button) view.findViewById(R.id.addCheckinManualSwitch);
	        final Button goBackButton = (Button) view.findViewById(R.id.goBackButtonCheckIn);
	        final Button checkInButton = (Button) view.findViewById(R.id.checkInButton);
	        
	        Button atmosphere = (Button) view.findViewById(R.id.atmosphereCommentButton);
	        
	        
	        final AutoCompleteTextView originCheckIn = (AutoCompleteTextView) view.findViewById(R.id.addCheckinManualOrigin);
	        final AutoCompleteTextView destinationCheckIn = (AutoCompleteTextView) view.findViewById(R.id.addCheckinManualDestination);
	        
	        final StopAutoCompleteAdapter originStopAdapter = new StopAutoCompleteAdapter(this.getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        final StopAutoCompleteAdapter destinationStopAdapter = new StopAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        originCheckIn.setAdapter(originStopAdapter);
	        destinationCheckIn.setAdapter(destinationStopAdapter);
	        
	        notCheckedInFrame.setVisibility(View.GONE);
	        commentFrame.setVisibility(View.GONE);
	        notLoggedInFrame.setVisibility(View.GONE);
	        noCheckInResultsFrame.setVisibility(View.GONE);
	        checkInResultsFrame.setVisibility(View.GONE);
	        
	        checkInResultList = (ListView) view.findViewById(R.id.checkInResultslist);
	        checkInResultAdapter = new CheckInResultsAdapter(getActivity(), R.layout.custom_listview_item_search_results, checkInResults);
	        checkInResultList.setAdapter(checkInResultAdapter);
	        
	        checkInResultList.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long arg) {
					checkInResultAdapter.setSelectedRow(position);
					checkInResultList.invalidateViews();
					view.refreshDrawableState();
				}
			});
	        
	        
	        
	        if(!MainNavActivity.isLoggedin()){
	        	notLoggedInFrame.setVisibility(View.VISIBLE);
	        	notCheckedInFrame.setVisibility(View.GONE);
	        	commentFrame.setVisibility(View.GONE);
	        }else if(MainNavActivity.isLoggedin() && !MainNavActivity.isCheckedIn()){
	        	notLoggedInFrame.setVisibility(View.GONE);
	        	notCheckedInFrame.setVisibility(View.VISIBLE);
	        	commentFrame.setVisibility(View.GONE);
	        }else if(MainNavActivity.isCheckedIn()){
	        	commentFrame.setVisibility(View.VISIBLE);
	        	notLoggedInFrame.setVisibility(View.GONE);
	        	notCheckedInFrame.setVisibility(View.GONE);
	        }
	        
	        
	        goBackButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					noCheckInResultsFrame.setVisibility(View.GONE);
					notCheckedInFrame.setVisibility(View.VISIBLE);
					
				}
			});
	        
	        
	        commentLoginButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), LoginActivity.class);
					startActivity(i);
					return;
				}
			});
	        
	        checkInSwitch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String origin = originCheckIn.getText().toString();
					String destination = destinationCheckIn.getText().toString();
					originCheckIn.setText(destination);
					destinationCheckIn.setText(origin);
				}
			});
	        
	        checkInButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(selectedRow >= 0){
						String uriCheckIn = "journey/checkin";
	        			String typeStr = "1";
	        			SearchCheckInTask checkinTask = new SearchCheckInTask();
	        			checkinTask.execute(typeStr, uriCheckIn);
					}
					else {
	        			Toast.makeText(getActivity(), R.string.no_checkin_options_selected, Toast.LENGTH_SHORT).show();
	        		}
					
				}
			});
	        
	        Button checkInSearchButton = (Button) view.findViewById(R.id.AddManualCheckinButton);
	        checkInSearchButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					boolean readyStart = false;
					boolean readyEnd = false;
					
					if(originCheckIn.getText().toString().length() > 0 && destinationCheckIn.getText().toString().length() > 0 && originStopAdapter != null && destinationStopAdapter != null) {
						
						String originString = originCheckIn.getText().toString();
						String destinationString = destinationCheckIn.getText().toString();

						Stop origin = new Stop();
						origin.setName(originString);
						Stop destination = new Stop();
						destination.setName(destinationString);

						if(originStopAdapter.getItem(origin) == null && originStopAdapter.getCount() > 0) {
							origin = new Stop(originStopAdapter.getItem(0));
							originCheckIn.setText(origin.toString());
							readyStart = true;
						}
						else if(originStopAdapter.getItem(origin) != null) {
							origin = new Stop(originStopAdapter.getItem(origin));
							originCheckIn.setText(origin.toString());
							readyStart = true;
						}

						if(destinationStopAdapter.getItem(destination) == null && destinationStopAdapter.getCount() > 0) {
							destinationCheckIn.setText(destinationStopAdapter.getItem(0).toString());
							destination = new Stop(destinationStopAdapter.getItem(0));
							readyEnd = true;
						}
						else if(destinationStopAdapter.getItem(destination) != null) {
							destination = new Stop(destinationStopAdapter.getItem(destination));
							destinationCheckIn.setText(destination.toString());
							readyEnd = true;
						}
						
					}else if(originCheckIn.getText().toString().length() > 0 && originStopAdapter != null) {
						String originString = originCheckIn.getText().toString();
						
						Stop st = new Stop();
						st.setName(originString);
						
						if(originStopAdapter.getItem(st) == null && originStopAdapter.getCount() > 0) {
							st = new Stop(originStopAdapter.getItem(0));
							originCheckIn.setText(st.toString());
							readyStart = true;
						}
						else if(originStopAdapter.getItem(st) != null) {
							st = new Stop(originStopAdapter.getItem(st));
							originCheckIn.setText(st.toString());
							readyStart = true;
						}
					}else if(destinationCheckIn.getText().toString().length() > 0 && destinationStopAdapter != null) {
						String destinationString = destinationCheckIn.getText().toString();
						
						Stop st = new Stop();
						st.setName(destinationString);
						
						if(originStopAdapter.getItem(st) == null && destinationStopAdapter.getCount() > 0) {
							st = new Stop(destinationStopAdapter.getItem(0));
							destinationCheckIn.setText(st.toString());
							readyEnd = true;
						}
						else if(destinationStopAdapter.getItem(st) != null) {
							st = new Stop(destinationStopAdapter.getItem(st));
							destinationCheckIn.setText(st.toString());
							readyEnd = true;
						}
					}
					
					String uriCheckIn="";
					String typeStr = "0";
					if(readyStart == true && readyEnd == true) {
						uriCheckIn = "journey/search?origin=" + originCheckIn.getText().toString().replace(" ", "%20") + "&destination=" + destinationCheckIn.getText().toString().replace(" ", "%20");
					}else if(readyStart == true){
						uriCheckIn = "journey/lines?stop=" + originCheckIn.getText().toString().replace(" ", "%20");
					}else if(readyEnd == true){
						uriCheckIn = "journey/lines?stop=" + destinationCheckIn.getText().toString().replace(" ", "%20");
					}else{
						noCheckInResultsFrame.setVisibility(View.VISIBLE);
						notCheckedInFrame.setVisibility(View.GONE);
						return;
					}

					notCheckedInFrame.setVisibility(View.GONE);
					checkInResultsFrame.setVisibility(View.VISIBLE);

					SearchCheckInTask searchCheckIn = new SearchCheckInTask();
					searchCheckIn.execute(typeStr, uriCheckIn);	
					Log.d("PLANJOURNEY", "pathsResults " + MainNavActivity.getFeedResults().size());
				}
			});
	        return view;
	    }
	    
	    //---------- UI HANDLER -------------	
	    //Provides the user with information in case of failure of the web service requests 
	    final Handler handler = new Handler() {
	    	public void handleMessage(Message msg) {
	    		//dialog.dismiss();
	    		//0 - does not do anything
	    		//1 - no internet connection
	    		//2 - ok
	    		switch(msg.what) {
	    		case 0:
	    			break;
	    		case 1:
	    			Toast.makeText(getActivity(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
	    			break;
	    		default:
	    			break;
	    		}
	    	}
	    };	
	  		
	    //---------- UPLOAD DATA -------------
	    //Check Out
	    class SearchCheckInTask extends AsyncTask<String, String, String> {

	    	@Override
	    	protected void onPreExecute() {
	    		super.onPreExecute();
	    		LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
	    		linlaHeaderProgress.setVisibility(View.VISIBLE);
	    	}


	    	@Override
	    	protected String doInBackground(String... params) {
	    		String typeStr = params[0];
	    		int type = Integer.parseInt(typeStr);
	    		String ret = "";

	    		Log.d("CHECKIN", "DOINBACKGROUND - type= " + typeStr);

	    		if (isOnline()) {
	    			try {
	    				switch(type) {
	    				case 0:
	    					String uriGetLines= params[1];
	    					checkInResults = (ArrayList<JourneyPath>) CasApp.doGet(getActivity(), uriGetLines, MainNavActivity.headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType());
	    					ret = "ok getlines";


	    					break; 	
	    					
	    				case 1:
	    					String uriCheckIn = params[1];
	    					Log.d("CHECKIN", "CHECKIN BUTTON: selectedLine=" + selectedRow);
    		    			
    		    			JourneyPath jp = (JourneyPath)(checkInResultAdapter.getItem(selectedRow));
		            		Calendar c = Calendar.getInstance();
		            		Date dateCheckIn = c.getTime();
		            		jp.setStartTime(dateCheckIn);
		            		checkInOrigin = jp.getOrigin().getName();
		            		checkInDestination = jp.getDestination().getName();
		            		CasApp.doPut(getActivity(), uriCheckIn, MainNavActivity.headers, new TypeToken<JourneyPath>() {}.getType(), jp);
		    				ret = "ok checkin";
    						handler.sendEmptyMessage(3);
    		    			break;
	    				}
	    			}
	    			catch(Exception e) {
	    				handler.sendEmptyMessage(0);
	    				return e.getMessage();
	    			}
	    		}
	    		else {
	    			handler.sendEmptyMessage(1);
	    			return "ok";					
	    		}
	    		Log.d("CHECKIN", "DOINBACKGROUND - ret= " + ret);
	    		return ret;
	    	}

	    	@Override
	    	protected void onPostExecute(String result) {
	    		super.onPostExecute(result);
	    		Log.d("CHECKIN", "POSTEXECUTE - result= " + result);	

	    		//Checks if there was an exception
	    		if(!result.startsWith("ok")) {
	    			Log.d("CHECKIN", "POSTEXECUTE - Exception");
	    			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
	    		}
	    		else if(result.contains("getlines")) {
	    			Log.d("CHECKIN", "POSTEXECUTE - planjourney");
	    			
	    		    final LinearLayout noCheckInResultsFrame = (LinearLayout) view.findViewById(R.id.noCheckInResultsFrame);
	    		    final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
	    			
	    			LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
		    		linlaHeaderProgress.setVisibility(View.VISIBLE);
	    			if(checkInResults.size() > 0){
	    				linlaHeaderProgress.setVisibility(View.GONE);
	    				checkInResultAdapter.updateList(checkInResults);
	    			}else{
	    				checkInResultsFrame.setVisibility(View.GONE);
	    				linlaHeaderProgress.setVisibility(View.GONE);
	    				noCheckInResultsFrame.setVisibility(View.VISIBLE);

	    			}

	    		}
	    		else if(result.contains("checkin")) {
					SharedPreferences pref = getActivity().getSharedPreferences(CasApp.PREFS_NAME, Context.MODE_PRIVATE);
					pref.edit().putBoolean(CasApp.PREF_CHECKIN, true).commit();
					MainNavActivity.setCheckedIn(true);
					
					LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressCheckIn);
		    		final LinearLayout checkInResultsFrame = (LinearLayout) view.findViewById(R.id.checkinResultsFrame);
					LinearLayout commentMenu = (LinearLayout) view.findViewById(R.id.commentMenuFrame);
					TextView commentOrigin = (TextView) view.findViewById(R.id.commentMenuOrigin);
					TextView commentDestination = (TextView) view.findViewById(R.id.commentMenuDestination);
					
					
					linlaHeaderProgress.setVisibility(View.GONE);
					checkInResultsFrame.setVisibility(View.GONE);
					
					commentOrigin.setText(checkInOrigin);
					commentDestination.setText(checkInDestination);
					
					commentMenu.setVisibility(View.VISIBLE);
				}
	    	}
	    }	
	  		
	  	//---------- IS ONLINE -------------
		//Checks if there is internet connection
		private boolean isOnline() {		
			ConnectivityManager conMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected())
				return true;
			else return false;
		}	
}