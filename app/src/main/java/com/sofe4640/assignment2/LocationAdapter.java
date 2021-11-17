package com.sofe4640.assignment2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class LocationAdapter extends ArrayAdapter<LocationModel> {
    public LocationAdapter(Context context, int resource, List<LocationModel> locList) {
        super(context, resource, locList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView longText, latText, addrText;
        LocationModel locModel = getItem(position);

        //make view if non-existent
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_cell, parent, false);
        }

        addrText = (TextView) convertView.findViewById(R.id.cell_address);
        longText = (TextView) convertView.findViewById(R.id.cell_longitude);
        latText = (TextView) convertView.findViewById(R.id.cell_latitude);

        addrText.setText(locModel.getAddress());
        longText.setText("Long: " + locModel.getLongitude());
        latText.setText("Lat: " + locModel.getLatitude());

        return convertView;
    }
}
