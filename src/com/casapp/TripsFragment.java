package com.casapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

public class TripsFragment extends Fragment{

	// newInstance constructor for creating fragment with arguments
	    public static TripsFragment newInstance() {
	    	TripsFragment tripsFragment = new TripsFragment();
	        Bundle args = new Bundle();
	        tripsFragment.setArguments(args);
	        return tripsFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.trips_fragment, container, false);
	        
	        final RadioButton favourites = (RadioButton) view.findViewById(R.id.favourites);
	        final RadioButton schedule = (RadioButton) view.findViewById(R.id.schedule);
	        
	        final LinearLayout tripsNotLoggedInFrame = (LinearLayout) view.findViewById(R.id.tripsNotLoggedInFrame);
	        final LinearLayout favouritesFrame = (LinearLayout) view.findViewById(R.id.favouritesFrame);
	        final LinearLayout scheduleFrame = (LinearLayout) view.findViewById(R.id.scheduleFrame);
	        final LinearLayout addFavouriteFrame = (LinearLayout) view.findViewById(R.id.addFavouriteFrame);
	        
	        Button tripsLoginButton = (Button) view.findViewById(R.id.tripsLoginButton);
	        Button tripsAddFavouriteButton = (Button) view.findViewById(R.id.addFavouriteButton);
	        Button tripsSearchFavouriteButton = (Button) view.findViewById(R.id.searchFavouriteButton);
	        
	        
	        
	        favourites.setChecked(true);
	        schedule.setChecked(false);
	        
	        if(MainNavActivity.isLoggedin()) {
	        	favouritesFrame.setVisibility(View.VISIBLE);
	            scheduleFrame.setVisibility(View.GONE);
	        }else
	        	tripsNotLoggedInFrame.setVisibility(View.VISIBLE);
	       
	        
	        
	        favourites.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.isLoggedin()){
						favouritesFrame.setVisibility(View.VISIBLE);
						scheduleFrame.setVisibility(View.GONE);
					}
			        else{
			        	tripsNotLoggedInFrame.setVisibility(View.VISIBLE);
			        	scheduleFrame.setVisibility(View.GONE);
			        }	
				}
			});
	        
	        schedule.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.isLoggedin()){
						scheduleFrame.setVisibility(View.VISIBLE);
						favouritesFrame.setVisibility(View.GONE);
					}
			        else{
			        	tripsNotLoggedInFrame.setVisibility(View.VISIBLE);
			        	scheduleFrame.setVisibility(View.GONE);
			        }
					
				}
			});
	        
	        tripsLoginButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
					startActivity(loginIntent);					
				}
			});
	        
	        tripsAddFavouriteButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					favouritesFrame.setVisibility(View.GONE);
					addFavouriteFrame.setVisibility(View.VISIBLE);
					
				}
			});
	        
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }

}
