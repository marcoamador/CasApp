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

public class CommentFragment extends Fragment{

	    // newInstance constructor for creating fragment with arguments
	    public static CommentFragment newInstance() {
	    	CommentFragment commentFragment = new CommentFragment();
	        Bundle args = new Bundle();
	        commentFragment.setArguments(args);
	        return commentFragment;
	    }

	    // Store instance variables based on arguments passed
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	    }

	    // Inflate the view for the fragment based on layout XML
	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        View view = inflater.inflate(R.layout.comment_fragment, container, false);
	        
	        LinearLayout notLoggedInFrame = (LinearLayout) view.findViewById(R.id.notLoggedInComment);
	        LinearLayout notCheckedInFrame = (LinearLayout) view.findViewById(R.id.notCheckedInComment);
	        
	        final Button commentLoginButton = (Button) view.findViewById(R.id.CommentLoginButton);
	        //Button searchOptionsButton = (Button) view.findViewById(R.id.)
	        
	        notLoggedInFrame.setVisibility(View.GONE);
	        notCheckedInFrame.setVisibility(View.GONE);
	        
	       /*if(!MainNavActivity.isLoggedin()){
	        	notLoggedInFrame.setVisibility(View.VISIBLE);
	        }else if(MainNavActivity.isLoggedin() && !MainNavActivity.isCheckedIn()){
	        	notLoggedInFrame.setVisibility(View.GONE);
	        	notCheckedInFrame.setVisibility(View.VISIBLE);
	        }*/
	        
	        commentLoginButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getActivity(), LoginActivity.class);
					startActivity(i);
					
				}
			});
	      
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }
}