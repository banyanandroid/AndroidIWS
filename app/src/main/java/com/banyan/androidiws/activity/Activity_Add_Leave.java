package com.banyan.androidiws.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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


public class Activity_Add_Leave extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USER_ROLE = "userrole";

    public static final String TAG_LEAVE_FROM_DATE = "from_date";
    public static final String TAG_LEAVE_TO_DATE = "to_date";
    public static final String TAG_LEAVE_APPLY_REASON = "reason";
    public static final String TAG_LEAVE_NO_OF_DAYS = "no_of_days";
    public static final String TAG_LEAVE_LEAVE_TYPE = "leave_type";
    public static final String TAG_LEAVE_SUBJECT = "subject";

    public static final String TAG_LEAVE_ID = "leave_id";
    public static final String TAG_LEAVE_NAME = "leave_name";

    public static final String TAG_LEAVE_BALANCE_TITLE = "leavename";
    public static final String TAG_LEAVE_BALANCE_BALANCE = "balance_leave";
    public static final String TAG_LEAVE_BALANCE_TOTAL = "total_leavedays";

    public static final String TAG_LEAVE_REQUEST_TOTAL_DAYS = "no_of_days";
    public static final String TAG_LEAVE_REQUEST_SUBJECT = "subject";
    public static final String TAG_LEAVE_REQUEST_REASON = "reason";
    public static final String TAG_LEAVE_START_DATE = "from_date";
    public static final String TAG_LEAVE_END_DATE = "to_date";
    public static final String TAG_LEAVE_REQUEST_STATUS = "approval_status";
    public static final String TAG_LEAVE_CANCEL_STATUS = "cancel_status";

    public static String str_selected_leave_type_id = "";

    private SwipeRefreshLayout swipe_refresh;

    private ImageView img_close;

    private TextView text_message;

    private EditText edit_subject, edit_start_date, edit_end_date, edit_description;

    private Button button_apply_leave;

    private NestedListview list_view_leave_request;

    private SpotsDialog dialog;

    private Session_Manager session;

    private RequestQueue queue;

    private Utility utility;

    private ArrayList<HashMap<String, String>> arrayList_leave_details, arrayList_leave_request;

    private ArrayList<HashMap<String, String>> arrayList_leave_type;

    private String str_user_id = "", str_user_name = "", str_subject = "", str_selected_from_date = "",
            str_selected_to_date = "", str_description = "", str_user_role = "", str_no_of_days = "",
            str_leave_id = "";

    private int mYear, mMonth, mDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leave);

        /*********************************
         *  SETUP
         **********************************/
        utility = new Utility();

        Function_Verify_Network_Available(this);

        arrayList_leave_details = new ArrayList<>();
        arrayList_leave_request = new ArrayList<>();

        /*********************************
         *  SESSION
         **********************************/

        session = new Session_Manager(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_role " + str_user_role);

        /*********************************
         *  FIND VIEW BY ID
         **********************************/

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        img_close = (ImageView) findViewById(R.id.img_close);
        edit_subject = (EditText) findViewById(R.id.edit_subject);
        edit_start_date = (EditText) findViewById(R.id.edit_start_date);
        edit_end_date = (EditText) findViewById(R.id.edit_end_date);
        edit_description = (EditText) findViewById(R.id.edit_description);
        button_apply_leave = (Button) findViewById(R.id.button_apply_leave);

        list_view_leave_request = (NestedListview)  findViewById(R.id.list_view_leave_request);
        text_message = (TextView)  findViewById(R.id.text_message);

        swipe_refresh.setOnRefreshListener(this);

        edit_start_date.setKeyListener(null);
        edit_end_date.setKeyListener(null);

        str_selected_leave_type_id = "";
        arrayList_leave_type = new ArrayList<>();

        /*********************************
         * GET DATA
         **********************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                try {

                    arrayList_leave_request.clear();

                    queue = Volley.newRequestQueue( Activity_Add_Leave.this);
                    Function_Leave_List();

                } catch (Exception e) {
                    System.out.println("### Exception " + e.getLocalizedMessage());
                }


            }
        });





        /*********************************
         *  ACTION
         **********************************/

        //action
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activity_Add_Leave.this, Activity_Attendance_Report_For_Months.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);  // close activity

            }
        });

        edit_start_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    String str_end_date = edit_end_date.getText().toString();

                    if (str_end_date.isEmpty()){

                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Add_Leave.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        try {

                                            str_selected_from_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                            Date date_selected_date = format.parse(str_selected_from_date);
                                            String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                            edit_start_date.setText(format_date);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        return true;

                    }else{

                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Add_Leave.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        try {

                                            // Create SimpleDateFormat object
                                            SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");

                                            String str_from_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                            // Get the two dates to be compared
                                            Date d1 = sdfo.parse(str_from_date);
                                            Date d2 = sdfo.parse(str_selected_to_date);

                                            // Print the dates
                                            System.out.println("Date1 : " + sdfo.format(d1));
                                            System.out.println("Date2 : " + sdfo.format(d2));

                                            if (d1.compareTo(d2) < 0 || d1.compareTo(d2) == 0) {

                                                str_selected_from_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                                Date date_selected_date = format.parse(str_selected_from_date);
                                                String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                                edit_start_date.setText(format_date);

                                            }else{
                                                TastyToast.makeText(Activity_Add_Leave.this, "From Date Should be Less Than To Date", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                                            }

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        return true;

                    }


                }


                return false;
            }
        });


        edit_end_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    String str_start_date = edit_start_date.getText().toString();

                    if (str_start_date.isEmpty()) { // start date not selected

                        // Get Current Date
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Add_Leave.this,
                                new DatePickerDialog.OnDateSetListener() {

                                    @Override
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {

                                        try {

                                            str_selected_to_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                            Date date_selected_date = format.parse(str_selected_to_date);
                                            String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                            edit_end_date.setText(format_date);

                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, mYear, mMonth, mDay);
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        return true;

                    } else { // start date selected


                        try {
                            //get start date
                            String[] array_star_date = str_selected_from_date.split("-");
                            String str_day = array_star_date[0];
                            String str_month = array_star_date[1];
                            String str_year = array_star_date[2];
                            String str_star_date = str_day + "-" + str_month + "-" + str_year;
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            Date date_start_date = format.parse(str_star_date);

                            // Get Current Date
                            final Calendar c = Calendar.getInstance();
                            mYear = c.get(Calendar.YEAR);
                            mMonth = c.get(Calendar.MONTH);
                            mDay = c.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog datePickerDialog = new DatePickerDialog(Activity_Add_Leave.this,
                                    new DatePickerDialog.OnDateSetListener() {

                                        @Override
                                        public void onDateSet(DatePicker view, int year,
                                                              int monthOfYear, int dayOfMonth) {

                                            try {

                                                str_selected_to_date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                                Date date_selected_date = format.parse(str_selected_to_date);
                                                String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
                                                edit_end_date.setText(format_date);

                                            } catch (ParseException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    }, mYear, mMonth, mDay);
                            datePickerDialog.getDatePicker().setMinDate(date_start_date.getTime());
                            datePickerDialog.show();
                            return true;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }

                }


                return false;
            }
        });


        button_apply_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                str_subject = edit_subject.getText().toString();
//                str_selected_from_date
//                str_selected_to_date
                str_description = edit_description.getText().toString();
//                str_selected_leave_type_id


                if (str_subject.isEmpty()){
                    TastyToast.makeText(Activity_Add_Leave.this, "Enter Subject", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_selected_from_date.isEmpty()){
                    TastyToast.makeText(Activity_Add_Leave.this, "Select From Date", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_selected_to_date.isEmpty()){
                    TastyToast.makeText(Activity_Add_Leave.this, "Select To Date", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_description.isEmpty()){
                    TastyToast.makeText(Activity_Add_Leave.this, "Enter Description", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else{

                    if (str_selected_from_date.equals(str_selected_to_date))
                        str_no_of_days = "1";
                    else
                        str_no_of_days = new Utility().getCountOfDays(str_selected_from_date, str_selected_to_date);

                    System.out.println("### str_no_of_days "+str_no_of_days);

                    try {

                        dialog = new SpotsDialog(Activity_Add_Leave.this);
                        dialog.show();

                        queue = Volley.newRequestQueue(Activity_Add_Leave.this);
                        Function_Leave_Apply();

                    } catch (Exception e) {
                        System.out.println("### Exception " + e.getLocalizedMessage());
                    }

                }

            }
        });


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
                String str_cancel_status = map.get(TAG_LEAVE_CANCEL_STATUS);


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( Activity_Add_Leave.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Activity_Leave_Details.TAG_LEAVE_FROM_DATE, str_request_from_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_TO_DATE, str_request_end_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_NO_OF_DAYS, str_request_total_days);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_SUBJECT, str_request_subject);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_APPLY_REASON, str_request_reason);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_STATUS, str_request_status);
                editor.commit();

                Intent intent = new Intent( Activity_Add_Leave.this, Activity_Leave_Details.class);
                startActivity(intent);
                Activity_Add_Leave.this.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });

        list_view_leave_request.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> map = arrayList_leave_request.get(position);
                String str_leave_request_status = map.get(TAG_LEAVE_REQUEST_STATUS);
                str_leave_id = map.get(TAG_LEAVE_ID);

                System.out.println("### setOnLongClickListener ");
                Alert_Cancel_Reqeust(str_leave_request_status);

                return true;
            }
        });

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

                            String str_leave_id = obj_balance.getString(TAG_LEAVE_ID);
                            String str_start_date = obj_balance.getString(TAG_LEAVE_START_DATE);
                            String str_end_date = obj_balance.getString(TAG_LEAVE_END_DATE);
                            String str_total_day = obj_balance.getString(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                            String str_subject = obj_balance.getString(TAG_LEAVE_REQUEST_SUBJECT);
                            String str_reason = obj_balance.getString(TAG_LEAVE_REQUEST_REASON);
                            String str_status = obj_balance.getString(TAG_LEAVE_REQUEST_STATUS);
                            String str_cancel_status = obj_balance.getString(TAG_LEAVE_CANCEL_STATUS);


                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_ID, str_leave_id);
                            map.put(TAG_LEAVE_START_DATE, str_start_date);
                            map.put(TAG_LEAVE_END_DATE, str_end_date);
                            map.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, str_total_day);
                            map.put(TAG_LEAVE_REQUEST_SUBJECT, str_subject);
                            map.put(TAG_LEAVE_REQUEST_REASON, str_reason);
                            map.put(TAG_LEAVE_REQUEST_STATUS, str_status);
                            map.put(TAG_LEAVE_CANCEL_STATUS, str_cancel_status);

                            arrayList_leave_request.add(map);

                        }

                        list_view_leave_request.setVisibility(View.VISIBLE);
                        text_message.setVisibility(View.GONE);

                        if (array_request.length() == 0){

                            list_view_leave_request.setVisibility(View.GONE);
                            text_message.setVisibility(View.VISIBLE);
                        }

                        swipe_refresh.setRefreshing(false);


                    } else if (status == 400) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        swipe_refresh.setRefreshing(false);

                        TastyToast.makeText( Activity_Add_Leave.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText( Activity_Add_Leave.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List( Activity_Add_Leave.this, arrayList_leave_request);
                    list_view_leave_request.setAdapter(adapter_leave_request_list);

                    swipe_refresh.setRefreshing(false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### JSONException "+e.getLocalizedMessage());
                }

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_LEAVE_LIST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_LIST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Add_Leave.this);

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

    /********************************
     *FUNCTION CANCEl LEAVE
     *********************************/
    private void Function_Cancel_Leave() {

        System.out.println("### AppConfig.URL_LEAVE_CANCEL " + AppConfig.URL_LEAVE_CANCEL);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_CANCEL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_CANCEL : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        new AlertDialog.Builder(Activity_Add_Leave.this)
                                .setTitle("Cancel Leave")
                                .setMessage("Leave Cancelled Successfully.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        onRefresh();

                                    }
                                }).show();

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.URL_LEAVE_CANCEL onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_CANCEL onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Add_Leave.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("leave_id", str_leave_id);

                System.out.println("### AppConfig.URL_LEAVE_CANCEL " + "leave_id" + " : " + str_leave_id);


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
     *FUNCTION CANCEl LEAVE
     *********************************/
    private void Function_Cancel_Leave_Request() {

        System.out.println("### AppConfig.URL_LEAVE_CANCEL_REQUEST " + AppConfig.URL_LEAVE_CANCEL_REQUEST);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_CANCEL_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_CANCEL_REQUEST : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        new AlertDialog.Builder(Activity_Add_Leave.this)
                                .setTitle("Cancel Leave Request")
                                .setMessage("Leave Request Cancelled Successfully.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        onRefresh();

                                    }
                                }).show();

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.URL_LEAVE_CANCEL_REQUEST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_CANCEL_REQUEST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Add_Leave.this);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("leave_id", str_leave_id);

                System.out.println("### AppConfig.URL_LEAVE_CANCEL_REQUEST " + "leave_id" + " : " + str_leave_id);


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
     *FUNCTION LEAVE REQUEST
     *********************************/
    private void Function_Leave_Apply() {

        System.out.println("### AppConfig.URL_LEAVE_APPLY " + AppConfig.URL_LEAVE_APPLY);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_APPLY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_APPLY : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        edit_subject.setText("");
                        edit_start_date.setText("");
                        edit_end_date.setText("");
                        edit_description.setText("");

                        new AlertDialog.Builder(Activity_Add_Leave.this)
                                .setTitle("Leave Application")
                                .setMessage("Leave Applied Successfully. Waiting For Approval.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        onRefresh();

                                    }
                                }).show();

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.URL_LEAVE_APPLY onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_APPLY onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Add_Leave.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("employee_id", str_user_id);
                params.put(TAG_LEAVE_NO_OF_DAYS, str_no_of_days);
                params.put(TAG_LEAVE_SUBJECT, str_subject);
                params.put(TAG_LEAVE_APPLY_REASON, str_description);
                params.put(TAG_LEAVE_FROM_DATE, str_selected_from_date);
                params.put(TAG_LEAVE_TO_DATE, str_selected_to_date);
                params.put(TAG_USER_ROLE, str_user_role);

                System.out.println("###" + "employee_id" + " : " + str_user_id);
                System.out.println("###" + TAG_LEAVE_NO_OF_DAYS + " : " + str_no_of_days);
                System.out.println("###" + TAG_LEAVE_SUBJECT + " : " + str_subject);
                System.out.println("###" + TAG_LEAVE_APPLY_REASON + " : " + str_description);
                System.out.println("###" + TAG_LEAVE_FROM_DATE + " : " + str_selected_from_date);
                System.out.println("###" + TAG_LEAVE_TO_DATE + " : " + str_selected_to_date);
                System.out.println("###" + TAG_USER_ROLE + " : " + str_user_role);

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

    public void Function_Verify_Network_Available(Context context) {
        try {
            if (!utility.IsNetworkAvailable(this)) {
                utility.Function_Show_Not_Network_Message(this);
            }
            ;
        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }

    public void Alert_Cancel_Reqeust(final String str_leave_request_status){

        System.out.println("### Alert_Cancel_Reqeust");

        String str_title = "";
        String str_message = "";

        if (str_leave_request_status.equals("Pending")){
            str_title = "Cancel Leave ";
            str_message = "Do you want to Cancel Leave";
        }else{
            str_title = "Cancel Leave Request";
            str_message = "Do you want to Cancel Leave Request";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(str_title);
        builder.setMessage(str_message);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {

                if (str_leave_request_status.equals("Pending")){

                    dialog = new SpotsDialog(Activity_Add_Leave.this);
                    dialog.show();

                    queue = Volley.newRequestQueue(Activity_Add_Leave.this);
                    Function_Cancel_Leave();

                }else if (str_leave_request_status.equals("Approved")){

                    dialog = new SpotsDialog(Activity_Add_Leave.this);
                    dialog.show();

                    queue = Volley.newRequestQueue(Activity_Add_Leave.this);
                    Function_Cancel_Leave_Request();

                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onRefresh() {

        swipe_refresh.setRefreshing(true);

        try {

            arrayList_leave_request.clear();

            queue = Volley.newRequestQueue( Activity_Add_Leave.this);
            Function_Leave_List();

        } catch (Exception e) {
            System.out.println("### Exception " + e.getLocalizedMessage());
        }

    }
}
