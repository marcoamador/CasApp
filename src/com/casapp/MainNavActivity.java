package com.casapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;

import data.objects.NewsFeed;
import data.objects.User;


public class MainNavActivity extends FragmentActivity implements LocationListener{
	private static final String[] CONTENT = new String[] { "Feed", "Comment", "My Trips", "Profile" };
	private static final int[] ICONS = new int[] {
        R.drawable.perm_group_feed,
        R.drawable.perm_group_comment,
        R.drawable.perm_group_trips,
        R.drawable.perm_group_profile,
	};
	
	private static final String[] FEED_CONTENT = new String[] {"News Feed", "Selected Feeds", "Receive New Feed"};
	FragmentPagerAdapter adapterViewPager;
	
	
	
	
	/*private static String username = "";
	
	private static int userPoints = 0;
	private static String generatedUsername = "";
	private static boolean anonymousMode = false;*/
	private static boolean checkedIn = false;
	private static int numFeeds = 0;
	private static boolean loggedIn = false;

	//
	
	protected LocationManager locationManager = null;
	protected Location lastLocation = null;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	public static Bundle bundleToSend = new Bundle();
	public static long GPS_MIN_TIME = 0;//miliseconds
	public static float GPS_MIN_DISTANCE = 0;//meters
	
	Dialog dialog;
	HashMap<String, Object> headers = new HashMap<String, Object>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_nav);
		
		//GPS START
	    this.locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
		
		//Fill request headers
		SharedPreferences pref = getSharedPreferences(CasApp.PREFS_NAME,MODE_PRIVATE);
		headers.put("username", pref.getString(CasApp.PREF_USERNAME, ""));
		headers.put("dateLogin", pref.getString(CasApp.PREF_DATELOGIN, ""));
		headers.put("authToken", pref.getString(CasApp.PREF_AUTHTOKEN, ""));
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
	}
	
	protected void onRestart() {
    	super.onRestart();
    	Log.d("TABMAIN", "TABMAIN: onRestart");
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
    }

    protected void onResume() {
    	super.onResume();
    	Log.d("TABMAIN", "TABMAIN: onResume");
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
    }

    protected void onPause() {
    	super.onPause();
    	Log.d("TABMAIN", "TABMAIN: onPause");
    	locationManager.removeUpdates(this);
    }

    protected void onStop() {
    	super.onStop();
    	Log.d("TABMAIN", "TABMAIN: onStop");
    	locationManager.removeUpdates(this);
    }
		
	class GoogleMusicAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
        	switch(position){
        	case 0:
        		return FeedFragment.newInstance();
        	case 1:
        		return CommentFragment.newInstance();
        	case 2:
        		return TripsFragment.newInstance();
        	case 3:
        		return ProfileFragment.newInstance();
        	default:
        		return null;
        	}
            
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase(Locale.getDefault());
        }

        @Override public int getIconResId(int index) {
          return ICONS[index];
        }

      @Override
        public int getCount() {
          return CONTENT.length;
        }
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    
	    MenuItem login = menu.findItem(R.id.action_login);
	    MenuItem logout = menu.findItem(R.id.action_logout);
	    MenuItem settings = menu.findItem(R.id.action_settings);
	    MenuItem checkout = menu.findItem(R.id.action_checkout);
	    
	    checkout.setVisible(false);
	    
	    if(isLoggedin()){
	    	login.setVisible(false);
	    }else{
	    	logout.setVisible(false);
	    	settings.setVisible(false);
	    	
	    }
	    
	    if(checkedIn){
	    	checkout.setVisible(true);
	    }
	    
	    settings.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
				startActivity(intent);
				return true;
				
			}
		});
	    
	    login.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
				startActivity(loginIntent);
				return true;
			}
		});
	    return super.onCreateOptionsMenu(menu);
	}

	public static boolean isLoggedin() {
		return loggedIn;
	}
	
	public static void setLoggedin(boolean bool){
		loggedIn = true;
	}
	
	public static boolean isCheckedIn(){
		return checkedIn;
	}
	
	public static void setCheckedIn(boolean bool){
		checkedIn=true;
	}

	public static int getNumFeeds() {
		return numFeeds;
	}

	public static void setNumFeeds(int numFeeds) {
		MainNavActivity.numFeeds = numFeeds;
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
						//fillList();
						break;
					case 1:
						Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
						break;
					case 2:
						Toast.makeText(getApplicationContext(), getString(R.string.checkout_successful), Toast.LENGTH_SHORT).show();
						break;
					case 3:
						Toast.makeText(getApplicationContext(), getString(R.string.logout_successful), Toast.LENGTH_SHORT).show();
						break;
					case 4:
						//REFRESH
						break;
					default:
						break;
				}
			}
		};
	
	
	
	
	
	
	
	
	
	class UploadData extends AsyncTask<String, String, String> {

	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }

	    
	    @Override
	    protected String doInBackground(String... params) {
	    	String typeStr = params[0];
	    	int type = Integer.parseInt(typeStr);
	    	String ret = "";
	    	
	    	Log.d("UPLOADDATA", "DOINBACKGROUND - type= " + typeStr);
	    	
			if (isOnline()) {
				try {
					switch(type) {
						case 0:
							String uriCheckOut = params[1];
							CasApp.doPost(getApplicationContext(), uriCheckOut, headers, new TypeToken<Integer>() {}.getType(), null);
							handler.sendEmptyMessage(2);
							ret = "ok checkout";
							break;
						case 1://LOGOUT
							String uriLogout = params[1];
							CasApp.doPost(getApplicationContext(), uriLogout, headers, new TypeToken<User>() {}.getType(), null);
							handler.sendEmptyMessage(3);
							ret = "ok logout";
							break;
						case 2://FEED
							String uriUpdateNewsFeed= params[1];
							ArrayList<NewsFeed> t = CasApp.doGet(getApplicationContext(), uriUpdateNewsFeed, headers, new TypeToken<ArrayList<NewsFeed>>() {}.getType());
							/*if(TabMain.getlastWriteFeedId() != 0 || TabMain.getlastCategorisedFeedId() != 0)
								TabMain.addNewsFeeds(t);
							else 
								TabMain.setNewsFeeds(t);*/
							ret = "ok updatenewsfeed";
							handler.sendEmptyMessage(4);
							break;
						case 3://RATE
							/*String uriUpdateRate= params[1];
							ArrayList<CommentCategorised> r = AndroidApp.doGet(getApplicationContext(), uriUpdateRate, headers, new TypeToken<ArrayList<CommentCategorised>>() {}.getType());
							if(TabMain.getLastRateId() != 0)
								TabMain.addRate(r);
							else 
								TabMain.setRate(r);
							ret = "ok updaterate";*/
							handler.sendEmptyMessage(4);
							break;
						case 4://JOURNEY
							/*String uriGetJourneys = params[1];
							journeysPlanned = AndroidApp.doGet(getApplicationContext(), uriGetJourneys, headers, new TypeToken<ArrayList<JourneyPath>>() {}.getType());
							ret = "ok getjourneys";*/
							break;    						
						default:
							if(type == 1)
								return "logout - Default";
							return "Default";
					}
				}
				catch(Exception e) {
					handler.sendEmptyMessage(0);
					if(type == 1) {//logout
						handler.sendEmptyMessage(3);
						return "logout - " + e.getMessage();
					}
					return e.getMessage();
				}
			}
			else {
	    		
	    		if(type == 1) {//logout
	    			handler.sendEmptyMessage(3);
	    			return "logout - ok";
	    		}
	    		handler.sendEmptyMessage(1);
	    		return "ok";
			}
			Log.d("UPLOADDATA", "DOINBACKGROUND - ret= " + ret);
			return ret;
	    }
	    
		   @Override
	      protected void onPostExecute(String result) {
				super.onPostExecute(result);
				Log.d("TABMAIN", "UPLOADDATA POSTEXECUTE - result= " + result);
				if(dialog != null)
					dialog.dismiss();
				
				//Checks if there was an exception
				if(result.contains("logout")) {
					SharedPreferences pref = getSharedPreferences(CasApp.PREFS_NAME,MODE_PRIVATE);
					pref.edit().remove(CasApp.PREF_CHECKIN).remove(CasApp.PREF_AUTHTOKEN).remove(CasApp.PREF_DATELOGIN)
						.remove(CasApp.PREF_LATITUDE).remove(CasApp.PREF_LONGITUDE).commit();
					
					
					//Goes back to the login activity
					Intent mainIntent = new Intent(getApplicationContext(), MainNavActivity.class);
					mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP  );  
					startActivity(mainIntent);  
					finish();  
					
				}
				else if(!result.startsWith("ok")) {
					Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
				}
				else if(result.contains("checkout")) {
					SharedPreferences pref = getSharedPreferences(CasApp.PREFS_NAME,MODE_PRIVATE);
					pref.edit().putBoolean(CasApp.PREF_CHECKIN, false).commit();
				}
				else if(result.contains("updatenewsfeed")) {					
					/*if(getTabHost().getCurrentTabTag().equals(TAB_FEED_TAG)) {
						TabNewsFeed tabAct = (TabNewsFeed)getLocalActivityManager().getCurrentActivity();
						NewsFeedAct act = (NewsFeedAct)tabAct.getCurrentActivity();
						//act.newsFeeds = new ArrayList<NewsFeed>(TabMain.getNewsFeeds());
						//act.lastFeedId = TabMain.getLastFeedId();
						act.fillGUI();
					}*/
				}
				else if(result.contains("updaterate")) {
					
					/*if(getTabHost().getCurrentTabTag().equals(TAB_RATE_TAG)) {
						TabRate tabAct = (TabRate)getLocalActivityManager().getCurrentActivity();
						RateAct act = (RateAct)tabAct.getCurrentActivity();
						act.fillGUI();
					}*/
				}
				else if(result.contains("getjourneys")) {
					/*if(getTabHost().getCurrentTabTag().equals(TAB_JOURNEY_TAG)) {
						TabPlanJourney tabAct = (TabPlanJourney)getLocalActivityManager().getCurrentActivity();
						PlanJourney act = (PlanJourney)tabAct.getCurrentActivity();
						act.showAlert();
					}*/					
				}					
	      }
	}
	
	protected boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
	    boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}
	
	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}
	
	
	
	
	
	//Utilities
	
		//---------- IS ONLINE -------------
		//Checks if there is internet connection
		private boolean isOnline() {		
			ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			
			if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected())
				return true;
			else return false;
		}

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}	
		
	

}
