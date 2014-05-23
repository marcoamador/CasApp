package com.casapp;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SettingsActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setTitle("CasApp");
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33B5E5")));
        
	}
}
