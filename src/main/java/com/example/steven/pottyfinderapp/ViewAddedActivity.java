package com.example.steven.pottyfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;

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

public class ViewAddedActivity extends AppCompatActivity {

    RecyclerView rl;
    AddedActivityAdapter aaa;
    ArrayList<Restroom> arr;
    SharedPreferences sharedPreferences;
    String wtf;
    BigInteger big;


    /*
    CHANGE THE VALUE PLEASEE!!!!
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_added);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        big = BigInteger.valueOf(Long.parseLong("1166285546715890"));
        //Log.v("YOHHH", big.toString());
        Intent i = getIntent();

        wtf = i.getStringExtra("userid");

        rl = (RecyclerView) findViewById(R.id.viewadded_rl_layout);

        arr = new ArrayList();

        getSupportActionBar().setTitle("Added Potties");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        rl.setLayoutManager(
                new StaggeredGridLayoutManager(
                        3, StaggeredGridLayoutManager.VERTICAL));
        rl.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        //System.out.println("String : " + wtf + " Length : " + wtf.length() );

        new Helper().execute(big.toString());
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

            String username = sharedPreferences.getString(LoginActivity.USERNAME, null);


            String url = ConnectionE.GETTER;
            //String username = params[0];
            //client.setConnectTimeout(1000, TimeUnit.SECONDS);

            Response response = null;
            //Log.v("YOHHH2", params[0]);
            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "getAdded")
                    .add("uidd", params[0])
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
            try
            {
                Log.v("HELLO", s);
                JSONArray crArray = new JSONArray(s);
                if(crArray != null ) {
                    for (int i = 0; i < crArray.length(); i++) {
                        JSONObject student = crArray.getJSONObject(i);

                        String str = student.getString("ptn");
                        Log.v("Hello", str);
                        int cap = student.getInt("pid");

                        Restroom rr = new Restroom(cap, str);
                        rr.setRlat(student.getDouble("ptla"));
                        rr.setRlong(student.getDouble("ptlo"));
                        arr.add(rr);
                    }

                    aaa = new AddedActivityAdapter(arr);

                    rl.setAdapter(aaa);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
