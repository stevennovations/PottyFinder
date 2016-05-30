package com.example.steven.pottyfinderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Jerrick on 3/16/2016.
 */
public class PopupAdapter implements GoogleMap.InfoWindowAdapter {
    private View popup=null;
    private LayoutInflater inflater=null;

    PopupAdapter(LayoutInflater inflater) {
        this.inflater=inflater;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {


        if( marker.getSnippet() != null) {
            popup=inflater.inflate(R.layout.popup, null);

            TextView tv=(TextView)popup.findViewById(R.id.title);

            String[] strList = marker.getSnippet().split("<_>");
            tv.setText(marker.getTitle());
            tv = (TextView) popup.findViewById(R.id.rating);
            tv.setText(strList[0]);
            tv = (TextView) popup.findViewById(R.id.access);
            tv.setText(strList[1]);
            tv = (TextView) popup.findViewById(R.id.type);
            tv.setText(strList[2]);
        }

        else{
            popup=inflater.inflate(R.layout.empty, null);
            TextView tv = (TextView) popup.findViewById(R.id.title);
            tv.setText(marker.getTitle());
        }


        return popup;
    }
}
