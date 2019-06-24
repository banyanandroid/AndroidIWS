package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.banyan.androidiws.adapter.Adapter_Leave_Type_Details_List;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Leave_Details extends AppCompatActivity {

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_USER_ROLE = "userrole";

    public static final String TAG_LEAVE_FROM_DATE = "from_date";
    public static final String TAG_LEAVE_TO_DATE = "to_date";
    public static final String TAG_LEAVE_APPLY_REASON = "apply_reason";
    public static final String TAG_LEAVE_NO_OF_DAYS = "no_of_days";
    public static final String TAG_LEAVE_LEAVE_TYPE = "leave_type";
    public static final String TAG_LEAVE_LEAVE_TYPE_ID = "leave_type_id";
    public static final String TAG_LEAVE_SUBJECT = "subject";
    public static final String TAG_LEAVE_STATUS = "status";


    public static final String TAG_LEAVE_ID = "leave_id";
    public static final String TAG_LEAVE_NAME = "leave_name";
    public static final String TAG_LEAVE_SELECTED = "leave_selected";

    public static String str_selected_leave_type_id = "";

    private ImageView img_close;
    private RecyclerView recycler_view_leave_type;
    private EditText edit_subject, edit_start_date, edit_end_date, edit_description;
    private TextView text_leave_status;
    private Session_Manager session;
    private String str_user_id = "", str_user_name = "", str_subject = "", str_selected_from_date = "",
            str_selected_to_date = "", str_description = "", str_user_role = "", str_no_of_days = "",
            str_leave_type = "", str_leave_type_id = "";

    private SpotsDialog dialog;

    private RequestQueue queue;

    private ArrayList<HashMap<String, String>> arrayList_leave_type;

    private Util utility;

    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);

        /*********************************
         * SETUP
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

        img_close = (ImageView) findViewById(R.id.img_close);
        recycler_view_leave_type = (RecyclerView) findViewById(R.id.recycler_view_leave_type);
        edit_subject = (EditText) findViewById(R.id.edit_subject);
        edit_start_date = (EditText) findViewById(R.id.edit_start_date);
        edit_end_date = (EditText) findViewById(R.id.edit_end_date);
        edit_description = (EditText) findViewById(R.id.edit_description);
        text_leave_status = (TextView) findViewById(R.id.text_leave_status);

        edit_subject.setKeyListener(null);
        edit_start_date.setKeyListener(null);
        edit_end_date.setKeyListener(null);
        edit_description.setKeyListener(null);
        text_leave_status.setKeyListener(null);


        str_selected_leave_type_id = "";
        arrayList_leave_type = new ArrayList<>();


        HashMap<String, String> map = new HashMap<>();
        map.put(TAG_LEAVE_ID, "1");
        map.put(TAG_LEAVE_NAME, "Medical Leave");
        map.put(TAG_LEAVE_SELECTED, "true");

        arrayList_leave_type.add(map);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_Leave_Details.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_leave_type.setLayoutManager(layoutManager);
        ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(Activity_Leave_Details.this, R.dimen.dimen_horizontal);
        recycler_view_leave_type.addItemDecoration(itemDecoration);

        Adapter_Leave_Type_Details_List adapter_leave_type_details_list = new Adapter_Leave_Type_Details_List(Activity_Leave_Details.this, arrayList_leave_type);
        recycler_view_leave_type.setAdapter(adapter_leave_type_details_list);

      /*  //get data
        try {

            dialog = new SpotsDialog(this);
            dialog.show();

            queue = Volley.newRequestQueue(this);
            Function_Leave_Type_List();

        } catch (Exception e) {
            System.out.println("### Exception " + e.getLocalizedMessage());
        }*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String str_leave_from_date = sharedPreferences.getString(TAG_LEAVE_FROM_DATE, "");
        String str_leave_to_date = sharedPreferences.getString(TAG_LEAVE_TO_DATE, "");
        String str_leave_reason = sharedPreferences.getString(TAG_LEAVE_APPLY_REASON, "");
        String str_leave_no_of_days = sharedPreferences.getString(TAG_LEAVE_NO_OF_DAYS, "");
        str_leave_type = sharedPreferences.getString(TAG_LEAVE_LEAVE_TYPE, "");
        str_leave_type_id = sharedPreferences.getString(TAG_LEAVE_LEAVE_TYPE_ID, "");
        String str_leave_subject = sharedPreferences.getString(TAG_LEAVE_SUBJECT, "");
        String str_leave_status = sharedPreferences.getString(TAG_LEAVE_STATUS, "0");

        System.out.println("### str_leave_type_id " + str_leave_type_id);
        //set data
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            Date date_selected_from_date = format.parse(str_leave_from_date);
            String format_from_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_from_date.getTime());
            edit_start_date.setText(format_from_date);

            Date date_selected_to_date = format.parse(str_leave_to_date);
            String format_to_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_to_date.getTime());
            edit_end_date.setText(format_to_date);

            edit_subject.setText(str_leave_reason);
            edit_description.setText(str_leave_reason);
            text_leave_status.setText(str_leave_status);

            if (str_leave_status.endsWith("Approved")) {
                text_leave_status.setBackground(getResources().getDrawable(R.drawable.bg_button_green));
            }else if (str_leave_status.endsWith("Pending")) {
                text_leave_status.setBackground(getResources().getDrawable(R.drawable.bg_button_grey));
            }else if (str_leave_status.endsWith("Rejected")) {
                text_leave_status.setBackground(getResources().getDrawable(R.drawable.bg_button_red));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //action
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Activity_Leave_Details.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
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
                            String str_leave_selected = "NO";

                            System.out.println("###  str_leave_name " + str_leave_name);
                            System.out.println("###  str_leave_type " + str_leave_type);

                            if (str_leave_name.equals(str_leave_type)) {
                                str_leave_selected = "YES";
                            }
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_ID, str_leave_id);
                            map.put(TAG_LEAVE_NAME, str_leave_name);
                            map.put(TAG_LEAVE_SELECTED, str_leave_selected);

                            arrayList_leave_type.add(map);

                        }

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Leave_Details.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_type.clear();

                        dialog.dismiss();
                        TastyToast.makeText(Activity_Leave_Details.this, "Oops! Something Went Wrong, Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_Leave_Details.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler_view_leave_type.setLayoutManager(layoutManager);
                    ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(Activity_Leave_Details.this, R.dimen.dimen_horizontal);
                    recycler_view_leave_type.addItemDecoration(itemDecoration);

                    Adapter_Leave_Type_Details_List adapter_leave_type_details_list = new Adapter_Leave_Type_Details_List(Activity_Leave_Details.this, arrayList_leave_type);
                    recycler_view_leave_type.setAdapter(adapter_leave_type_details_list);

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

                TastyToast.makeText(Activity_Leave_Details.this,"Something Went Wrong, Try Again Later", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_TYPE onErrorResponse " + error.getLocalizedMessage());
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
