package com.casapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class SettingsActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
		
        
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    
	    MenuItem login = menu.findItem(R.id.action_login);
	    MenuItem logout = menu.findItem(R.id.action_logout);
	    MenuItem settings = menu.findItem(R.id.action_settings);
	    
	    if(MainNavActivity.isLoggedin()){
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
}
