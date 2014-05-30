package com.casapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ProfileFragment extends Fragment{

	// newInstance constructor for creating fragment with arguments
	    public static ProfileFragment newInstance() {
	    	ProfileFragment profileFragment = new ProfileFragment();
	        Bundle args = new Bundle();
	        profileFragment.setArguments(args);
	        return profileFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.profile_fragment, container, false);
	        
	        final RadioButton privacy = (RadioButton) view.findViewById(R.id.userStatistics);
	        final RadioButton rewards = (RadioButton) view.findViewById(R.id.claimRewards);
	        
	        final LinearLayout privacyFrame = (LinearLayout) view.findViewById(R.id.userStatisticsFrame);
	        final LinearLayout rewardsFrame = (LinearLayout) view.findViewById(R.id.claimRewardsFrame);
	        
	        privacy.setChecked(true);
	        rewards.setChecked(false);
	        privacyFrame.setVisibility(View.VISIBLE);
	        rewardsFrame.setVisibility(View.GONE);
	        Log.d("PROFILE", Boolean.toString(MainNavActivity.isLoggedin()));
	        
			privacy.setOnClickListener(new OnClickListener() {
							
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			        privacyFrame.setVisibility(View.VISIBLE);
			        rewardsFrame.setVisibility(View.GONE);
				}
			});

	        
	        
	        rewards.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
			        privacyFrame.setVisibility(View.GONE);
			        rewardsFrame.setVisibility(View.VISIBLE);
				}
			});
	        
	        
	        if(!MainNavActivity.isLoggedin()){
	        	view = inflater.inflate(R.layout.profile_not_logged, container, false);
	        	
	        	final Button loginButton = (Button) view.findViewById(R.id.profileLoginButton);
	        	loginButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
						startActivity(loginIntent);
						return;
					}
				});
	        }else{
	        	TextView username = (TextView) view.findViewById(R.id.profileUsername);
	        	TextView totalPoints = (TextView) view.findViewById(R.id.profileNumPoints);
	        	TextView feedbackPoints = (TextView) view.findViewById(R.id.feedbackNumPoints);
	        	SharedPreferences pref = this.getActivity().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
	        	username.setText(pref.getString(CasApp.PREF_USERNAME, "Undefined"));
	        	totalPoints.setText(pref.getString(CasApp.PREF_POINTS, "NaN"));
	        	feedbackPoints.setText(pref.getString(CasApp.PREF_FEEDBACK_POINTS, "NaN"));
	        }
	        
	        
	        
	        return view;
	    }

}
