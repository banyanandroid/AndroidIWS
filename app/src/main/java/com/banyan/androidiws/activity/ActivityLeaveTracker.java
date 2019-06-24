package com.banyan.androidiws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ActivityLeaveTracker extends AppCompatActivity {

    private final String TAG_USER_ID = "userid";

    private final String TAG_NAME = "Name";
    private final String TAG_TOTAL_DAYS = "Total Days";
    private final String TAG_ELIGIBLE_LEAVE = "Eligible Leaves";
    private final String TAG_AVAILABLE_LEAVE = "Available Leaves";
    private final String TAG_TOTAL_AL = "Total AL";
    private final String TAG_TOTAL_UPL = "Total UPL";
    private final String TAG_TOTAL_BENCH = "Total Bench";
    private final String TAG_TOTAL_TR = "Total TR";
    private final String TAG_TOTAL_WORKING_DAYS = "Total Working Days";
    private final String TAG_PRODUCTIVE_DAYS = "Productive Days";

    private Toolbar toolbar;

    private TextView text_total_days, text_eligible_leave, text_available_leave, text_total_al, text_total_upl,
            text_total_bench, text_total_tr, text_total_working_days, text_productive_days;

    private SpotsDialog dialog;

    private Util utility;

    private Session_Manager session;

    private RequestQueue queue;

    private String str_user_id, str_user_name, str_user_type, str_user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__leave__tracker);

        /*********************************
         * SETUP
         **********************************/
        utility = new Util();


        /*************************
         *  SESSION
         *************************/
        Function_Verify_Network_Available(this);

        session = new Session_Manager(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);


        /**************************************
        *  FIND VIEW BY ID
        **************************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Leave Tracker");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        text_total_days = (TextView)findViewById(R.id.text_total_days);
        text_eligible_leave = (TextView)findViewById(R.id.text_eligible_leave);
        text_available_leave = (TextView)findViewById(R.id.text_available_leave);
        text_total_al = (TextView)findViewById(R.id.text_total_al);
        text_total_upl = (TextView)findViewById(R.id.text_total_upl);
        text_total_bench = (TextView)findViewById(R.id.text_total_bench);
        text_total_tr = (TextView)findViewById(R.id.text_total_tr);
        text_total_working_days = (TextView)findViewById(R.id.text_total_working_days);
        text_productive_days = (TextView)findViewById(R.id.text_productive_days);


        /*********************************
         * GET DATA
         **********************************/
        dialog = new SpotsDialog(this);
        dialog.show();
        queue = Volley.newRequestQueue(this);
        Function_Get_Leave_Details();


    }


    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(ActivityLeaveTracker.this)){
                utility.Function_Show_Not_Network_Message(ActivityLeaveTracker.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }


    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Get_Leave_Details() {

        System.out.println("### AppConfig.URL_LEAVE_TRACK " + AppConfig.URL_LEAVE_TRACK);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_TRACK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_TRACK : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONObject obj_one = obj.getJSONObject("records");

                        String str_name = obj_one.getString(TAG_NAME);
                        String str_total_days = obj_one.getString(TAG_TOTAL_DAYS);
                        String str_eligible_leave = obj_one.getString(TAG_ELIGIBLE_LEAVE);
                        String str_available_leaves = obj_one.getString(TAG_AVAILABLE_LEAVE);
                        String str_total_al = obj_one.getString(TAG_TOTAL_AL);
                        String str_total_upl = obj_one.getString(TAG_TOTAL_UPL);
                        String str_total_bench = obj_one.getString(TAG_TOTAL_BENCH);
                        String str_total_tr = obj_one.getString(TAG_TOTAL_TR);
                        String str_total_working_days = obj_one.getString(TAG_TOTAL_WORKING_DAYS);
                        String str_total_productive_days = obj_one.getString(TAG_PRODUCTIVE_DAYS);

                        text_total_days.setText(str_total_days);
                        text_eligible_leave.setText(str_eligible_leave);
                        text_available_leave.setText(str_available_leaves);
                        text_total_al.setText(str_total_al);
                        text_total_upl.setText(str_total_upl);
                        text_total_bench.setText(str_total_bench);
                        text_total_tr.setText(str_total_tr);
                        text_total_working_days.setText(str_total_working_days);
                        text_productive_days.setText(str_total_productive_days);

                        dialog.dismiss();

                    } else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

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

                System.out.println("### URL_LEAVE_TRACK " + TAG_USER_ID + " : " + str_user_id);

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
