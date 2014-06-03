package com.casapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import data.objects.JourneyPath;

public class CheckInResultsAdapter extends ArrayAdapter<JourneyPath> {
	int resource;
	public List<JourneyPath> content;
	
	public CheckInResultsAdapter(Context context, int resource, List<JourneyPath> objects) {
		super(context, resource, objects);
		this.resource = resource;
		content = objects;
	}
	
	public void setSelectedRow(int position) {
		CommentFragment.selectedRow = position;
		notifyDataSetChanged();
	}
	
	public void updateList(List<JourneyPath> newlist) {
	    content.clear();
	    content.addAll(newlist);
	    this.notifyDataSetChanged();
	}
	
	public int getSelectedRow() {
		return CommentFragment.selectedRow;
	}	
	
	@Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LinearLayout feedResultView;
        //Get the current alert object
        JourneyPath jp = getItem(position);
        //Inflate the view
        if(convertView==null)
        {
        	feedResultView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, feedResultView, true);
        }
        else
        {
        	feedResultView = (LinearLayout) convertView;
        }
        
        TextView jpOrigin =(TextView)feedResultView.findViewById(R.id.origin);
        TextView jpDestination =(TextView)feedResultView.findViewById(R.id.destination);
        final CheckBox jpChecked = (CheckBox)feedResultView.findViewById(R.id.checkbox);
 
        //Assign the appropriate data from the object
        jpOrigin.setText(jp.getOrigin().getName());
        jpDestination.setText(jp.getDestination().getName());
        if(position == CommentFragment.selectedRow){
        	jpChecked.setChecked(true);
        }else
        	jpChecked.setChecked(false);
        
        jpChecked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				jpChecked.setChecked(false);
				
			}
		});
        
        
        
        
        return feedResultView;
    }
	
}
