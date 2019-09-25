package com.banyan.androidiws.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Leave_Request_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class Activity_Leave_List extends AppCompatActivity {

    public static final String TAG_LEAVE_BALANCE_TITLE = "leavename";
    public static final String TAG_LEAVE_BALANCE_BALANCE = "balance_leave";
    public static final String TAG_LEAVE_BALANCE_TOTAL = "total_leavedays";

    public static final String TAG_LEAVE_REQUEST_TOTAL_DAYS = "no_of_days";
    public static final String TAG_LEAVE_REQUEST_SUBJECT = "subject";
    public static final String TAG_LEAVE_REQUEST_REASON = "reason";
    public static final String TAG_LEAVE_START_DATE = "from_date";
    public static final String TAG_LEAVE_END_DATE = "to_date";
    public static final String TAG_LEAVE_REQUEST_STATUS = "approval_status";



    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_FILTER_STATUS = "status";

    private SwipeRefreshLayout swipe_refresh_list, swipe_refresh_message;

    private TextView text_message;

    private Toolbar toolbar;

    private TextView text_filter;

    private ListView list_view_leave_request;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Session_Manager session;

    private Utility utility;

    private String str_user_id = "", str_user_name = "";

    private int int_selected_filter_id = 0;

    private ArrayList<HashMap<String, String>> arrayList_leave_details, arrayList_leave_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leave_details);


        /****************************
        *  SETUP
        ****************************/

        utility = new Utility();

        Function_Verify_Network_Available( Activity_Leave_List.this);

        /****************************
         *  SESSION
         ****************************/

        session = new Session_Manager( Activity_Leave_List.this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);

        /*************************
         *  FIND VIEW BY ID
         *************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Leave Request");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Leave_List.this, Activity_Attendance_Report_For_Months.class);
                startActivity(intent);
            }
        });

        list_view_leave_request = (ListView)  findViewById(R.id.list_view_leave_request);
        text_message = (TextView)  findViewById(R.id.text_message);



        /*********************************
         * SETUP
         **********************************/

        arrayList_leave_details = new ArrayList<>();
        arrayList_leave_request = new ArrayList<>();

        /*********************************
         * GET DATA
         **********************************/

        try {

            dialog = new SpotsDialog( Activity_Leave_List.this);
            dialog.show();

            queue = Volley.newRequestQueue( Activity_Leave_List.this);
            Function_Leave_List();

        } catch (Exception e) {
            System.out.println("### Exception " + e.getLocalizedMessage());
        }


        // action

        list_view_leave_request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                
                HashMap<String, String> map = arrayList_leave_request.get(position);
                String str_request_from_date = map.get(TAG_LEAVE_START_DATE);
                String str_request_end_date = map.get(TAG_LEAVE_END_DATE);
                String str_request_total_days = map.get(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                String str_request_subject = map.get(TAG_LEAVE_REQUEST_SUBJECT);
                String str_request_reason = map.get(TAG_LEAVE_REQUEST_REASON);
                String str_request_status = map.get(TAG_LEAVE_REQUEST_STATUS);


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( Activity_Leave_List.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Activity_Leave_Details.TAG_LEAVE_FROM_DATE, str_request_from_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_TO_DATE, str_request_end_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_NO_OF_DAYS, str_request_total_days);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_SUBJECT, str_request_subject);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_APPLY_REASON, str_request_reason);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_STATUS, str_request_status);
                editor.commit();

                Intent intent = new Intent( Activity_Leave_List.this, Activity_Leave_Details.class);
                startActivity(intent);
                 Activity_Leave_List.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_apply_leave, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_leave){

            Intent intent = new Intent(Activity_Leave_List.this, Activity_Add_Leave.class);
            startActivity(intent);

            return  true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Activity_Leave_List.this, Activity_Attendance_Report_For_Months.class);
        startActivity(intent);

    }

    /********************************
     *FUNCTION LEAVE BALANCE
     *********************************/
    private void Function_Leave_List() {

        System.out.println("### AppConfig.URL_LEAVE_LIST " + AppConfig.URL_LEAVE_LIST);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_LIST : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200) {

                        JSONArray array_request = obj.getJSONArray("records");

                        for (int count = 0; count < array_request.length(); count++) {
                            JSONObject obj_balance = array_request.getJSONObject(count);

                            String str_start_date = obj_balance.getString(TAG_LEAVE_START_DATE);
                            String str_end_date = obj_balance.getString(TAG_LEAVE_END_DATE);
                            String str_total_day = obj_balance.getString(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                            String str_subject = obj_balance.getString(TAG_LEAVE_REQUEST_SUBJECT);
                            String str_reason = obj_balance.getString(TAG_LEAVE_REQUEST_REASON);
                            String str_status = obj_balance.getString(TAG_LEAVE_REQUEST_STATUS);


                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_START_DATE, str_start_date);
                            map.put(TAG_LEAVE_END_DATE, str_end_date);
                            map.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, str_total_day);
                            map.put(TAG_LEAVE_REQUEST_SUBJECT, str_subject);
                            map.put(TAG_LEAVE_REQUEST_REASON, str_reason);
                            map.put(TAG_LEAVE_REQUEST_STATUS, str_status);

                            arrayList_leave_request.add(map);

                        }

                        list_view_leave_request.setVisibility(View.VISIBLE);
                        text_message.setVisibility(View.GONE);

                        if (array_request.length() == 0){

                            list_view_leave_request.setVisibility(View.GONE);
                            text_message.setVisibility(View.VISIBLE);
                        }

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText( Activity_Leave_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText( Activity_Leave_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List( Activity_Leave_List.this, arrayList_leave_request);
                    list_view_leave_request.setAdapter(adapter_leave_request_list);

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### JSONException "+e.getLocalizedMessage());
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_LEAVE_LIST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_LIST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Leave_List.this);


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

    public void Function_Verify_Network_Available(Context context){
        try{
            if (!utility.IsNetworkAvailable(this)){
                utility.Function_Show_Not_Network_Message(this);
            };
        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }

}
