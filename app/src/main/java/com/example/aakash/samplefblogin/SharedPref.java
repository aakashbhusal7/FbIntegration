package com.example.aakash.samplefblogin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SharedPref {
    private Activity activity;

    public SharedPref(Activity activity) {
        this.activity = activity;
    }

    public void saveAccessToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("fb_access_token", token);
        editor.apply(); // This line is IMPORTANT !!!
    }


    public String getToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("fb_access_token", null);
    }

    public void clearToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }

    public void saveFacebookUserInfo(String first_name,String last_name, String email, String gender, String profileURL){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("first_name", first_name);
        editor.putString("last_name", last_name);
        editor.putString("email", email);
        editor.putString("fb_profileURL", profileURL);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void getFacebookUserInfo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }


}
