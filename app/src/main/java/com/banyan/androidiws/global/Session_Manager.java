package com.banyan.androidiws.global;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.banyan.androidiws.activity.Activity_Login;

import java.util.HashMap;


/**
 * Created by Banyan on 1/24/2018.
 */

public class Session_Manager {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_USER_ID = "str_user_id";
    public static final String KEY_USER_TYPE_ID = "str_user_type_id";
    public static final String KEY_USER_NAME = "str_user_name";
    public static final String KEY_USER_ROLE ="str_user_role";
    public static final String KEY_USER_DEPARTMENT_ID ="str_user_department_id";
    public static final String KEY_USER_IMAGE ="str_user_image";
    public static final String KEY_USER_NAME_FIRST_LAST ="str_user_name_first_last";


    public static final String KEY_USER = "name";
    // Constructor
    public Session_Manager(Context context) {

        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     *
     * str_type_id 1 : main admin ; 2 : sub admin ; 3 : guest
     */

    public void createLoginSession(String user_id, String user_name, String type_id, String user_role, String user_department_id, String user_image , String user_name_first_last) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USER_ID,user_id);
        editor.putString(KEY_USER_NAME,user_name);
        editor.putString(KEY_USER_TYPE_ID,type_id);
        editor.putString(KEY_USER_ROLE, user_role);
        editor.putString(KEY_USER_DEPARTMENT_ID, user_department_id);
        editor.putString(KEY_USER_IMAGE, user_image);
        editor.putString(KEY_USER_NAME_FIRST_LAST, user_name_first_last);
        // commit changes
        editor.commit();

    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */

    public void checkLogin() {

        // Check login status
        if (!this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Activity_Login.class);
            // Closing all the Act`ivities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {

        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, ""));
        user.put(KEY_USER_NAME, pref.getString(KEY_USER_NAME, ""));
        user.put(KEY_USER_ROLE, pref.getString(KEY_USER_ROLE, ""));
        user.put(KEY_USER_TYPE_ID, pref.getString(KEY_USER_TYPE_ID, ""));
        user.put(KEY_USER_DEPARTMENT_ID, pref.getString(KEY_USER_DEPARTMENT_ID, ""));
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, ""));
        user.put(KEY_USER_NAME_FIRST_LAST, pref.getString(KEY_USER_NAME_FIRST_LAST, ""));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logoutUser() {

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Activity_Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);

    }

    /**
     * Quick check for login
     **/
// Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


}


