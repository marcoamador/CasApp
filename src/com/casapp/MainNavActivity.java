package com.casapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.TabPageIndicator;


public class MainNavActivity extends FragmentActivity{
	private static final String[] CONTENT = new String[] { "Feed", "Comment", "My Trips", "Profile" };
	private static final int[] ICONS = new int[] {
        R.drawable.perm_group_feed,
        R.drawable.perm_group_comment,
        R.drawable.perm_group_trips,
        R.drawable.perm_group_profile,
	};
	
	private static final String[] FEED_CONTENT = new String[] {"News Feed", "Selected Feeds", "Receive New Feed"};
	FragmentPagerAdapter adapterViewPager;
	
	private static String username = "";
	private static boolean loggedIn = false;
	private static int userPoints = 0;
	private static String generatedUsername = "";
	private static boolean checkedIn = false;
	private static boolean anonymousMode = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_nav);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));

		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
	}
		
	class GoogleMusicAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
        	switch(position){
        	case 0:
        		return FeedFragment.newInstance(1, "cenas");
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
            return CONTENT[position % CONTENT.length].toUpperCase();
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
	    
	    if(isLoggedin()){
	    	login.setVisible(false);
	    }else{
	    	logout.setVisible(false);
	    	settings.setVisible(false);
	    	
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

}
