package com.banyan.androidiws.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;


public class Activity_Attendance_Report_For_Months extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final String TAG_USER_ID = "userid";

    public static final String TAG_ATTENDANCE_USER_ID = "userid";
    public static final String TAG_ATTENDANCE_MONTH = "month";
    public static final String TAG_ATTENDANCE_YEAR = "year";

    public static final String TAG_ATTENDANCE_APPROVE_STATUS_ID = "approved_value";
    public static final String TAG_ATTENDANCE_APPROVE_STATUS = "approved_status";
    public static final String TAG_ATTENDANCE_DATE = "attendance_date";
    public static final String TAG_ATTENDANCE_STATUS = "attendance_status";
    public static final String TAG_ATTENDANCE_REMARKS = "remarks";

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

    private final String TAG_CALLING_TYPE_FROM_NORMAL = "normal";
    private final String TAG_CALLING_TYPE_FROM_CALENDER = "calender";

    private SwipeRefreshLayout swipe_refresh;

    private Toolbar toolbar;

    private ExpCalendarView calendar_exp;

    private TextView text_month, text_year;

    private TextView text_total_days,
            text_eligible_leave, text_available_leave, text_total_al, text_total_upl, text_total_bench, text_total_tr, text_total_working_days, text_productive_days;

    private AppCompatButton button_leave_tracker;

    private RequestQueue queue;
    
    private SpotsDialog dialog;

    private ArrayList<HashMap<String, String>> arrayList_attendance;

    private Session_Manager session;

    private Utility utility;

    private String str_user_id, str_user_type;

    private int mYear, mMonth, mDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report_for_month);
        // Inflate the layout for this fragment

        /************************
        *  SESSION
        *************************/
        utility = new Utility();
        Function_Verify_Network_Available(Activity_Attendance_Report_For_Months.this);

        session = new Session_Manager(Activity_Attendance_Report_For_Months.this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_type " + str_user_type);

        /************************
         *  FIND VIEW BY ID
         *************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Attendance Report");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Attendance_Report_For_Months.this, Activity_Main.class);
                startActivity(intent);
            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        text_month = (TextView)  findViewById(R.id.text_month);
        text_year = (TextView)  findViewById(R.id.text_year);

        text_total_days = (TextView)  findViewById(R.id.text_total_days);
        text_eligible_leave = (TextView)  findViewById(R.id.text_eligible_leave);
        text_available_leave = (TextView)  findViewById(R.id.text_available_leave);
        text_total_al = (TextView)  findViewById(R.id.text_total_al);
        text_total_upl = (TextView)  findViewById(R.id.text_total_upl);
        text_total_bench = (TextView)  findViewById(R.id.text_total_bench);
        text_total_tr = (TextView)  findViewById(R.id.text_total_tr);
        text_total_working_days = (TextView)  findViewById(R.id.text_total_working_days);
        text_productive_days = (TextView)  findViewById(R.id.text_productive_days);

        calendar_exp = (ExpCalendarView)  findViewById(R.id.calendar_exp);

        button_leave_tracker = (AppCompatButton)  findViewById(R.id.button_leave_tracker);

        swipe_refresh.setOnRefreshListener(this);

        /************************
         *  SETUP
         *************************/

        arrayList_attendance = new ArrayList<>();




        /************************
         *  GET DATA
         *************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                //set current month and year
                try {

                    Calendar calendar = Calendar.getInstance();
                    mDay = calendar.get(Calendar.DAY_OF_MONTH);
                    mMonth = calendar.get(Calendar.MONTH);
                    mYear = calendar.get(Calendar.YEAR);

                    String today_date = mDay + "-" + mMonth + "-" + mYear;
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date_selected_date = null;
                    date_selected_date = format.parse(today_date);

                    String str_month = new Utility().getMonthForInt(mMonth);
                    String str_year = new SimpleDateFormat("yyyy").format(date_selected_date.getTime());

                    text_month.setText(str_month);
                    text_year.setText(str_year);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {

                    queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Months.this);
                    Function_Get_Attendance_Report_For_Month(TAG_CALLING_TYPE_FROM_NORMAL);

                } catch (Exception e) {

                }

            }
        });



        /************************
         *  ACTION
         *************************/
        calendar_exp.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {

                mMonth = month;
                mYear = year;

                System.out.println("### month "+month);
                System.out.println("### year "+year);

                String str_month = new Utility().getMonthForInt((mMonth-1));
                String str_year = ""+year;
                text_month.setText(str_month);
                text_year.setText(str_year);

                try {

                    swipe_refresh.setRefreshing(true);

                    queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Months.this);
                    Function_Get_Attendance_Report_For_Month(TAG_CALLING_TYPE_FROM_CALENDER);

                } catch (Exception e) {

                }

            }
        });

        button_leave_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Attendance_Report_For_Months.this, Activity_Add_Leave.class);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Activity_Attendance_Report_For_Months.this, Activity_Main.class);
        startActivity(intent);

    }

    /********************************
     *FUNCTION LEAVE LIST
     ********************************
     * @param str_calling_type*/
    private void Function_Get_Attendance_Report_For_Month(final String str_calling_type) {

        System.out.println("### AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH " + AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200) {

                        JSONObject obj_records = obj.getJSONObject("records");
                        JSONArray array_day = obj_records.getJSONArray("day");

                        for (int count = 0; count < array_day.length(); count++) {

                            if (count == 0){  // to handle total days

                            }else{

                                JSONObject obj_day = array_day.getJSONObject(count);

                                if (count >= 1 && count <= 9){

                                    String str_status = "";
                                    if (obj_day.has(""+count)){
                                         str_status = obj_day.getString(""+count);
                                    }else{
                                         str_status = obj_day.getString("0"+count);
                                    }

                                    if (str_calling_type.equals(TAG_CALLING_TYPE_FROM_NORMAL)){
                                        Function_Assign_Color_For_Date(count, (mMonth +1), mYear, str_status);
                                    }else{
                                        Function_Assign_Color_For_Date(count, (mMonth), mYear, str_status);
                                    }


                                }else{

                                    String str_status = obj_day.getString(""+count);

                                    if (str_calling_type.equals(TAG_CALLING_TYPE_FROM_NORMAL)){
                                        Function_Assign_Color_For_Date(count, (mMonth +1), mYear, str_status);
                                    }else{
                                        Function_Assign_Color_For_Date(count, (mMonth), mYear, str_status);
                                    }
                                }


                            }

                        }


                    } else if (status == 400) {


                        TastyToast.makeText(Activity_Attendance_Report_For_Months.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {


                        TastyToast.makeText(Activity_Attendance_Report_For_Months.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Months.this);
                    Function_Get_Leave_Details();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### JSONException "+e.getLocalizedMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                //testing

                System.out.println("### AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Attendance_Report_For_Months.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (str_calling_type.equals(TAG_CALLING_TYPE_FROM_NORMAL)){

                    params.put(TAG_ATTENDANCE_MONTH, ""+(mMonth+1));
                    System.out.println("### "+TAG_ATTENDANCE_MONTH+":"+(mMonth+1));
                }else{

                    params.put(TAG_ATTENDANCE_MONTH, ""+mMonth);
                    System.out.println("### "+TAG_ATTENDANCE_MONTH+":"+mMonth);
                }

                params.put(TAG_ATTENDANCE_YEAR, ""+mYear);
                params.put(TAG_ATTENDANCE_USER_ID, str_user_id);


                System.out.println("### AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH "+TAG_ATTENDANCE_YEAR+":"+mYear);
                System.out.println("### "+TAG_ATTENDANCE_USER_ID+":"+str_user_id);

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
                        text_productive_days.setText(str_total_productive_days +"%");

                        swipe_refresh.setRefreshing(false);

                    } else if(status == 400) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    swipe_refresh.setRefreshing(false);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                swipe_refresh.setRefreshing(false);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_LEAVE_TRACK onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_TRACK onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Attendance_Report_For_Months.this);

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

    public void Function_Verify_Network_Available(Context context){
        try{
            if (!utility.IsNetworkAvailable(context)){
                utility.Function_Show_Not_Network_Message(Activity_Attendance_Report_For_Months.this);
            };
        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }

    public void Function_Assign_Color_For_Date(int day, int month, int year, String status){

        System.out.println("### status "+status);
        switch (status){
            case "P" :
                calendar_exp.markDate(
                        new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.color_blue_dark))));
                break;
            case "L" :
                calendar_exp.markDate(
                        new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.color_yellow))));
                break;
            case "B" :
                calendar_exp.markDate(
                        new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_green_dark))));

            case "T" :
                calendar_exp.markDate(
                        new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(R.color.color_blue_light))));
            case "UPL" :
                calendar_exp.markDate(
                        new DateData(year, month, day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_red_dark))));
                break;
        }

    }

    @Override
    public void onRefresh() {

        swipe_refresh.setRefreshing(true);

        //set current month and year
        try {

            Calendar calendar = Calendar.getInstance();
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);
            mYear = calendar.get(Calendar.YEAR);

            String today_date = mDay + "-" + mMonth + "-" + mYear;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date_selected_date = null;
            date_selected_date = format.parse(today_date);

            String str_month = new Utility().getMonthForInt(mMonth);
            String str_year = new SimpleDateFormat("yyyy").format(date_selected_date.getTime());

            System.out.println("### mDay "+mDay);
            System.out.println("### mMonth "+mMonth);
            System.out.println("### mYear "+mYear);

            calendar_exp.travelTo(new DateData(mYear, mMonth +1, mDay));

            text_month.setText(str_month);
            text_year.setText(str_year);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {

            queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Months.this);
            Function_Get_Attendance_Report_For_Month(TAG_CALLING_TYPE_FROM_NORMAL);

        } catch (Exception e) {

        }

    }
}
