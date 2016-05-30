package com.example.steven.pottyfinderapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Miguel on 3/16/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private ArrayList<Review> arr;

    public ReviewAdapter(ArrayList<Review> arr){
        this.arr = arr;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.potty_record, null);

        return new ReviewHolder(v);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        holder.tvname.setText(arr.get(position).getName());
        holder.tvdesc.setText(arr.get(position).getDesc());
        holder.tvrate.setText(arr.get(position).getRate());
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder{
        TextView tvname;
        TextView tvdesc;
        TextView tvrate;

        public ReviewHolder(View itemView) {
            super(itemView);
            tvname = (TextView) itemView.findViewById(R.id.record_name);
            tvdesc = (TextView) itemView.findViewById(R.id.record_text);
            tvrate = (TextView) itemView.findViewById(R.id.record_rate);
        }
    }
}
