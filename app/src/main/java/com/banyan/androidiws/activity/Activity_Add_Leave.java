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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Leave_Type_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.ItemOffSetDecorator;
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


public class Activity_Add_Leave extends AppCompatActivity {

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USER_ROLE = "userrole";
    
    public static final String TAG_LEAVE_FROM_DATE = "from_date";
    public static final String TAG_LEAVE_TO_DATE = "to_date";
    public static final String TAG_LEAVE_APPLY_REASON = "apply_reason";
    public static final String TAG_LEAVE_NO_OF_DAYS = "no_of_days";
    public static final String TAG_LEAVE_LEAVE_TYPE = "leave_type";
    public static final String TAG_LEAVE_SUBJECT = "subject";

    public static final String TAG_LEAVE_ID = "leave_id";
    public static final String TAG_LEAVE_NAME = "leave_name";

    public static String str_selected_leave_type_id = "" ;

    private ImageView img_close;
    private RecyclerView recycler_view_leave_type;
    private EditText edit_subject, edit_start_date, edit_end_date, edit_description;
    private Button button_apply_leave;

    private Session_Manager session;
    private String str_user_id = "", str_user_name ="", str_subject = "", str_selected_from_date = "",
            str_selected_to_date = "", str_description = "", str_user_role = "", str_no_of_days = "";

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Util utility;

