package com.casapp;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.casapp.RefreshableListView.onListLoadMoreListener;
import com.casapp.RefreshableListView.onListRefreshListener;
import com.google.gson.reflect.TypeToken;

import data.objects.JourneyPath;
import data.objects.Network;
import data.objects.NewsFeed;
import data.objects.Stop;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class FeedFragment extends Fragment{
	
	    View view;
	    ListView feedResultList;
	    ListView selectFeedList;
	    RefreshableListView feedList;
	    SelectFeedAdapter selectFeedAdapter;
	    FeedResultsAdapter feedResultAdapter;
	    NewsFeedAdapter feedAdapter;
	    ProgressBar bar;
	    int size = 0;
	    Boolean refresh = false;

	    // newInstance constructor for creating fragment with arguments
	    public static FeedFragment newInstance() {
	    	FeedFragment feedFragment = new FeedFragment();
	        Bundle args = new Bundle();
	        feedFragment.setArguments(args);
	        return feedFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        view = inflater.inflate(R.layout.feed_fragment, container, false);
	        
	        final RadioButton viewFeed = (RadioButton) view.findViewById(R.id.viewFeed);
	        final RadioButton selectFeed = (RadioButton) view.findViewById(R.id.selectFeed);
	        final RadioButton receiveFeed = (RadioButton) view.findViewById(R.id.receiveFeed);
	        
	        final LinearLayout feedFrame = (LinearLayout) view.findViewById(R.id.feedFrame);
	        final LinearLayout selectFeedFrame = (LinearLayout) view.findViewById(R.id.selectFeedFrame);
	        final LinearLayout receiveFeedFrame = (LinearLayout) view.findViewById(R.id.receiveFeedFrame);
	        final LinearLayout noFeedResultsFrame = (LinearLayout) view.findViewById(R.id.noFeedResultsFrame);
	        final LinearLayout noFeedsSelected = (LinearLayout) view.findViewById(R.id.noFeedsSelected);
	        
	        //Listeners do separador Receive New Feed
	        Button locationButton = (Button) view.findViewById(R.id.AddFeedUseLocation);
	        Button manualButton = (Button) view.findViewById(R.id.AddFeedManual);
	        
	        final LinearLayout addFeedGpsFrame = (LinearLayout) view.findViewById(R.id.gpsResultsFrame);
	        final LinearLayout addFeedManuallyFrame = (LinearLayout) view.findViewById(R.id.AddFeedManuallyFrame);
	        final LinearLayout feedResultsFrame = (LinearLayout) view.findViewById(R.id.FeedResultsFrame);
	        
	        //Origin Stop
	        final AutoCompleteTextView originTextView = (AutoCompleteTextView) view.findViewById(R.id.addFeedManualOrigin);
	        final AutoCompleteTextView destinationTextView = (AutoCompleteTextView) view.findViewById(R.id.addFeedManualDestination);
	        final Button addFeedSwitch = (Button) view.findViewById(R.id.addFeedManualSwitch);
	        final StopAutoCompleteAdapter originStopAdapter = new StopAutoCompleteAdapter(this.getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        final StopAutoCompleteAdapter destinationStopAdapter = new StopAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item, R.id.item);
	        originTextView.setAdapter(originStopAdapter);
	        destinationTextView.setAdapter(destinationStopAdapter);
	        
	        
		    feedResultList = (ListView) view.findViewById(R.id.manualAddFeedlist);
			    
	    	feedResultAdapter = new FeedResultsAdapter(this.getActivity(), R.layout.custom_listview_item_search_results, MainNavActivity.feedResults);
		    feedResultList.setAdapter(feedResultAdapter);
		    
		    feedResultList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
						feedResultAdapter.setSelectedRows(position);
						feedResultList.invalidateViews();
						view.refreshDrawableState();
					
				}
			});

		    selectFeedList = (ListView) view.findViewById(R.id.selectFeedlist);
		    selectFeedAdapter = new SelectFeedAdapter(this.getActivity(), R.layout.custom_listview_current_feeds, MainNavActivity.feedsSubscribed);
		    selectFeedList.setAdapter(selectFeedAdapter);
		    
		    
		    feedList = (RefreshableListView) view.findViewById(R.id.RefreshList);
		    feedAdapter = new NewsFeedAdapter(this.getActivity(), R.layout.feed_item, MainNavActivity.feed);
		    feedList.setAdapter(feedAdapter);
		    feedList.setOnListRefreshListener(new onListRefreshListener() {
				
				@Override
				public void Refresh(RefreshableListView list) {
					Toast.makeText(getActivity(), "Refreshing...", Toast.LENGTH_SHORT).show();
					refreshList(list);
					
				}
			});
		    
		    feedList.setOnListLoadMoreListener(new onListLoadMoreListener() {
				
				@Override
				public void LoadMore(RefreshableListView list) {
					// TODO Auto-generated method stub
					
				}
			});
		    
		    
		    
		    
		  
	        
	        final Button addManualFeed = (Button) view.findViewById(R.id.ManualAddFeed);
	        
	        
	        viewFeed.setChecked(true);
	        selectFeed.setChecked(false);
	        receiveFeed.setChecked(false);
	        
	        if(MainNavActivity.getNumFeeds() == 0){
	        	addFeedManuallyFrame.setVisibility(View.GONE);
				addFeedGpsFrame.setVisibility(View.GONE);
	        	noFeedsSelected.setVisibility(View.VISIBLE);
	        	feedFrame.setVisibility(View.GONE);
		        selectFeedFrame.setVisibility(View.GONE);
		        receiveFeedFrame.setVisibility(View.GONE);
		        feedResultsFrame.setVisibility(View.GONE);
		        noFeedResultsFrame.setVisibility(View.GONE);
		        
	        }else{
	        	addFeedManuallyFrame.setVisibility(View.GONE);
				addFeedGpsFrame.setVisibility(View.GONE);
	        	noFeedsSelected.setVisibility(View.GONE);
	        	feedFrame.setVisibility(View.VISIBLE);
				selectFeedFrame.setVisibility(View.GONE);
		        receiveFeedFrame.setVisibility(View.GONE);
		        feedResultsFrame.setVisibility(View.GONE);
		        noFeedResultsFrame.setVisibility(View.GONE);
	        }
	        
	        viewFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.getNumFeeds() == 0){
						addFeedManuallyFrame.setVisibility(View.GONE);
						addFeedGpsFrame.setVisibility(View.GONE);
			        	noFeedsSelected.setVisibility(View.VISIBLE);
			        	feedFrame.setVisibility(View.GONE);
				        selectFeedFrame.setVisibility(View.GONE);
				        receiveFeedFrame.setVisibility(View.GONE);
				        feedResultsFrame.setVisibility(View.GONE);
				        noFeedResultsFrame.setVisibility(View.GONE);
			        }else{
			        	if(feedFrame.getVisibility() != View.VISIBLE){
			        		addFeedManuallyFrame.setVisibility(View.GONE);
							addFeedGpsFrame.setVisibility(View.GONE);
				        	noFeedsSelected.setVisibility(View.GONE);
				        	feedFrame.setVisibility(View.VISIBLE);
							selectFeedFrame.setVisibility(View.GONE);
					        receiveFeedFrame.setVisibility(View.GONE);
					        feedResultsFrame.setVisibility(View.GONE);
					        noFeedResultsFrame.setVisibility(View.GONE);
					        
					        Set<Integer> unique = new HashSet<Integer>();
					        unique.addAll(MainNavActivity.networksList);
					        size = unique.size();
					        Log.d("size inicial", String.valueOf(size));
					        
					        for(Integer i : unique){
					        	String uriUpdateFeeds = "feed/comment?write=" + MainNavActivity.getlastWriteFeedId() + "&categorised=" + MainNavActivity.getlastCategorisedFeedId() + "&network=" + i.intValue();
								String typeStr = "0";	
								
								GetFeed feedRequest = new GetFeed();
								feedRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, typeStr, uriUpdateFeeds);
					        }
			        	}   
				        
			        }
					return;
				}
			});
	        
	        selectFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.getNumFeeds() == 0){
						addFeedManuallyFrame.setVisibility(View.GONE);
						addFeedGpsFrame.setVisibility(View.GONE);
			        	noFeedsSelected.setVisibility(View.VISIBLE);
			        	feedFrame.setVisibility(View.GONE);
				        selectFeedFrame.setVisibility(View.GONE);
				        receiveFeedFrame.setVisibility(View.GONE);
				        feedResultsFrame.setVisibility(View.GONE);
				        noFeedResultsFrame.setVisibility(View.GONE);
			        }else{
			        	addFeedManuallyFrame.setVisibility(View.GONE);
						addFeedGpsFrame.setVisibility(View.GONE);
			        	noFeedsSelected.setVisibility(View.GONE);
						feedFrame.setVisibility(View.GONE);
						selectFeedFrame.setVisibility(View.VISIBLE);
				        receiveFeedFrame.setVisibility(View.GONE);
				        feedResultsFrame.setVisibility(View.GONE);
				        noFeedResultsFrame.setVisibility(View.GONE);
			        }
					return;
				}
			});
	        
	        receiveFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addFeedManuallyFrame.setVisibility(View.GONE);
					addFeedGpsFrame.setVisibility(View.GONE);
					feedFrame.setVisibility(View.GONE);
					selectFeedFrame.setVisibility(View.GONE);
			        receiveFeedFrame.setVisibility(View.VISIBLE);
			        noFeedsSelected.setVisibility(View.GONE);
			        return;
				}
			});
	        
	        
	        
	        
	        locationButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addFeedGpsFrame.setVisibility(View.VISIBLE);
					addFeedManuallyFrame.setVisibility(View.GONE);
					feedFrame.setVisibility(View.GONE);
					selectFeedFrame.setVisibility(View.GONE);
			        receiveFeedFrame.setVisibility(View.GONE);
			        noFeedsSelected.setVisibility(View.GONE);
			        return;
				}
			});
	        
	        manualButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addFeedManuallyFrame.setVisibility(View.VISIBLE);
					addFeedGpsFrame.setVisibility(View.GONE);
					feedFrame.setVisibility(View.GONE);
					selectFeedFrame.setVisibility(View.GONE);
			        receiveFeedFrame.setVisibility(View.GONE);
			        noFeedsSelected.setVisibility(View.GONE);
			        return;
				}
			});
	        
	        addFeedSwitch.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String origin = originTextView.getText().toString();
					String destination = destinationTextView.getText().toString();
					originTextView.setText(destination);
					destinationTextView.setText(origin);
				}
			});
	        
	        
	        addManualFeed.setOnClickListener(new OnClickListener() {
	        	
				@Override
				public void onClick(View v) {
					if(originTextView.getText().toString().length() > 0 && destinationTextView.getText().toString().length() > 0 && originStopAdapter != null && destinationStopAdapter != null) {
						boolean readyStart = false;
						boolean readyEnd = false;
						String originString = originTextView.getText().toString();
						String destinationString = destinationTextView.getText().toString();
						
						Stop origin = new Stop();
						origin.setName(originString);
						Stop destination = new Stop();
						destination.setName(destinationString);
						
						if(originStopAdapter.getItem(origin) == null && originStopAdapter.getCount() > 0) {
							origin = new Stop(originStopAdapter.getItem(0));
							originTextView.setText(origin.toString());
							readyStart = true;
						}
						else if(originStopAdapter.getItem(origin) != null) {
							origin = new Stop(originStopAdapter.getItem(origin));
							originTextView.setText(origin.toString());
							readyStart = true;
						}
						
						if(destinationStopAdapter.getItem(destination) == null && destinationStopAdapter.getCount() > 0) {
							destinationTextView.setText(destinationStopAdapter.getItem(0).toString());
							destination = new Stop(destinationStopAdapter.getItem(0));
							readyEnd = true;
						}
						else if(destinationStopAdapter.getItem(destination) != null) {
							destination = new Stop(destinationStopAdapter.getItem(destination));
							destinationTextView.setText(destination.toString());
							readyEnd = true;
						}
						
						if(readyStart == true && readyEnd == true) {
							final String uriPlanJourney = "journey/search?origin=" + origin.getName().replace(" ", "%20") + "&destination=" + destination.getName().replace(" ", "%20");
							
							String typeStr = "0";
							addFeedManuallyFrame.setVisibility(View.GONE);
							feedResultsFrame.setVisibility(View.VISIBLE);
							
							SearchFeedTask searchFeed = new SearchFeedTask();
							searchFeed.execute(typeStr, uriPlanJourney);	
		            		Log.d("PLANJOURNEY", "pathsResults " + MainNavActivity.getFeedResults().size());
		            		
		            			
						}
					}
				}
			});
	        
	       
	        
	        final Button goBackButton = (Button) view.findViewById(R.id.goBackButtonFeed);
	        goBackButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					noFeedResultsFrame.setVisibility(View.GONE);
					addFeedManuallyFrame.setVisibility(View.VISIBLE);
					
				}
			});
	        
	        //VER QUAIS ESTÃO SELECCIONADOS E SUBSCREVER A FEED
	        feedResultList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					feedResultAdapter.setSelectedRows(position);
					
				}
			});
	        
	        final Button manualResultsAddFeed = (Button) view.findViewById(R.id.manualResultsAddFeed);
	        manualResultsAddFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ArrayList<Boolean> selectedRows = feedResultAdapter.getSelectedRows();
	        		MainNavActivity.feedsSubscribed = new ArrayList<JourneyPath>();
	        		for(int i = 0; i < selectedRows.size(); i++) {
	        			Log.d("entrou", selectedRows.get(i).toString());
	        			if(selectedRows.get(i)) {
	        				MainNavActivity.feedsSubscribed.add(MainNavActivity.feedResults.get(i));
	        			}
	        		}
	        		//Checks if the user chose an option
	        		if(!MainNavActivity.feedsSubscribed.isEmpty()) {
						MainNavActivity.setNumFeeds(MainNavActivity.feedsSubscribed.size());
						feedResultsFrame.setVisibility(View.GONE);
						selectFeed.setChecked(true);
						selectFeedFrame.setVisibility(View.VISIBLE);
						selectFeedAdapter.updateList(MainNavActivity.feedsSubscribed);
						
	        		}
	        		else {
	        			Toast.makeText(getActivity(), getString(R.string.no_feed_options_selected), Toast.LENGTH_SHORT).show();
	        		}
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
		class SearchFeedTask extends AsyncTask<String, String, String> {

		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        LinearLayout linlaHeaderProgress = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressFeedResults);
		        linlaHeaderProgress.setVisibility(View.VISIBLE);
		    }

		    
		    @Override
		    protected String doInBackground(String... params) {
		    	String typeStr = params[0];
		    	int type = Integer.parseInt(typeStr);
		    	String ret = "";
		    	
		    	Log.d("PLANJOURNEY", "DOINBACKGROUND - type= " + typeStr);
		    	
				if (isOnline()) {
	    			try {
	    				switch(type) {
	    					case 0:
	    						String uriPlanJourney= params[1];
	        					MainNavActivity.setFeedResults((ArrayList<JourneyPath>) CasApp.doGet(getActivity(), uriPlanJourney, MainNavActivity.headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType()));
	    						ret = "ok planjourney";
	    						for(JourneyPath j : MainNavActivity.feedResults){
	    							Log.d("cenas", j.getOrigin().getName() + "-"+ j.getDestination().getName());
	    						}
	    						Log.d("tamanho", String.valueOf(MainNavActivity.feedResults.size()));
	    						
	    						break;
	    					/*case 1:
	    						String uriGetJourneys = params[1];
	    						TabMain.journeysPlanned = AndroidApp.doGet(PlanJourney.this, uriGetJourneys, headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType());
	    						ret = "ok getjourneys";    						
	    						break;
	    					case 2:
	    						String uriSetJourneys = params[1];
	    						AndroidApp.doPost(PlanJourney.this, uriSetJourneys, headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType(), TabMain.journeysPlanned);
	    						ret = "ok setjourneys";    						
	    						break; */   						
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
				Log.d("PLANJOURNEY", "DOINBACKGROUND - ret= " + ret);
				return ret;
		    }
		    
			   @Override
		      protected void onPostExecute(String result) {
					super.onPostExecute(result);
					Log.d("PLANJOURNEY", "POSTEXECUTE - result= " + result);	
					
					//Checks if there was an exception
					if(!result.startsWith("ok")) {
						Log.d("PLANJOURNEY", "POSTEXECUTE - Exception");
						Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
					}
					else if(result.contains("planjourney")) {
						Log.d("PLANJOURNEY", "POSTEXECUTE - planjourney");
						
				        final LinearLayout feedResultsFrame = (LinearLayout) view.findViewById(R.id.FeedResultsFrame);
				        final LinearLayout noFeedResultsFrame = (LinearLayout) view.findViewById(R.id.noFeedResultsFrame);
				        final LinearLayout linlaHeaderProgressFeedResults = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressFeedResults);
						if(MainNavActivity.feedResults.size() > 0){
							linlaHeaderProgressFeedResults.setVisibility(View.GONE);
							feedResultAdapter.updateList(MainNavActivity.feedResults);
						}else{
							feedResultsFrame.setVisibility(View.GONE);
							linlaHeaderProgressFeedResults.setVisibility(View.GONE);
							noFeedResultsFrame.setVisibility(View.VISIBLE);
							
						}
						
						
						
						
	            		/*Intent newIntent = new Intent(PlanJourney.this.getParent(), PlanJourney_ChoosePaths.class);
	            		newIntent.putExtra("results", pathsResults);
	            		newIntent.putExtra("starttime", String.format("%d-%02d-%02d %02d:%02d:00", mYear, mMonth + 1, mDay, mHour, mMinute));
	            		TabGroupActivity parentActivity = (TabGroupActivity)PlanJourney.this.getParent();
	            		parentActivity.startChildActivity("PlanJourney_ChoosePaths", newIntent);   */
					}
					/*else if(result.contains("getjourneys")) {
						Log.d("PLANJOURNEY", "POSTEXECUTE - getjourneys " + TabMain.journeysPlanned.size());
						showAlert();	
					}
					else if(result.contains("setjourneys")) {
						Log.d("PLANJOURNEY", "POSTEXECUTE - setjourneys " + TabMain.journeysPlanned.size());					
					}		*/		
		      }
		}	
		
		
		class GetFeed extends AsyncTask<String, String, String> {

		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        LinearLayout header = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressFeed);
		        header.setVisibility(View.VISIBLE);
		    }

		    
		    @SuppressWarnings("unchecked")
			@Override
		    protected String doInBackground(String... params) {
		    	String typeStr = params[0];
		    	int type = Integer.parseInt(typeStr);
		    	String ret = "";
		    	
		    	Log.d("NEWSFEED", "UPLOADDATA DOINBACKGROUND - type= " + typeStr);
		    	
				if (isOnline()) {
	    			try {
	    				switch(type) {
	    					case 0:
	    						String uriUpdateNewsFeed= params[1];
	    						ArrayList<NewsFeed> t = CasApp.doGet(getActivity(), uriUpdateNewsFeed, MainNavActivity.headers, new TypeToken<ArrayList<NewsFeed>>() {}.getType());
	    						size--;
	    						if(MainNavActivity.getlastWriteFeedId() != 0 || MainNavActivity.getlastCategorisedFeedId() != 0)
	    							MainNavActivity.addNewsFeeds(t);
	    						else 
	    							MainNavActivity.setNewsFeeds(t);
	    						ret = "ok updatenewsfeed";
	    						Log.d("tamanho", String.valueOf(MainNavActivity.feed.size()));
	    						handler.sendEmptyMessage(2);
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
				Log.d("NEWSFEED", "UPLOADDATA DOINBACKGROUND - ret= " + ret);
				return ret;
		    }
		    
			   @Override
		      protected void onPostExecute(String result) {
					super.onPostExecute(result);				
					Log.d("NEWSFEED", "UPLOADDATA POSTEXECUTE - result= " + result);
					
					//Checks if there was an exception
					if(!result.startsWith("ok")) {
						Log.d("NEWSFEED", "UPLOADDATA POSTEXECUTE - Exception");
						Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
					}
					else if(result.contains("updatenewsfeed")) {
						Log.d("NEWSFEED", "UPLOADDATA POSTEXECUTE - updatenewsfeed is empty? " + MainNavActivity.feed.isEmpty() + " " + size);
						Log.d("tamanho 2", String.valueOf(MainNavActivity.feed.size()));
						if(size == 0){
							 feedAdapter.updateList(MainNavActivity.feed);
							 LinearLayout header = (LinearLayout) view.findViewById(R.id.linlaHeaderProgressFeed);
						     header.setVisibility(View.GONE);
						     LinearLayout feedFrame = (LinearLayout) view.findViewById(R.id.feedFrame);
						     feedFrame.setVisibility(View.VISIBLE);
						     if(refresh){
						    	 feedList.finishRefresh();
						    	 refresh = false;
						     }
						     
						}
					}
		      }
		}	
		
		
		private void refreshList(RefreshableListView list) {
			 Set<Integer> unique = new HashSet<Integer>();
		     unique.addAll(MainNavActivity.networksList);
		     size = unique.size();
		     refresh = true;
			 for(Integer i : unique){
	        	String uriUpdateFeeds = "feed/comment?write=" + MainNavActivity.getlastWriteFeedId() + "&categorised=" + MainNavActivity.getlastCategorisedFeedId() + "&network=" + i.intValue();
				String typeStr = "0";	
				GetFeed feedRequest = new GetFeed();
				feedRequest.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, typeStr, uriUpdateFeeds);
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
