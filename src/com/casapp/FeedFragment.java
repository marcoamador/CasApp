package com.casapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class FeedFragment extends Fragment{
	 private String title;
	    private int page;

	    // newInstance constructor for creating fragment with arguments
	    public static FeedFragment newInstance(int page, String title) {
	    	FeedFragment feedFragment = new FeedFragment();
	        Bundle args = new Bundle();
	        args.putInt("someInt", page);
	        args.putString("someTitle", title);
	        feedFragment.setArguments(args);
	        return feedFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        page = getArguments().getInt("someInt", 0);
	        title = getArguments().getString("someTitle");
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.feed_fragment, container, false);
	        
	        RadioGroup radio = (RadioGroup) view.findViewById(R.id.rdogrp);
	        final RadioButton viewFeed = (RadioButton) view.findViewById(R.id.viewFeed);
	        final RadioButton selectFeed = (RadioButton) view.findViewById(R.id.selectFeed);
	        final RadioButton receiveFeed = (RadioButton) view.findViewById(R.id.receiveFeed);
	        
	        viewFeed.setChecked(true);
	        selectFeed.setChecked(false);
	        receiveFeed.setChecked(false);
	        
	        viewFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        selectFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }
}
