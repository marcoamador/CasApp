package com.casapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
	        
	        RadioGroup radio = (RadioGroup) view.findViewById(R.id.rdogrp_trips);
	        final RadioButton favourites = (RadioButton) view.findViewById(R.id.favourites);
	        final RadioButton schedule = (RadioButton) view.findViewById(R.id.schedule);
	        
	        favourites.setChecked(true);
	        schedule.setChecked(false);
	        
	        
	        favourites.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					favourites.setChecked(true);
					schedule.setChecked(false);
				}
			});
	        
	        schedule.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					favourites.setChecked(false);
					schedule.setChecked(true);
				}
			});
	        
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }

}
