package com.company.automaticfishfeederapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class LoginSession {

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_SESSION = "login_session";

    private static final String IS_LOGIN = "isLogin";

    public static final String KEY_USERID = "userId";

    public static final String KEY_MOBILENUMBER = "mobileNumber";

    public static final String KEY_EMAIL = "email";

    public static final String KEY_PROFILEPICTURE = "profilePicture";

    public static final String KEY_FIRSTNAME = "firstName";

    public static final String KEY_LASTNAME = "lastName";

    public static final String KEY_DEVICEID = "deviceId";

    public static final String KEY_ISADD = "isAdd";
    public static final String KEY_ISFISHFEEDING = "isFishFeeding";
    public static final String KEY_SCHEDULEID = "scheduleId";
    public static final String KEY_SCHEDULEUSERID = "scheduleUserId";
    public static final String KEY_SCHEDULETITLE = "scheduleTitle";
    public static final String KEY_SCHEDULETIME = "scheduleTime";
    public static final String KEY_SCHEDULETYPE = "scheduleType";
    public static final String KEY_ISACTIVE = "isActive";

    public LoginSession(Context context){
        this._context = context;
        sharedPreferences = _context.getSharedPreferences(PREF_SESSION, PRIVATE_MODE);
        editor = sharedPreferences.edit();

    }

    public void writeLoginSession(String userId, String firstName, String lastName, String email, String profilePicture,String mobileNumber,String deviceId){

        editor.putBoolean(IS_LOGIN, true);

        editor.putString(KEY_USERID, userId);
        editor.putString(KEY_FIRSTNAME, firstName);
        editor.putString(KEY_LASTNAME, lastName);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PROFILEPICTURE, profilePicture);
        editor.putString(KEY_MOBILENUMBER, mobileNumber);
        editor.putString(KEY_DEVICEID, deviceId);
        editor.commit();
    }

    public HashMap<String, String> readLoginSession(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERID, sharedPreferences.getString(KEY_USERID, null));
        user.put(KEY_FIRSTNAME, sharedPreferences.getString(KEY_FIRSTNAME, null));
        user.put(KEY_LASTNAME, sharedPreferences.getString(KEY_LASTNAME, null));
        user.put(KEY_EMAIL, sharedPreferences.getString(KEY_EMAIL, null));
        user.put(KEY_PROFILEPICTURE, sharedPreferences.getString(KEY_PROFILEPICTURE, null));
        user.put(KEY_MOBILENUMBER, sharedPreferences.getString(KEY_MOBILENUMBER,null));
        user.put(KEY_DEVICEID, sharedPreferences.getString(KEY_DEVICEID, null));

        return user;
    }

    public void writeScheduleSession(String scheduleId, String scheduleUserId, String scheduleTitle, String scheduleTime, String scheduleType,String isActive){

        editor.putString(KEY_SCHEDULEID, scheduleId);
        editor.putString(KEY_SCHEDULEUSERID, scheduleUserId);
        editor.putString(KEY_SCHEDULETITLE, scheduleTitle);
        editor.putString(KEY_SCHEDULETIME, scheduleTime);
        editor.putString(KEY_SCHEDULETYPE, scheduleType);
        editor.putString(KEY_ISACTIVE, isActive);
        editor.commit();
    }

    public HashMap<String, String> readScheduleSession(){
        HashMap<String, String> schedule = new HashMap<String, String>();

        schedule.put(KEY_SCHEDULEID, sharedPreferences.getString(KEY_SCHEDULEID, null));
        schedule.put(KEY_SCHEDULEUSERID, sharedPreferences.getString(KEY_SCHEDULEUSERID, null));
        schedule.put(KEY_SCHEDULETITLE, sharedPreferences.getString(KEY_SCHEDULETITLE, null));
        schedule.put(KEY_SCHEDULETIME, sharedPreferences.getString(KEY_SCHEDULETIME, null));
        schedule.put(KEY_SCHEDULETYPE, sharedPreferences.getString(KEY_SCHEDULETYPE, null));
        schedule.put(KEY_ISACTIVE, sharedPreferences.getString(KEY_ISACTIVE,null));

        return schedule;
    }

    public void writeActivitySession(String isAdd, String isFishFeeding){

        editor.putString(KEY_ISADD, isAdd);
        editor.putString(KEY_ISFISHFEEDING, isFishFeeding);
        editor.commit();
    }

    public HashMap<String, String> readActivitySession(){
        HashMap<String, String> activity = new HashMap<String, String>();

        activity.put(KEY_ISADD, sharedPreferences.getString(KEY_ISADD, null));
        activity.put(KEY_ISFISHFEEDING, sharedPreferences.getString(KEY_ISFISHFEEDING, null));

        return activity;
    }
    public void checkLogin(){

        if(!this.isLoggedIn()){
            // user is not logged in redirect him to MainActivity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
        else
        {
            // user is logged in redirect him to HomeActivity
            Intent intent = new Intent(_context, HomeActivity.class);
            // Closing all the Activities
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring HomeActivity
            _context.startActivity(intent);
        }
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to MainActivity Activity
        Intent intent1 = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring MainActivity
        _context.startActivity(intent1);
    }
    public void clearSchedule()
    {
        editor.remove(KEY_SCHEDULEID);
        editor.remove(KEY_SCHEDULEUSERID);
        editor.remove(KEY_SCHEDULETITLE);
        editor.remove(KEY_SCHEDULETIME);
        editor.remove(KEY_SCHEDULETYPE);
        editor.remove(KEY_ISACTIVE);
    }
    public void clearActivity()
    {
        editor.remove(KEY_ISADD);
        editor.remove(KEY_ISFISHFEEDING);
    }
    // Get Login State
    public boolean isLoggedIn(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }
}
