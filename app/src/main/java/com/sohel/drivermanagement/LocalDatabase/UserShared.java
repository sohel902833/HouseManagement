package com.sohel.drivermanagement.LocalDatabase;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserShared {
    Activity activity;

    public UserShared(Activity activity) {
        this.activity = activity;
    }

    public void saveData(String homeAdded){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("homeAdded",homeAdded);
        editor.commit();
    }


    public String isHomeAded(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user",Context.MODE_PRIVATE);
        String homeAded=sharedPreferences.getString("homeAdded","false");
        return  homeAded;
    }

}
