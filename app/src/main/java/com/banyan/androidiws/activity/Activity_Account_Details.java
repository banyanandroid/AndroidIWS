package com.banyan.androidiws.activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Util;
import com.google.android.material.tabs.TabLayout;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class Activity_Account_Details extends AppCompatActivity {

    public static final String TAG_USER_ID = "userid";
    public static final String TAG_SALUTE = "salute";
    public static final String TAG_FIRST_NAME = "fname";
    public static final String TAG_LAST_NAME = "lname";
    public static final String TAG_DOB = "dob";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_DESIGNATION = "designation";
    public static final String TAG_ADDRESS_1 = "address1";
    public static final String TAG_ADDRESS_2 = "address2";
    public static final String TAG_CITY = "city";
    public static final String TAG_AADHAR_NO = "aadharno";
    public static final String TAG_PAN_NO = "panno";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_PHOTO = "profile_photo";

    public static final String TAG_OLD_PASSWORD = "oldpassword";
    public static final String TAG_NEW_PASSWORD = "newpassword";
    public static final String TAG_USER_TYPE = "usertype";
    public static final String TAG_USER_ROLE = "userrole";

    private Session_Manager session;

    private Toolbar toolbar;

    private CircleImageView circle_image_profile;

    private TextView text_name, text_designation;

    private EditText edit_first_name, edit_last_name, edit_date_of_birth, edit_email,
            edit_designation, edit_address_1, edit_address_2, edit_city, edit_aadhar_no, edit_pan_no, edit_gender;

    private Button button_change_password;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Util utility;

    private String str_user_id, str_user_name, str_user_type, str_user_role, str_old_password, str_new_password;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*********************************
         * SETUP
         **********************************/
        utility = new Util();


        /*****************************
         *  SESSION
         *****************************/
        Function_Verify_Network_Available(this);

        session = new Session_Manager(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_role " + str_user_role);

        /*************************
         *  FIND VIEW BY ID
         *************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Account Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        circle_image_profile = (CircleImageView) findViewById(R.id.circle_image_profile);

        text_name = (TextView) findViewById(R.id.text_name);
        text_designation = (TextView) findViewById(R.id.text_designation);

        edit_first_name = (EditText) findViewById(R.id.edit_first_name);
        edit_last_name = (EditText) findViewById(R.id.edit_last_name);
        edit_date_of_birth = (EditText) findViewById(R.id.edit_date_of_birth);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_designation = (EditText) findViewById(R.id.edit_designation);
        edit_address_1 = (EditText) findViewById(R.id.edit_address_1);
        edit_address_2 = (EditText) findViewById(R.id.edit_address_2);
        edit_city = (EditText) findViewById(R.id.edit_city);
        edit_aadhar_no = (EditText) findViewById(R.id.edit_aadhar_no);
        edit_pan_no = (EditText) findViewById(R.id.edit_pan_no);
        edit_gender = (EditText) findViewById(R.id.edit_gender);

        button_change_password = (Button) findViewById(R.id.button_change_password);

        edit_first_name.setKeyListener(null);
        edit_last_name.setKeyListener(null);
        edit_date_of_birth.setKeyListener(null);
        edit_email.setKeyListener(null);
        edit_designation.setKeyListener(null);
        edit_address_1.setKeyListener(null);
        edit_address_2.setKeyListener(null);
        edit_city.setKeyListener(null);
        edit_aadhar_no.setKeyListener(null);
        edit_pan_no.setKeyListener(null);
        edit_gender.setKeyListener(null);



        /*************************
         *  GET DATA
         *************************/

        dialog = new SpotsDialog(this);
        dialog.show();
        queue = Volley.newRequestQueue(this);
        Function_Get_Profile();

        /*************************
         *  ACTION
         *************************/

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Alert_Change_Password();

            }
        });

    }


    public void Alert_Change_Password() {

        LayoutInflater layoutInflater = LayoutInflater.from(Activity_Account_Details.this);
        View view_alert = layoutInflater.inflate(R.layout.alert_change_password, null);

        final EditText edit_old_password = (EditText) view_alert.findViewById(R.id.edit_old_password);
        final EditText edit_new_password = (EditText) view_alert.findViewById(R.id.edit_new_password);

        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Account_Details.this);
        alertdialog_builder.setCancelable(false);
        alertdialog_builder.setView(view_alert);
        alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                str_old_password = edit_old_password.getText().toString();
                str_new_password = edit_new_password.getText().toString();

                if (str_old_password.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Enter Old Password", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else if (str_new_password.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Enter New Password", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    dialog = new SpotsDialog(Activity_Account_Details.this);
                    dialog.show();

                    queue = Volley.newRequestQueue(Activity_Account_Details.this);
                    Function_Change_Password();

                }

            }
        });
        alertdialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertdialog_builder.create();
        alertDialog.setTitle("Change Password");
        alertDialog.show();

    }


    /********************************
     *FUNCTION CHANGE PASSWORD
     *********************************/
    private void Function_Change_Password() {

        System.out.println("### AppConfig.URL_CHANGE_PASSWORD " + AppConfig.URL_CHANGE_PASSWORD);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_CHANGE_PASSWORD : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Account_Details.this, "Password Changed Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Account_Details.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Account_Details.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_CHANGE_PASSWORD onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_CHANGE_PASSWORD onErrorResponse " + error.getLocalizedMessage());

                new AlertDialog.Builder(Activity_Account_Details.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Something Went Wrong, Try Again Later.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Activity_Account_Details.this.finishAffinity();

                            }
                        }).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_TYPE, str_user_type);
                params.put(TAG_USER_ROLE, str_user_role);
                params.put(TAG_OLD_PASSWORD, str_old_password);
                params.put(TAG_NEW_PASSWORD, str_new_password);

                System.out.println("### URL_CHANGE_PASSWORD " + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_USER_TYPE + " : " + str_user_type);
                System.out.println("###" + TAG_USER_ROLE + " : " + str_user_role);
                System.out.println("###" + TAG_OLD_PASSWORD + " : " + str_old_password);
                System.out.println("###" + TAG_NEW_PASSWORD + " : " + str_new_password);

                return checkParams(params);

            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                new Constants().MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(Activity_Account_Details.this)){
                utility.Function_Show_Not_Network_Message(Activity_Account_Details.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }


    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Get_Profile() {

        System.out.println("### AppConfig.URL_PROFILE " + AppConfig.URL_PROFILE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROFILE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONObject obj_one = obj.getJSONObject("records");

                        String str_salute = obj_one.getString(TAG_SALUTE);
                        String str_first_name = obj_one.getString(TAG_FIRST_NAME);
                        String str_last_name = obj_one.getString(TAG_LAST_NAME);
                        String str_dob = obj_one.getString(TAG_DOB);
                        String str_email = obj_one.getString(TAG_EMAIL);
                        String str_designation = obj_one.getString(TAG_DESIGNATION);
                        String str_address1 = obj_one.getString(TAG_ADDRESS_1);
                        String str_address2 = obj_one.getString(TAG_ADDRESS_2);
                        String str_city = obj_one.getString(TAG_CITY);
                        String str_aadhar = obj_one.getString(TAG_AADHAR_NO);
                        String str_pan_no = obj_one.getString(TAG_PAN_NO);
                        String str_gender = obj_one.getString(TAG_GENDER);
                        String str_photo = obj_one.getString(TAG_PHOTO);

                        text_name.setText(str_first_name +" "+ str_last_name);
                        text_designation.setText(str_designation);

                        edit_first_name.setText(str_first_name);
                        edit_last_name.setText(str_last_name);
                        edit_date_of_birth.setText(str_dob);
                        edit_email.setText(str_email);
                        edit_designation.setText(str_designation);
                        edit_address_1.setText(str_address1);
                        edit_address_2.setText(str_address2);
                        edit_city.setText(str_city);
                        edit_aadhar_no.setText(str_aadhar);
                        edit_pan_no.setText(str_pan_no);
                        edit_gender.setText(str_gender);

                        System.out.println("### str_photo "+str_photo);

                        Picasso.with(Activity_Account_Details.this)
                                .load(str_photo)
                                .placeholder(R.drawable.user)
                                .into(circle_image_profile);

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);

                System.out.println("###" + TAG_USER_ID + " : " + str_user_id);

                return checkParams(params);

            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }

        };

        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                new Constants().MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }


}
