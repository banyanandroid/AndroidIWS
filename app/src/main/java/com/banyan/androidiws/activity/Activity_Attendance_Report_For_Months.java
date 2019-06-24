package com.banyan.androidiws.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.banyan.androidiws.R;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Util;
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
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;


public class Activity_Attendance_Report_For_Months extends AppCompatActivity {

    public static final String TAG_ATTENDANCE_USER_ID = "user_id";
    public static final String TAG_ATTENDANCE_FROM_DATE = "from_date";
    public static final String TAG_ATTENDANCE_TO_DATE = "to_date";

    public static final String TAG_ATTENDANCE_APPROVE_STATUS_ID = "approved_value";
    public static final String TAG_ATTENDANCE_APPROVE_STATUS = "approved_status";
    public static final String TAG_ATTENDANCE_DATE = "attendance_date";
    public static final String TAG_ATTENDANCE_STATUS = "attendance_status";
    public static final String TAG_ATTENDANCE_REMARKS = "remarks";

    private Toolbar toolbar;

    private ExpCalendarView calendar_exp;

    private TextView text_month, text_year;

    private TextView text_present_days, text_bench_days, text_training_days, text_app_leave, text_lop_days, text_total_days;

    private CardView cardview_report;
    
    private RequestQueue queue;
    
    private SpotsDialog dialog;

    private ArrayList<HashMap<String, String>> arrayList_attendance;

    private Session_Manager session;

    private Util utility;

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
        utility = new Util();
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
        toolbar.setTitle("Attendance");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        text_month = (TextView)  findViewById(R.id.text_month);
        text_year = (TextView)  findViewById(R.id.text_year);
        cardview_report = (CardView)  findViewById(R.id.cardview_report);
        text_present_days = (TextView)  findViewById(R.id.text_present_days);
        text_bench_days = (TextView)  findViewById(R.id.text_bench_days);
        text_training_days = (TextView)  findViewById(R.id.text_training_days);
        text_app_leave = (TextView)  findViewById(R.id.text_app_leave);
        text_lop_days = (TextView)  findViewById(R.id.text_lop_days);
        text_total_days = (TextView)  findViewById(R.id.text_total_days);
        calendar_exp = (ExpCalendarView)  findViewById(R.id.calendar_exp);

        cardview_report.setVisibility(View.VISIBLE);

        /************************
         *  SETUP
         *************************/

        arrayList_attendance = new ArrayList<>();


        //get data

        //set current month and year
        try {

            Calendar calendar = Calendar.getInstance();
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);
            mYear = calendar.get(Calendar.YEAR);

            String today_date = mDay + "-" + (mMonth + 1) + "-" + mYear;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date_selected_date = null;
            date_selected_date = format.parse(today_date);

            String str_month = new Util().getMonthForInt(mMonth);
            String str_year = new SimpleDateFormat("yyyy").format(date_selected_date.getTime());

            text_month.setText(str_month);
            text_year.setText(str_year);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //set data
        calendar_exp.markDate(
                new DateData(2019, 06, 01).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_red_dark))));
        calendar_exp.markDate(
                new DateData(2019, 06, 02).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_green_dark))));
        calendar_exp.markDate(
                new DateData(2019, 06, 03).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_orange_dark))));
        calendar_exp.markDate(
                new DateData(2019, 06, 04).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_purple))));

        /*
        try {


            dialog = new SpotsDialog(Activity_Attendance_Report_For_Months.this);
            dialog.show();

            queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Months.this);
            Function_Get_Attendance_Report_For_Month();

        } catch (Exception e) {

        }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_attendance_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_attendance_report_for_days){

            Intent intent = new Intent(Activity_Attendance_Report_For_Months.this, Activity_Attendance_Report_For_Dates.class);
            startActivity(intent);

            return true;
        }

        return false;
    }

    /********************************
     *FUNCTION LEAVE LIST
     *********************************/
    private void Function_Get_Attendance_Report_For_Month() {

        System.out.println("### AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH " + AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_REPORT_FOR_MONTH, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_TYPE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200) {

                        JSONArray array_attendance_report = obj.getJSONArray("records");

                        for (int count = 0; count < array_attendance_report.length()-1; count++) {
                            JSONObject obj_leave_type = array_attendance_report.getJSONObject(count);

                            String str_approve_status_id = obj_leave_type.getString(TAG_ATTENDANCE_APPROVE_STATUS_ID);
                            String str_approve_status = obj_leave_type.getString(TAG_ATTENDANCE_APPROVE_STATUS);
                            String str_attendance_date = obj_leave_type.getString(TAG_ATTENDANCE_DATE);
                            String str_attendance_status = obj_leave_type.getString(TAG_ATTENDANCE_STATUS);
                            String str_attendance_remarks = obj_leave_type.getString(TAG_ATTENDANCE_REMARKS);

                            String[] str_array = str_attendance_date.split("-");
                            int int_year = Integer.parseInt(str_array[0]);
                            int int_month = Integer.parseInt(str_array[1]);
                            int int_day = Integer.parseInt(str_array[2]);

                            switch (str_approve_status_id){
                                case "0" :
                                    calendar_exp.markDate(
                                            new DateData(int_year, int_month, int_day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_red_dark))));
                                    break;
                                case "1" :
                                    calendar_exp.markDate(
                                            new DateData(int_year, int_month, int_day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_green_dark))));
                                    break;
                                case "2" :
                                    calendar_exp.markDate(
                                            new DateData(int_year, int_month, int_day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_orange_dark))));
                                case "3" :
                                    calendar_exp.markDate(
                                            new DateData(int_year, int_month, int_day).setMarkStyle(new MarkStyle(MarkStyle.BACKGROUND, getResources().getColor(android.R.color.holo_purple))));
                                    break;
                            }


                        }

                        JSONObject obj_attendance_status_count = array_attendance_report.getJSONObject(array_attendance_report.length() - 1 );

                        String str_lop = obj_attendance_status_count.getString("lop");
                        String str_approved_leave = obj_attendance_status_count.getString("approved Leave");
                        String str_training = obj_attendance_status_count.getString("training");
                        String str_bench = obj_attendance_status_count.getString("bench");
                        String str_present = obj_attendance_status_count.getString("present");
                        String str_total_days = obj_attendance_status_count.getString("total_days");

                        text_lop_days.setText(str_lop);
                        text_app_leave.setText(str_approved_leave);
                        text_training_days.setText(str_training);
                        text_bench_days.setText(str_bench);
                        text_present_days.setText(str_present);
                        text_total_days.setText(str_total_days);
                        dialog.dismiss();

                        cardview_report.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                    } else if (status == 400) {

                        arrayList_attendance.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Attendance_Report_For_Months.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_attendance.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Attendance_Report_For_Months.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


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

                new AlertDialog.Builder(Activity_Attendance_Report_For_Months.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Something Went Wrong, Try Again")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Activity_Attendance_Report_For_Months.this.finishAffinity();

                            }
                        }).show();

                System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse " + error.getLocalizedMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);
                params.put("usertype", str_user_type);

                System.out.println("### userid "+str_user_id);
                System.out.println("### usertype "+str_user_type);

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
}
