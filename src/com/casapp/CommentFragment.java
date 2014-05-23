package com.casapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CommentFragment extends Fragment{
	 private String title;
	    private int page;

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
	        View view = inflater.inflate(R.layout.feed_fragment, container, false);
	        
	        
	        //TextView tvLabel = (TextView) view.findViewById(R.id.);
	        //tvLabel.setText(page + " -- " + title);
	        return view;
	    }
}