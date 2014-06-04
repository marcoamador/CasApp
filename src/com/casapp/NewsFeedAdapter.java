package com.casapp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import data.objects.NewsFeed;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {
	private List<NewsFeed> newsFeedsObjects = new ArrayList<NewsFeed>();
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
		//Log.d("getView", "p" + position);
		TextView user = (TextView) convertView.findViewById(R.id.userName);
		user.setText(newsFeedsObjects.get(position).getUser().getUsername());
		
		TextView origin = (TextView) convertView.findViewById(R.id.feedItemOrigin);
		TextView destination = (TextView) convertView.findViewById(R.id.feedItemDestination);
		TextView separator = (TextView) convertView.findViewById(R.id.separator2);
		separator.setText("");
		origin.setText(newsFeedsObjects.get(position).getLine().getLineName());
		destination.setText("");
		
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

	public void updateList(ArrayList<NewsFeed> objects) {
		newsFeedsObjects.clear();
		newsFeedsObjects.addAll(objects);
		notifyDataSetChanged();
		//Log.d("avisou", String.valueOf(newsFeedsObjects.size()));

	}

	
}