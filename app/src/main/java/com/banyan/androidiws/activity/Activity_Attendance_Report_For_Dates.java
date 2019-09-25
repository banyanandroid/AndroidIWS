package com.banyan.androidiws.activity;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Attendance_Report;
import com.banyan.androidiws.fragment.Fragment_Main_Menu;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.NestedListview;
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


public class Activity_Attendance_Report_For_Dates extends AppCompatActivity {

    public static final String TAG_ATTENDANCE_USER_ID = "user_id";
    public static final String TAG_ATTENDANCE_FROM_DATE = "from_date";
    public static final String TAG_ATTENDANCE_TO_DATE = "to_date";

    public static final String TAG_ATTENDANCE_DATE = "date";
    public static final String TAG_ATTENDANCE_STATUS = "status";

    private TextView text_present_days, text_bench_days, text_training_days, text_app_leave, text_lop_days, text_total_days;

    private EditText edit_from_date, edit_to_date;

    private Button button_get_details;
    
    private NestedListview list_view_attendance_report;

    private CardView cardview_report;
    
    private RequestQueue queue;
    
    private SpotsDialog dialog;

    ArrayList<HashMap<String, String>> arrayList_attendance;

    private Session_Manager session;

    private String str_user_id;

    private int mYear, mMonth, mDay;

    private String str_selected_from_date, str_selected_to_date;

    private ImageView img_close;
    private Utility utility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report_for_dates);
        // Inflate the layout for this fragment

        utility = new Utility();

        Function_Verify_Network_Available(this);

        session = new Session_Manager(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);

        System.out.println("### str_user_id " + str_user_id);


        img_close = (ImageView) findViewById(R.id.img_close);
        edit_from_date = (EditText) findViewById(R.id.edit_from_date);
        edit_to_date = (EditText) findViewById(R.id.edit_to_date);
        button_get_details = (Button) findViewById(R.id.button_get_details);

        cardview_report = (CardView) findViewById(R.id.cardview_report);
        text_present_days = (TextView) findViewById(R.id.text_present_days);
        text_bench_days = (TextView) findViewById(R.id.text_bench_days);
        text_training_days = (TextView) findViewById(R.id.text_training_days);
        text_app_leave = (TextView) findViewById(R.id.text_app_leave);
        text_lop_days = (TextView) findViewById(R.id.text_lop_days);
        text_total_days = (TextView) findViewById(R.id.text_total_days);

        list_view_attendance_report = (NestedListview) findViewById(R.id.list_view_attendance_report);

        edit_from_date.setKeyListener(null);
        edit_to_date.setKeyListener(null);
        cardview_report.setVisibility(View.GONE);

        //setup
        /*********************************
         *  SETUP
         **********************************/


        arrayList_attendance = new ArrayList<>();

        //set data


        edit_from_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Attendance_Report_For_Dates.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    try {

                                        str_selected_from_date = year + "-" + (monthOfYear + 1)+ "-"+dayOfMonth;
                                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                        Date date_selected_date = format.parse(str_selected_from_date);
                                        String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                        edit_from_date.setText(format_date);

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.show();


                    return true;
                }

                return false;
            }
        });


        edit_to_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){

                    String str_start_date = edit_from_date.getText().toString();

                    if (str_start_date.isEmpty()){

                        TastyToast.makeText(Activity_Attendance_Report_For_Dates.this, "Select From Date First.", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                        
                    }else{

                        try {
                            //get start date
                            String[] array_star_date = str_selected_from_date.split("-");
                            String str_year = array_star_date[0];
                            String str_month = array_star_date[1];
                            String str_day = array_star_date[2];

                            String str_star_date = str_day+"-"+str_month+"-"+str_year;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            Date date_start_date = format.parse(str_star_date);

                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Attendance_Report_For_Dates.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            try {

                                                str_selected_to_date = year + "-" + (monthOfYear + 1)+ "-"+dayOfMonth;
                                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                                Date date_selected_date = format.parse(str_selected_to_date);
                                                String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                                edit_to_date.setText(format_date);

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                            datePickerDialog.show();
                            return true;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }


                   
                    
                    return true;
                }

                return false;
            }
        });

        //action

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Activity_Attendance_Report_For_Dates.this, Fragment_Main_Menu.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);  // close activity

            }
        });

        button_get_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cardview_report.setVisibility(View.VISIBLE);

                HashMap<String, String> map = new HashMap<>();
                map.put(TAG_ATTENDANCE_DATE, "31-05-2019");
                map.put(TAG_ATTENDANCE_STATUS, "Present");

                arrayList_attendance.add(map);


                HashMap<String, String> map2 = new HashMap<>();
                map2.put(TAG_ATTENDANCE_DATE, "03-06-2019");
                map2.put(TAG_ATTENDANCE_STATUS, "Absent");

                arrayList_attendance.add(map2);

                Adapter_Attendance_Report adapter_attendance_report = new Adapter_Attendance_Report(Activity_Attendance_Report_For_Dates.this, arrayList_attendance);
                list_view_attendance_report.setAdapter(adapter_attendance_report);

