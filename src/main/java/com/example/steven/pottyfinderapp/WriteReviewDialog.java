package com.example.steven.pottyfinderapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigInteger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Miguel on 3/16/2016.
 */
public class WriteReviewDialog extends DialogFragment {

    private View v;
    private EditText et;
    private RatingBar rb;
    private BigInteger bg;
    private int ptid;
    private String name;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        v = LayoutInflater.from(getActivity()).inflate(R.layout.review_dialog, null);
        et = (EditText)v.findViewById(R.id.review_text);
        rb = (RatingBar)v.findViewById(R.id.review_rating);
        bg = BigInteger.valueOf(Long.valueOf(getArguments().getString("fbid")));
        Log.v("Hello", bg.toString());
        ptid = getArguments().getInt("ptid");
        name = getArguments().getString("nme");


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setCancelable(true)
                .setView(v)
                .setPositiveButton("Post", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(v.getContext(),"AHAHAHA", Toast.LENGTH_LONG).show();

                        int ribs = (int) rb.getRating();
                        String st = "";
                        st += ribs;
                        ReviewPass revp = new ReviewPass(et.getText().toString(), ptid, name, st);

                        Gson gsn = new Gson();
                        String jsondata = gsn.toJson(revp);
                        Log.v("Hello", jsondata);
                        new Helper().execute(jsondata);

                    }
                }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public class Helper extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            String url = ConnectionE.ADDER;
            //String username = params[0];
            //client.setConnectTimeout(1000, TimeUnit.SECONDS);

            Response response = null;

            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "areview")
                    .add("review", params[0])
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            int insert = Integer.parseInt(s);

            if(insert != 0){
                //Toast.makeText(getContext(), "Add successful", Toast.LENGTH_LONG).show();
            }
        }
    }
}