    private ArrayList<HashMap<String, String>> arrayList_leave_type;


    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__leave);

        /*********************************
         *  SETUP
         **********************************/
        utility = new Util();

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

        img_close =(ImageView)findViewById(R.id.img_close);
        recycler_view_leave_type =(RecyclerView)findViewById(R.id.recycler_view_leave_type);
        edit_subject =(EditText)findViewById(R.id.edit_subject);
        edit_start_date =(EditText)findViewById(R.id.edit_start_date);
        edit_end_date =(EditText)findViewById(R.id.edit_end_date);
        edit_description =(EditText)findViewById(R.id.edit_description);
        button_apply_leave =(Button)findViewById(R.id.button_apply_leave);

        edit_start_date.setKeyListener(null);
        edit_end_date.setKeyListener(null);



        str_selected_leave_type_id = "";
        arrayList_leave_type = new ArrayList<>();


        HashMap<String, String> map = new HashMap<>();
        map.put(TAG_LEAVE_ID, "1");
        map.put(TAG_LEAVE_NAME, "Casual Leave");

        arrayList_leave_type.add(map);

        HashMap<String, String> map2 = new HashMap<>();
        map2.put(TAG_LEAVE_ID, "2");
        map2.put(TAG_LEAVE_NAME, "Medical Leave");

        arrayList_leave_type.add(map2);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_Add_Leave.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_leave_type.setLayoutManager(layoutManager);
        ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(Activity_Add_Leave.this, R.dimen.dimen_horizontal);
        recycler_view_leave_type.addItemDecoration(itemDecoration);

        Adapter_Leave_Type_List adapter_leave_type_list = new Adapter_Leave_Type_List(Activity_Add_Leave.this, arrayList_leave_type);
        recycler_view_leave_type.setAdapter(adapter_leave_type_list);



        /*//get data


        try {

            dialog = new SpotsDialog(this);
            dialog.show();

            queue = Volley.newRequestQueue(this);
            Function_Leave_Type_List();

        } catch (Exception e) {
            System.out.println("### Exception " + e.getLocalizedMessage());
        }*/

        //action
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activity_Add_Leave.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);  // close activity
            }
        });

                edit_start_date.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//your code
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
                }


                return false;
            }
        });


        edit_end_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                    String str_start_date = edit_start_date.getText().toString();

                    if (str_start_date.isEmpty()){ // start date not selected

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

                    }else{ // start date selected


                        try {
                            //get start date
                            String[] array_star_date = str_selected_from_date.split("-");
                            String str_day = array_star_date[0];
                            String str_month = array_star_date[1];
                            String str_year = array_star_date[2];
                            String str_star_date = str_day+"-"+str_month+"-"+str_year;
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


                new AlertDialog.Builder(Activity_Add_Leave.this)
                        .setTitle("Leave Application")
                        .setMessage("Leave Applied Successfully. Waiting For Approval.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Add_Leave.this);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(Activity_Main_Not_Used.TAG_CALLING_ACTIVITY, Activity_Main_Not_Used.TAG_ACTIVITY_LEAVE_REQUEST);
                                editor.commit();

                                Intent intent = new Intent(Activity_Add_Leave.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                            }
                        }).show();

                /*

                str_subject = edit_subject.getText().toString();
//                str_selected_from_date
//                str_selected_to_date
                str_description = edit_description.getText().toString();
//                str_selected_leave_type_id


                if (str_selected_leave_type_id.isEmpty()){
                    TastyToast.makeText(Activity_Add_Leave.this, "Select Leave Type", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_subject.isEmpty()){
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
                        str_no_of_days = new Util().getCountOfDays(str_selected_from_date, str_selected_to_date);

                    System.out.println("### str_no_of_days "+str_no_of_days);

                    try {

                        dialog = new SpotsDialog(Activity_Add_Leave.this);
                        dialog.show();

                        queue = Volley.newRequestQueue(Activity_Add_Leave.this);
                        Function_Leave_Apply();

                    } catch (Exception e) {
                        System.out.println("### Exception " + e.getLocalizedMessage());
                    }

                }*/

            }
        });
    }

    /********************************
     *FUNCTION LEAVE LIST
     *********************************/
    private void Function_Leave_Type_List() {

        System.out.println("### AppConfig.URL_LEAVE_TYPE " + AppConfig.URL_LEAVE_TYPE);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_TYPE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_TYPE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 200) {

                        JSONArray array_leave_type = obj.getJSONArray("records");

                        for (int count = 0; count < array_leave_type.length(); count++) {
                            JSONObject obj_leave_type = array_leave_type.getJSONObject(count);

                            String str_leave_id = obj_leave_type.getString(TAG_LEAVE_ID);
                            String str_leave_name = obj_leave_type.getString(TAG_LEAVE_NAME);


                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_ID, str_leave_id);
                            map.put(TAG_LEAVE_NAME, str_leave_name);

                            arrayList_leave_type.add(map);

                        }

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Add_Leave.this, "Oops! Something Went Wrong, Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_Add_Leave.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler_view_leave_type.setLayoutManager(layoutManager);
                    ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(Activity_Add_Leave.this, R.dimen.dimen_horizontal);
                    recycler_view_leave_type.addItemDecoration(itemDecoration);

                    Adapter_Leave_Type_List adapter_leave_type_list = new Adapter_Leave_Type_List(Activity_Add_Leave.this, arrayList_leave_type);
                    recycler_view_leave_type.setAdapter(adapter_leave_type_list);

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

                System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse " + error.getLocalizedMessage());

                TastyToast.makeText(Activity_Add_Leave.this, "Something Went Wrong, Try Again Later", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

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

                        new AlertDialog.Builder(Activity_Add_Leave.this)
                                .setTitle("Leave Application")
                                .setMessage("Leave Applied Successfully. Waiting For Approval.")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Add_Leave.this);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(Activity_Main_Not_Used.TAG_CALLING_ACTIVITY, Activity_Main_Not_Used.TAG_ACTIVITY_LEAVE_REQUEST);
                                        editor.commit();

                                        Intent intent = new Intent(Activity_Add_Leave.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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

                new AlertDialog.Builder(Activity_Add_Leave.this)
                        .setTitle(R.string.app_name)
                        .setMessage("Something Went Wrong, Try Again Later.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                finishAffinity();

                            }
                        }).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_ROLE, str_user_role);
                params.put(TAG_LEAVE_FROM_DATE, str_selected_from_date);
                params.put(TAG_LEAVE_TO_DATE, str_selected_to_date);
                params.put(TAG_LEAVE_NO_OF_DAYS, str_no_of_days);
                params.put(TAG_LEAVE_LEAVE_TYPE, str_selected_leave_type_id);
                params.put(TAG_LEAVE_SUBJECT, str_subject);
                params.put(TAG_LEAVE_APPLY_REASON, str_description);

                System.out.println("###" + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_USER_ROLE + " : " + str_user_role);
                System.out.println("###" + TAG_LEAVE_FROM_DATE + " : " + str_selected_from_date);
                System.out.println("###" + TAG_LEAVE_TO_DATE + " : " + str_selected_to_date);
                System.out.println("###" + TAG_LEAVE_NO_OF_DAYS + " : " + str_no_of_days);
                System.out.println("###" + TAG_LEAVE_LEAVE_TYPE + " : " + str_selected_leave_type_id);
                System.out.println("###" + TAG_LEAVE_SUBJECT + " : " + str_subject);
                System.out.println("###" + TAG_LEAVE_APPLY_REASON + " : " + str_description);

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