/*                String str_from_date = edit_from_date.getText().toString();
                String str_to_date = edit_to_date.getText().toString();
                
                if (str_from_date.isEmpty()){
                    TastyToast.makeText(Activity_Attendance_Report_For_Dates.this, "Select From Date", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_to_date.isEmpty()){
                    TastyToast.makeText(Activity_Attendance_Report_For_Dates.this, "Select To Date", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else{

                    try{

                        cardview_report.setVisibility(View.GONE);
                        arrayList_attendance.clear();
                        Adapter_Attendance_Report adapter_attendance_report = new Adapter_Attendance_Report(Activity_Attendance_Report_For_Dates.this, arrayList_attendance);
                        list_view_attendance_report.setAdapter(adapter_attendance_report);

                        dialog = new SpotsDialog(Activity_Attendance_Report_For_Dates.this);
                        dialog.show();

                        queue = Volley.newRequestQueue(Activity_Attendance_Report_For_Dates.this);
                        Function_Get_Attendance_Report();

                    }catch (Exception e){

                    }

                }*/

            }
        });

    }

    /********************************
     *FUNCTION LEAVE LIST
     *********************************/
    private void Function_Get_Attendance_Report() {

        System.out.println("### AppConfig.URL_ATTENDANCE_REPORT " + AppConfig.URL_ATTENDANCE_REPORT);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_REPORT, new Response.Listener<String>() {

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

                            String str_attendance_date = obj_leave_type.getString(TAG_ATTENDANCE_DATE);
                            String str_attendance_status = obj_leave_type.getString(TAG_ATTENDANCE_STATUS);

                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_ATTENDANCE_DATE, str_attendance_date);
                            map.put(TAG_ATTENDANCE_STATUS, str_attendance_status);

                            arrayList_attendance.add(map);

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

                    } else if (status == 400) {

                        arrayList_attendance.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Attendance_Report_For_Dates.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_attendance.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Attendance_Report_For_Dates.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Attendance_Report adapter_attendance_report = new Adapter_Attendance_Report(Activity_Attendance_Report_For_Dates.this, arrayList_attendance);
                    list_view_attendance_report.setAdapter(adapter_attendance_report);

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

                new Utility().Function_Error_Dialog(Activity_Attendance_Report_For_Dates.this);

                System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse " + error.getLocalizedMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_ATTENDANCE_USER_ID, str_user_id);
                params.put(TAG_ATTENDANCE_FROM_DATE, str_selected_from_date);
                params.put(TAG_ATTENDANCE_TO_DATE, str_selected_to_date);

                System.out.println("###" + TAG_ATTENDANCE_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_ATTENDANCE_FROM_DATE + " : " + str_selected_from_date);
                System.out.println("###" + TAG_ATTENDANCE_TO_DATE + " : " + str_selected_to_date);

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
