package com.casapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import data.objects.NewsFeed;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {
	private List<NewsFeed> newsFeedsObjects;
	private SimpleDateFormat hourMinutesFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
	
	public NewsFeedAdapter(Context context, int textViewResourceId, List<NewsFeed> objects) {
		super(context, textViewResourceId, objects);
		newsFeedsObjects = objects;
	}		
	
	@Override
	public void add(NewsFeed object) {
		// TODO Auto-generated method stub
		super.add(object);
	}

	@Override
	public NewsFeed getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.feed_item, null);
		}
		
		SharedPreferences pref = getContext().getSharedPreferences(CasApp.PREFS_NAME,Context.MODE_PRIVATE);
		String username = pref.getString(CasApp.PREF_USERNAME, "");
		if(newsFeedsObjects.get(position).getUser().getUsername().equals(username)){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.my_feed_item, null);
		}else{
			Button likeButton = (Button) convertView.findViewById(R.id.feedItemLikeComment);
			Button dislikeButton = (Button) convertView.findViewById(R.id.feedItemDislikeComment);
			
			if(!MainNavActivity.isLoggedin()){
				likeButton.setVisibility(View.GONE);
				dislikeButton.setVisibility(View.GONE);
			}
		}
		
		
		
		//Log.d("getView", "p" + position);
		TextView user = (TextView) convertView.findViewById(R.id.userName);
		user.setText(newsFeedsObjects.get(position).getUser().getUsername());
		
		TextView origin = (TextView) convertView.findViewById(R.id.feedItemOrigin);
		TextView destination = (TextView) convertView.findViewById(R.id.feedItemDestination);
		TextView separator = (TextView) convertView.findViewById(R.id.separator2);
		origin.setText("Line");
		destination.setText(newsFeedsObjects.get(position).getLine().getLineName());
		
		TextView hour = (TextView) convertView.findViewById(R.id.feedItemTime);
		
		hour.setText(hourMinutesFormat.format(newsFeedsObjects.get(position).getDateDate()));
		
		TextView comment = (TextView) convertView.findViewById(R.id.feedItemComment);
		//If it is a written comment
		if(newsFeedsObjects.get(position).getCommentType() == 0) {
			comment.setText(newsFeedsObjects.get(position).getMessage());
		}
		//If it is a categorised comment
		else if(newsFeedsObjects.get(position).getCommentType() == 1) {
			
			int typeComment = 0;
			switch(newsFeedsObjects.get(position).getIdCategorisedType()) {
			case 1:
				typeComment = R.array.temperature_feed_array;
				break;
			case 2:
				typeComment = R.array.noise_feed_array;
				break;
			case 3:
				typeComment = R.array.crowding_feed_array;
				break;
			case 4:
				typeComment = R.array.seating_feed_array;
				break;
			case 5:
				typeComment = R.array.cleanliness_feed_array;
				break;
			case 6:
				typeComment = R.array.scenery_feed_array;
				break;
			case 7:
				typeComment = R.array.perceived_security_feed_array;
				break;
			case 8:
				typeComment = R.array.speed_feed_array;
				break;
			case 9:
				typeComment = R.array.progress_feed_array;
				break;
			case 19:
				typeComment = R.array.similar_feed_array;
				break;					
			case 11:
				typeComment = R.array.courtesy_feed_array;
				break;
			case 12:
				typeComment = R.array.smoothness_feed_array;
				break;							
			default:
				typeComment = R.array.temperature_feed_array;
				break;	
			}
		
			String[] rate_array = getContext().getResources().getStringArray(typeComment);
			comment.setText(rate_array[newsFeedsObjects.get(position).getDiscreteClassificationCategorisedComment() - 1]);			
			
		}
		else {
			comment.setText("");
		}
		
		return convertView;
	}

	@Override
	public void insert(NewsFeed object, int index) {
		// TODO Auto-generated method stub
		super.insert(object, index);
	}

	public void updateList(List<NewsFeed> objects) {
		Log.d("avisou", String.valueOf(objects.size()));
		newsFeedsObjects.clear();
	    newsFeedsObjects.addAll(objects);
		notifyDataSetChanged();
		Log.d("avisou", String.valueOf(newsFeedsObjects.size()));

	}

	
}