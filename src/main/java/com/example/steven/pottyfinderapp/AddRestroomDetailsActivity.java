package com.example.steven.pottyfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

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

public class AddRestroomDetailsActivity extends AppCompatActivity {

    private CheckBox b1; 
    private CheckBox b2; 
    private CheckBox b3; 
    private CheckBox b4; 
    private CheckBox b5; 
    private CheckBox b6; 

    private RadioGroup r1; 
    private RadioGroup r2; 
    private EditText e1; 
    private CardView c1;
    double lat = 0;
    double lng = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restroom_details);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        getSupportActionBar().setTitle("Add Potty Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        b1 = (CheckBox) findViewById(R.id.add_benefits_dryer);
        b2 = (CheckBox) findViewById(R.id.add_benefits_diaper_sec);
        b3 = (CheckBox) findViewById(R.id.add_benefits_soap);
        b4 = (CheckBox) findViewById(R.id.add_benefits_dispenser);
        b5 = (CheckBox) findViewById(R.id.add_benefits_bidet);
        b6 = (CheckBox) findViewById(R.id.add_benefits_tissue);

        r1 = (RadioGroup) findViewById(R.id.add_privacy_group);
        r2 = (RadioGroup) findViewById(R.id.add_gender_preference_group);
        e1 = (EditText) findViewById(R.id.add_name);
        c1 = (CardView) findViewById(R.id.add_cv_layout);

        lat = getIntent().getExtras().getDouble("lat");
        lng = getIntent().getExtras().getDouble("long");

        String ltlg = lat + " " + lng;

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = e1.getText().toString();
                String access = "";
                String type = "";

                String username = sharedPreferences.getString(LoginActivity.USERNAME, null);
                //BigInteger bigi = BigInteger.valueOf(Long.parseLong(username));

                int pos = r1.getCheckedRadioButtonId();
                View v1 = r1.findViewById(pos);
                int pos2 = r1.indexOfChild(v1);

                int pos3 = r2.getCheckedRadioButtonId();
                View v2 = r2.findViewById(pos3);
                int pos4 = r2.indexOfChild(v2);

                if(pos2 == 0){
                    access = "private";
                }
                else{
                    access = "public";
                }

                if(pos4 == 0){
                    type = "Unisex";
                }
                else{
                    type = "Male&Female";
                }

                Restroom rest = new Restroom(name, access, type, username, lat, lng);

                if(b1.isChecked()){
                    rest.addBenefit(b1.getText().toString());
                }
                else{
                    rest.addBenefit("null");
                }
                if(b2.isChecked()){
                    rest.addBenefit(b2.getText().toString());
                }
                else{
                    rest.addBenefit("null");
                }
                if(b3.isChecked()){
                    rest.addBenefit(b3.getText().toString());
                }
                else{
                    rest.addBenefit("null");
                }
                if(b4.isChecked()){
                    rest.addBenefit(b4.getText().toString());
                }
                else{
                    rest.addBenefit("null");
                }
                if(b5.isChecked()){
                    rest.addBenefit(b5.getText().toString());
                }
                else{
                    rest.addBenefit("null");
                }
                if(b6.isChecked()){
                    rest.addBenefit(b6.getText().toString());

                }
                else{
                    rest.addBenefit("null");
                }

                Gson gsn = new Gson();
                String jsondata = gsn.toJson(rest);

                new Helper().execute(jsondata);
            }
        });


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public class Helper extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            String url = ConnectionE.ADDER; //addlink

            Response response = null;

            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "arestroom")
                    .add("restroom", params[0])
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
                finish();
                MapsActivity.fa.finish();
            }
        }
    }
}
