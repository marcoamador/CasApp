package com.casapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
	        
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }

}
