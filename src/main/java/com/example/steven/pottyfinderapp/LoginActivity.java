package com.example.steven.pottyfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SignInButton signinbutton;
    LoginButton fblogin;
    CallbackManager cbm;
    ProfileTracker profileTracker;
    GoogleSignInOptions gso;
    Profile profile;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    GoogleSignInResult result;
    final static String USERNAME = "username";
    final static String NAME = "name";
    final static int RC_SIGN_IN = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_login);

        signinbutton = (SignInButton) findViewById(R.id.loginGoogleButton);
        signinbutton.setSize(SignInButton.SIZE_STANDARD);
        signinbutton.setTextAlignment(SignInButton.TEXT_ALIGNMENT_CENTER);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this  /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signinbutton.setScopes(gso.getScopeArray());

        fblogin = (LoginButton) findViewById(R.id.fblogin);

        cbm = CallbackManager.Factory.create();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String username = sharedPreferences.getString(USERNAME, null);

        if( username != null ){
            profile = Profile.getCurrentProfile();


            if( profile != null) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("name", profile.getName());
                i.putExtra("userid", profile.getId());
                startActivity(i);
                finish();
            }
            else{
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        }else {
            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {
                    // App code
                    if(currentProfile != null ) {
                        System.out.println("Name: " + currentProfile.getFirstName() + " " + currentProfile.getLastName());
                        System.out.println("ID: " + currentProfile.getId());

                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                        editor.putString(USERNAME, currentProfile.getId());
                        editor.putString(NAME, currentProfile.getName());
                        editor.commit();

                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        i.putExtra("name", currentProfile.getName());
                        i.putExtra("userid", currentProfile.getId());
                        startActivity(i);
                        finish();
                    }
                }
            };

            signinbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }

    }



    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            acct = result.getSignInAccount();
            if(acct!=null) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit();
                editor.putString(USERNAME, acct.getId());
                editor.putString(NAME, acct.getDisplayName());
                editor.commit();

                Intent i = new Intent(getBaseContext(), MainActivity.class);
                i.putExtra("name", acct.getDisplayName());
                i.putExtra("userid", acct.getId());
                startActivity(i);
                finish();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            }
        }
        else {
            cbm.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(profileTracker!=null)
            profileTracker.stopTracking();

    }

    private void getFbKeyHash(){
        PackageInfo info;
        try {

            info = getPackageManager().getPackageInfo(
                    "com.example.miguel.pottyfinderv20", PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashString = new String(Base64.encode(md.digest(), 0));
                System.out.println("App KeyHash : " + hashString);
            }
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}