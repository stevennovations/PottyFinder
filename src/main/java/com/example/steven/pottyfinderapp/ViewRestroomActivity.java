package com.example.steven.pottyfinderapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

public class ViewRestroomActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ReviewAdapter recyclerAdapter;
    private CardView reviewCard;
    private BigInteger fb_id;
    private int pottyid;
    private RatingBar rb1;
    private ArrayList<Review> arr;

    private CheckBox c1;
    private CheckBox c2;
    private CheckBox c3;
    private CheckBox c4;
    private CheckBox c5;
    private CheckBox c6;
    SharedPreferences sharedPreferences;
    private LatLng pos;
    private String str;
    private String str2;

    /*

    DONT FORGET TO CHANGE THE VALUES PART!!!!!!!

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_restroom);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = sharedPreferences.getString(LoginActivity.USERNAME, null);
        final String names = sharedPreferences.getString(LoginActivity.NAME, null);

        pottyid = getIntent().getExtras().getInt("pid");
        str = getIntent().getExtras().getString("ptn");
        pos = new LatLng(getIntent().getExtras().getDouble("lat"), getIntent().getExtras().getDouble("lng"));
        rb1 = (RatingBar)findViewById(R.id.view_ratingbar);

        c1 = (CheckBox)findViewById(R.id.view_benefits_dryer);
        c2 = (CheckBox)findViewById(R.id.view_benefits_diaper_sec);
        c3 = (CheckBox)findViewById(R.id.view_benefits_soap);
        c4 = (CheckBox)findViewById(R.id.view_benefits_dispenser);
        c5 = (CheckBox)findViewById(R.id.view_benefits_bidet);
        c6 = (CheckBox)findViewById(R.id.view_benefits_tissue);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.view_recycler);
        recyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(
                        3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        reviewCard = (CardView) findViewById(R.id.view_cv_layout);
        reviewCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteReviewDialog wrd = new WriteReviewDialog();
                Bundle args = new Bundle();
                args.putString("fbid", username);
                args.putInt("ptid", pottyid);
                args.putString("nme", names);
                wrd.setArguments(args);
                wrd.show(getFragmentManager(), "");
            }
        });

        new Helper().execute(String.valueOf(pottyid));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.view_map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setTitle(str);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //LatLng pos = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        LatLng philippines = new LatLng(14.5995, 120.9842);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(philippines));
        googleMap.addMarker(new MarkerOptions().position(pos).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)).title(str2));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(pos)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public class Helper extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            OkHttpClient client = new OkHttpClient();

            String url = ConnectionE.GETTER;
            //String username = params[0];
            //client.setConnectTimeout(1000, TimeUnit.SECONDS);

            Response response = null;

            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "grestroom")
                    .add("rid", params[0])
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
                //Toast.makeText(getBaseContext(), s, Toast.LENGTH_LONG).show();
                Log.v("HELLO", s);
                JSONArray cArr = new JSONArray(s);

                arr = new ArrayList();

                for(int i = 0; i < cArr.length(); i++){
                    JSONObject cr = cArr.getJSONObject(i);

                    if(cr.getJSONArray("belist") != null){
                        JSONArray arr1 = cr.getJSONArray("belist");

                        for(int j = 0; j < arr1.length(); j++){
                            if(arr1.get(j).equals("Dryer")){
                                c1.setChecked(true);
                                c1.setClickable(false);
                            }
                            else if(arr1.get(j).equals("Diaper Station")){
                                c2.setChecked(true);
                                c2.setClickable(false);
                            }
                            else if(arr1.get(j).equals("Soap")){
                                c3.setChecked(true);
                                c3.setClickable(false);
                            }
                            else if(arr1.get(j).equals("Dispenser")){
                                c4.setChecked(true);
                                c4.setClickable(false);
                            }
                            else if(arr1.get(j).equals("Bidet")){
                                c5.setChecked(true);
                                c5.setClickable(false);
                            }
                            else if(arr1.get(j).equals("Tissue")){
                                c6.setChecked(true);
                                c6.setClickable(false);
                            }
                        }
                        c1.setClickable(false);
                        c2.setClickable(false);
                        c3.setClickable(false);
                        c4.setClickable(false);
                        c5.setClickable(false);
                        c6.setClickable(false);
                    }


                    int rt = cr.getInt("ptc");
                    str2 = cr.getString("ptn");
                    pos = new LatLng(cr.getDouble("ptla"), cr.getDouble("ptlo"));

                    rb1.setRating(rt);
                    rb1.setIsIndicator(true);


                    if(cr.getJSONArray("comnts") != null){
                        JSONArray revs = cr.getJSONArray("comnts");

                        for(int x = 0; x < revs.length(); x++){
                            JSONObject obj = revs.getJSONObject(x);
                            obj.getString("userid");


                            arr.add(new Review(obj.getString("userid"), obj.getString("comment"), obj.getString("rts")));
                        }
                    }
                }

                recyclerAdapter = new ReviewAdapter(arr);
                recyclerView.setAdapter(recyclerAdapter);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
