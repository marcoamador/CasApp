package com.casapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FeedFragment extends Fragment{

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
	        View view = inflater.inflate(R.layout.feed_fragment, container, false);
	        
	        final RadioButton viewFeed = (RadioButton) view.findViewById(R.id.viewFeed);
	        final RadioButton selectFeed = (RadioButton) view.findViewById(R.id.selectFeed);
	        final RadioButton receiveFeed = (RadioButton) view.findViewById(R.id.receiveFeed);
	        
	        final LinearLayout feedFrame = (LinearLayout) view.findViewById(R.id.feedFrame);
	        final LinearLayout selectFeedFrame = (LinearLayout) view.findViewById(R.id.selectFeedFrame);
	        final LinearLayout receiveFeedFrame = (LinearLayout) view.findViewById(R.id.receiveFeedFrame);
	        
	        final LinearLayout noFeedsSelected = (LinearLayout) view.findViewById(R.id.noFeedsSelected);
	        
	        viewFeed.setChecked(true);
	        selectFeed.setChecked(false);
	        receiveFeed.setChecked(false);
	        
	        if(MainNavActivity.getNumFeeds() == 0){
	        	noFeedsSelected.setVisibility(View.VISIBLE);
	        	feedFrame.setVisibility(View.GONE);
		        selectFeedFrame.setVisibility(View.GONE);
		        receiveFeedFrame.setVisibility(View.GONE);
	        }else{
	        	noFeedsSelected.setVisibility(View.GONE);
	        	feedFrame.setVisibility(View.VISIBLE);
				selectFeedFrame.setVisibility(View.GONE);
		        receiveFeedFrame.setVisibility(View.GONE);
	        }
	        
	        viewFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.getNumFeeds() == 0){
			        	noFeedsSelected.setVisibility(View.VISIBLE);
			        	feedFrame.setVisibility(View.GONE);
				        selectFeedFrame.setVisibility(View.GONE);
				        receiveFeedFrame.setVisibility(View.GONE);
			        }else{
			        	noFeedsSelected.setVisibility(View.GONE);
			        	feedFrame.setVisibility(View.VISIBLE);
						selectFeedFrame.setVisibility(View.GONE);
				        receiveFeedFrame.setVisibility(View.GONE);
			        }
				}
			});
	        
	        selectFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(MainNavActivity.getNumFeeds() == 0){
			        	noFeedsSelected.setVisibility(View.VISIBLE);
			        	feedFrame.setVisibility(View.GONE);
				        selectFeedFrame.setVisibility(View.GONE);
				        receiveFeedFrame.setVisibility(View.GONE);
			        }else{
			        	noFeedsSelected.setVisibility(View.GONE);
						feedFrame.setVisibility(View.GONE);
						selectFeedFrame.setVisibility(View.VISIBLE);
				        receiveFeedFrame.setVisibility(View.GONE);
			        }
				}
			});
	        
	        receiveFeed.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					feedFrame.setVisibility(View.GONE);
					selectFeedFrame.setVisibility(View.GONE);
			        receiveFeedFrame.setVisibility(View.VISIBLE);
			        noFeedsSelected.setVisibility(View.GONE);
				}
			});
	        
	        
	        //Listeners do separador Receive New Feed
	        Button locationButton = (Button) view.findViewById(R.id.AddFeedUseLocation);
	        Button manualButton = (Button) view.findViewById(R.id.AddFeedManual);
	        
	        final LinearLayout addFeedGpsFrame = (LinearLayout) view.findViewById(R.id.gpsResultsFrame);
	        final LinearLayout addFeedManuallyFrame = (LinearLayout) view.findViewById(R.id.AddFeedManuallyFrame);
	        LinearLayout feedResultsFrame = (LinearLayout) view.findViewById(R.id.FeedResultsFrame);
	        
	        locationButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					addFeedGpsFrame.setVisibility(View.VISIBLE);
					addFeedManuallyFrame.setVisibility(View.GONE);
					feedFrame.setVisibility(View.GONE);
					selectFeedFrame.setVisibility(View.GONE);
			        receiveFeedFrame.setVisibility(View.GONE);
			        noFeedsSelected.setVisibility(View.GONE);
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
				}
			});
	        
	        
	        return view;
	    }
}
