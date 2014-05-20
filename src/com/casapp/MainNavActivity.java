package com.casapp;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main_nav);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");

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
        		return NavFragment.newInstance(CONTENT[position % CONTENT.length]);
        	case 2:
        		return NavFragment.newInstance(CONTENT[position % CONTENT.length]);
        	case 3:
        		return NavFragment.newInstance(CONTENT[position % CONTENT.length]);
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

}
