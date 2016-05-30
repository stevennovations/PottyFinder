package com.example.steven.pottyfinderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Miguel on 3/17/2016.
 */
public class AddedActivityAdapter extends RecyclerView.Adapter<AddedActivityAdapter.AddedActivityHolder> {

    ArrayList<Restroom> arr;

    public AddedActivityAdapter(ArrayList<Restroom> arr){
        this.arr = arr;
    }

    @Override
    public AddedActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.potty_added, null);

        return new AddedActivityHolder(v);
    }

    @Override
    public void onBindViewHolder(AddedActivityHolder holder, final int position) {
        holder.textview.setText(arr.get(position).getRname());
        holder.clayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "";
                s += arr.get(position).getPid();
                Intent i = new Intent(v.getContext(), ViewRestroomActivity.class);
                i.putExtra("pid", arr.get(position).getPid());
                i.putExtra("ptn", arr.get(position).getRname());
                i.putExtra("lat", arr.get(position).getRlat());
                i.putExtra("lng", arr.get(position).getRlong());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class AddedActivityHolder extends RecyclerView.ViewHolder{
        TextView textview;
        RelativeLayout rlayout;
        CardView clayout;

        public AddedActivityHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.viewadded_name);
            rlayout = (RelativeLayout) itemView.findViewById(R.id.viewadded_rl_layout);
            clayout = (CardView) itemView.findViewById(R.id.viewadded_cv_layout);
        }
    }
}
