package com.example.steven.pottyfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.LoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String name, userid;
    private Intent i;

    private ImageView imageView;
    private TextView username;
    private CardView cardv;
    private TextView tview;
    private CardView cardv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardv = (CardView)findViewById(R.id.home_potty_search);
        tview = (TextView)findViewById(R.id.home_pottyday_name);
        cardv2 = (CardView) findViewById(R.id.home_pottyday_layout);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerlayout = navigationView.getHeaderView(0);

        //
        imageView = (ImageView) headerlayout.findViewById(R.id.imageView);
        username = (TextView) headerlayout.findViewById(R.id.nav_name);

        i = getIntent();

        if(i != null){
            name = i.getStringExtra("name");
            username.setText(name);
            //imageView.setImageURI(Uri.parse(i.getStringExtra("pic")));
            userid = i.getStringExtra("userid");
        }

        cardv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SearchPottyActivity.class);
                startActivity(i);
            }
        });
        cardv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), ViewRestroomActivity.class);
                i.putExtra("pid", 1001);
                i.putExtra("ptn", "Gokongwei");
                i.putExtra("lat", 14.56425150);
                i.putExtra("lng", 120.99317420);
                v.getContext().startActivity(i);
            }
        });

        System.out.println(name + userid);
        new Helper().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            Intent i = new Intent(getBaseContext(), SearchPottyActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_add) {
            Intent i = new Intent(getBaseContext(), MapsActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_view_added){
            Intent i = new Intent(getBaseContext(), ViewAddedActivity.class);
            i.putExtra("userid",userid);
            startActivity(i);
        } else if (id == R.id.nav_set) {
            Intent i = new Intent(getBaseContext(), LoginActivity.class);
            LoginManager.getInstance().logOut();
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
            editor.clear();
            editor.commit();
            startActivity(i);
            finish();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class Helper extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            OkHttpClient client = new OkHttpClient();

            String url = ConnectionE.GETTER;

            Response response = null;

            RequestBody requestBody = new FormBody.Builder()
                    .add("type", "grestroom")
                    .add("rid", "1001")
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

            ArrayList<String> students = new ArrayList();
            try
            {
                JSONArray studentArray = new JSONArray(s);

                for( int i = 0; i < studentArray.length(); i++ )
                {
                    JSONObject student = studentArray.getJSONObject(i);

                    String str = student.getString("ptn");
                    tview.setText(str);
                    //String rating = student.getString("ptc");
                    //String access = student.getString("pta");
                    //String type = student.getString("ptrt");
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
