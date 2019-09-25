package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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
    private EditText edit_subject, edit_start_date, edit_end_date, edit_description;
    private TextView text_leave_status;
    private Session_Manager session;
    private String str_user_id = "", str_user_name = "", str_subject = "", str_selected_from_date = "",
            str_selected_to_date = "", str_description = "", str_user_role = "", str_no_of_days = "",
            str_leave_type = "", str_leave_type_id = "";

    private SpotsDialog dialog;

    private RequestQueue queue;

    private ArrayList<HashMap<String, String>> arrayList_leave_type;

    private Utility utility;

    private int mYear, mMonth, mDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();


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
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

            Date date_selected_from_date = format.parse(str_leave_from_date);
            String format_from_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_from_date.getTime());
            edit_start_date.setText(format_from_date);

            Date date_selected_to_date = format.parse(str_leave_to_date);
            String format_to_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_to_date.getTime());
            edit_end_date.setText(format_to_date);

            edit_subject.setText(str_leave_subject);
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

                Intent intent = new Intent(Activity_Leave_Details.this, Activity_Attendance_Report_For_Months.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
            }
        });

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
