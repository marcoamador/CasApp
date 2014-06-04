package com.casapp;

import java.util.ArrayList;
import java.util.List;

import data.objects.JourneyPath;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedResultsAdapter extends ArrayAdapter<JourneyPath> {
	int resource;
	private ArrayList<Boolean> selectedRows;
	public List<JourneyPath> content;
	
	public FeedResultsAdapter(Context context, int resource, List<JourneyPath> objects) {
		super(context, resource, objects);
		this.resource = resource;
		content = objects;
		selectedRows = new ArrayList<Boolean>();
		for(int i = 0; i < objects.size(); i++)
			selectedRows.add(false);
	}
	
	public void setSelectedRows(int position) {
		selectedRows.set(position, !selectedRows.get(position));
		notifyDataSetChanged();
	}
	
	public void updateList(List<JourneyPath> newlist) {
	    content.clear();
	    content.addAll(newlist);
	    selectedRows.clear();
	    for(int i = 0; i < newlist.size(); i++)
			selectedRows.add(false);
	    this.notifyDataSetChanged();
	}
	
	public ArrayList<Boolean> getSelectedRows() {
		return selectedRows;
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
        CheckBox jpChecked = (CheckBox)feedResultView.findViewById(R.id.checkbox);
 
        //Assign the appropriate data from the object
        jpOrigin.setText(jp.getOrigin().getName());
        jpDestination.setText(jp.getDestination().getName());
        jpChecked.setChecked(selectedRows.get(position));
        
        jpChecked.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				//cb.setSelected(!cb.isChecked());
				//setSelectedRows(position);
				cb.setChecked(false);
			}
		});
 
        return feedResultView;
    }
}
