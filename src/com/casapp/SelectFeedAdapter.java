package com.casapp;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import data.objects.JourneyPath;
import data.objects.Network;

public class SelectFeedAdapter extends ArrayAdapter<JourneyPath> {
	int resource;
	private ArrayList<Boolean> activeRows;
	public List<JourneyPath> content;

	public SelectFeedAdapter(Context context, int textViewResourceId,
			List<JourneyPath> objects) {
		super(context, textViewResourceId, objects);
		this.resource = textViewResourceId;
		content = objects;
		activeRows = new ArrayList<Boolean>();
		for(int i = 0; i < objects.size(); i++)
			activeRows.add(true);
	}

	public void setActiveRows(int position) {
		activeRows.set(position, !activeRows.get(position));
	}
	
	public void updateList(List<JourneyPath> newlist) {
	    content.addAll(newlist);
	    for(int i = 0; i < newlist.size(); i++)
			activeRows.add(true);
	    for(JourneyPath j : newlist){
	    	for(Network n : j.getNetworks()){
	    		MainNavActivity.networksList.add(n.getNetworkId());
	    	}
	    }
	    this.notifyDataSetChanged();
	}
	
	public ArrayList<Boolean> getActiveRows() {
		return activeRows;
	}	
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout selectFeedView;
        //Get the current alert object
        JourneyPath jp = getItem(position);
 
        //Inflate the view
        if(convertView==null)
        {
        	selectFeedView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, selectFeedView, true);
        }
        else
        {
        	selectFeedView = (LinearLayout) convertView;
        }
        
        TextView jpOrigin =(TextView)selectFeedView.findViewById(R.id.feeds_origin);
        TextView jpDestination =(TextView)selectFeedView.findViewById(R.id.feeds_destination);
        Switch jpActive = (Switch)selectFeedView.findViewById(R.id.switchFeed);
 
        //Assign the appropriate data from the object
        jpOrigin.setText(jp.getOrigin().getName());
        jpDestination.setText(jp.getDestination().getName());
        jpActive.setChecked(activeRows.get(position));
 
        return selectFeedView;
    }

}
