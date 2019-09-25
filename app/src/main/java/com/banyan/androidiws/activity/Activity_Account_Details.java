package com.banyan.androidiws.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
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
import com.banyan.androidiws.adapter.Adapter_Children_List;
import com.banyan.androidiws.adapter.Adapter_Document_List;
import com.banyan.androidiws.adapter.Adapter_Experience_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.NestedListview;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.sdsmdg.tastytoast.TastyToast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class Activity_Account_Details extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_USER_ID = "userid";

    public static final String TAG_SALUTE = "salute";
    public static final String TAG_FIRST_NAME = "fname";
    public static final String TAG_EMPDA = "empda";
    public static final String TAG_LAST_NAME = "lname";
    public static final String TAG_DOB = "dob";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_DESIGNATION = "designation";
    public static final String TAG_JOB_PROFILE = "job_profile";
    public static final String TAG_HEADER = "header";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_BLOOD_GROUP = "blood_group";
    public static final String TAG_JOINING_DATE = "joining_date";
    public static final String TAG_PERSONAL_CONTACT = "personal_contal";
    public static final String TAG_SECONDARY_CONTACT = "secondary_contact";
    public static final String TAG_PERSONAL_EMAIL = "personal_email";
    public static final String TAG_ADDRESS_1 = "address1";
    public static final String TAG_ADDRESS_2 = "address2";
    public static final String TAG_CITY = "city";
    public static final String TAG_STATE = "state";
    public static final String TAG_POSTAL = "postal";
    public static final String TAG_TEMP_ADDRESS = "temp_address1";
    public static final String TAG_TEMP_ADDRESS_2 = "temp_address2";
    public static final String TAG_TEMP_CITY = "temp_city";
    public static final String TAG_TEMP_STATE = "temp_state";
    public static final String TAG_TEMP_POSTAL = "temp_postal";
    public static final String TAG_COMP_COUNTRY = "comp_country";
    public static final String TAG_COMP_STATE = "comp_state";
    public static final String TAG_COMP_CITY = "comp_city";
    public static final String TAG_PROJECT_NAME = "project_name";
    public static final String TAG_PROJECT_ID = "project_id";
    public static final String TAG_PROJECT_TYPE = "project_type";
    public static final String TAG_MARRITAL_STATUS = "marrital_status";
    public static final String TAG_CHILD = "child";
    public static final String TAG_ACCOUNT_HOLDER_NAME = "ac_holdername";
    public static final String TAG_ACCOUNT_NUMBER = "ac_number";
    public static final String TAG_BRANCH_NAME = "branch_name";
    public static final String TAG_BRANCH_ADDRESS = "bank_address";
    public static final String TAG_IFSC_CODE = "ifsc_code";
    public static final String TAG_EMERGENCY_NAME = "emergency_name";
    public static final String TAG_EMERGENCY_CONTACT_NO = "emergency_contactno";
    public static final String TAG_HOME_CONTACT_NO = "home_contactno";
    public static final String TAG_AADHAR_NO = "aadharno";
    public static final String TAG_PAN_NO = "panno";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_PHOTO = "profile_photo";
    public static final String TAG_ID_PROOFS = "id_proofs";
    public static final String TAG_ID_NAME = "name";
    public static final String TAG_ID_PATH = "id_path";
    public static final String TAG_OTHER_DOCS = "other_docs";
    public static final String TAG_EXPERIENCE = "experience";

    public static final String TAG_COMPANY_NAME = "company_name";
    public static final String TAG_WORK_FROM = "work_from";
    public static final String TAG_WORK_TO = "work_to";
    public static final String TAG_SALARTY = "salary";

    public static final String TAG_CHILDREN = "children";
    public static final String TAG_CHILDREN_NAME = "children_name";
    public static final String TAG_CHILDREN_DOB = "children_dob";
    public static final String TAG_REPORT_MANAGER = "report_manager";
    public static final String TAG_LINE_MANAGER = "linemanager";
    public static final String TAG_EMPLOYEE_LEVEL = "level";
    public static final String TAG_INSURENCE_NO = "insurance_no";
    public static final String TAG_PF_NUMBER = "pf_number";
    public static final String TAG_ESI_NUMBER = "esi_number";

    public static final String TAG_CERTIFICATE = "certificate";
    public static final String TAG_CERTIFICATE_MFC = "mfc_no";
    public static final String TAG_CERTIFICATE_MFC_COLOR = "mfc_color";
    public static final String TAG_CERTIFICATE_FAT_NO = "fat_no";
    public static final String TAG_CERTIFICATE_FAT_COLOR = "fat_color";
    public static final String TAG_CERTIFICATE_FARM_NO = "farm_no";
    public static final String TAG_CERTIFICATE_FORM_COLOR = "farm_color";
    public static final String TAG_CERTIFICATE_WH_NO = "wh_no";
    public static final String TAG_CERTIFICATE_WH_COLOR = "wh_color";
    public static final String TAG_CERTIFICATE_PTI_NO = "ptidno";
    public static final String TAG_CERTIFICATE_PTI_COLOR = "ptid_color";
    public static final String TAG_CERTIFICATE_SQPID_NO = "sqpid_no";
    public static final String TAG_CERTIFICATE_SQPID_COLOR = "sqpid_color";
    public static final String TAG_CERTIFICATE_TTP_NO = "ttp_no";
    public static final String TAG_CERTIFICATE_TTP_COLOR = "ttp_color";

    public static final String TAG_OLD_PASSWORD = "oldpassword";
    public static final String TAG_NEW_PASSWORD = "newpassword";
    public static final String TAG_USER_TYPE = "usertype";
    public static final String TAG_USER_ROLE = "userrole";

    private Session_Manager session;

    private Toolbar toolbar;

    private CircleImageView circle_image_profile;

    private TextInputLayout text_layout_mfc_no_title, text_layout_fat_no_title, text_layout_farm_no_title, text_layout_ptid_no_title,
            text_layout_sqpid_no_title, text_layout_ttp_no_title, text_layout_wh_no_title;

    private TextView text_name, text_designation, text_view_child, text_view_message_id_proofs,
            text_view_message_other_document, text_view_message_experience;

    private EditText edit_first_name, edit_last_name, edit_date_of_birth, edit_email,
            edit_designation, edit_address_1, edit_address_2, edit_city,
            edit_aadhar_no, edit_pan_no, edit_gender, edit_blood_group,
            edit_personal_email, edit_secondary_contact, edit_da, edit_state, edit_pin, edit_temp_address_1,
            edit_temp_address_2, edit_temp_city, edit_temp_state, edit_temp_pin,
            edit_job_profile, edit_header, edit_category, edit_joining_date,
            edit_personal_contact, edit_account_holder_name,
            edit_account_number, edit_branch_name, edit_branch_address,
            edit_ifsc_code, edit_emergency_name, edit_emergency_contact,
            edit_home_contact, edit_pf_no, edit_esi_no, edit_insurence_no,
            edit_mfc_no, edit_fat_no, edit_farm_no, edit_ptid_no, edit_sqpid_no, edit_ttp_no, edit_wh_no;

    private SwipeRefreshLayout swipe_refresh;

    private NestedListview nested_list_view_childred, nested_list_view_id_proofs,
            nested_list_view_other_document, nested_list_view_experience;

    private SearchableSpinner seach_spinner_marriage_status, seach_spinner_child;

    private LinearLayoutCompat linear_layout_edit_profile;

    private Button button_change_password, button_submit;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Utility utility;

    private ArrayList<HashMap<String, String>> array_id_proof = new ArrayList<>();
    private ArrayList<HashMap<String, String>> array_other_doc = new ArrayList<>();
    private ArrayList<HashMap<String, String>> array_experience = new ArrayList<>();
    private ArrayList<HashMap<String, String>> array_children = new ArrayList<>();

    private String str_user_id = "", str_user_name = "", str_user_type = "", str_user_role = "",
            str_old_password = "", str_new_password = "", str_marriage_status = "", str_children = "",
            str_email = "", str_personal_email = "", str_personal_contact = "", str_secondary_contact = "",
            str_aadhar_no = "", str_pan_no = "", str_address_1 = "", str_address_2 = "", str_city = "",
            str_state = "", str_pin = "", str_temparary_address_1 = "", str_temparary_address_2 = "",
            str_temparary_city = "", str_temparary_state = "", str_temparary_pin = "", str_designation = "",
            str_job_profile = "", str_header = "", str_category = "", str_join_date = "", str_dearness_allowance = "",
            str_account_holder_name = "", str_account_number = "", str_branch_name = "", str_branch_address = "",
            str_ifsc_code = "", str_emergency_name = "", str_emergency_contact = "", str_home_contact;

    private int mYear, mMonth, mDay, int_spinner_marriage_selected = 0, int_spinner_child_selected = 0, int_certificate_warning_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();

        Function_Verify_Network_Available(this);

        /*****************************
         *  SESSION
         *****************************/

        session = new Session_Manager(this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
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
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Account_Details.this, Activity_Main.class);
                startActivity(intent);
            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        circle_image_profile = (CircleImageView) findViewById(R.id.circle_image_profile);

        text_name = (TextView) findViewById(R.id.text_name);
        text_designation = (TextView) findViewById(R.id.text_designation);
        text_view_child = (TextView) findViewById(R.id.text_view_child);
        text_view_message_id_proofs = (TextView) findViewById(R.id.text_view_message_id_proofs);
        text_view_message_other_document = (TextView) findViewById(R.id.text_view_message_other_document);
        text_view_message_experience = (TextView) findViewById(R.id.text_view_message_experience);

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
        edit_blood_group = (EditText) findViewById(R.id.edit_blood_group);
        edit_personal_email = (EditText) findViewById(R.id.edit_personal_email);
        edit_secondary_contact = (EditText) findViewById(R.id.edit_secondary_contact);
        edit_da = (EditText) findViewById(R.id.edit_da);
        edit_state = (EditText) findViewById(R.id.edit_state);
        edit_pin = (EditText) findViewById(R.id.edit_pin);
        edit_temp_address_1 = (EditText) findViewById(R.id.edit_temp_address_1);
        edit_temp_address_2 = (EditText) findViewById(R.id.edit_temp_address_2);
        edit_temp_city = (EditText) findViewById(R.id.edit_temp_city);
        edit_temp_state = (EditText) findViewById(R.id.edit_temp_state);
        edit_temp_pin = (EditText) findViewById(R.id.edit_temp_pin);
        edit_job_profile = (EditText) findViewById(R.id.edit_job_profile);
        edit_header = (EditText) findViewById(R.id.edit_header);
        edit_category = (EditText) findViewById(R.id.edit_category);
        edit_joining_date = (EditText) findViewById(R.id.edit_joining_date);
        edit_personal_contact = (EditText) findViewById(R.id.edit_personal_contact);
        edit_account_holder_name = (EditText) findViewById(R.id.edit_account_holder_name);
        edit_account_number = (EditText) findViewById(R.id.edit_account_number);
        edit_branch_name = (EditText) findViewById(R.id.edit_branch_name);
        edit_branch_address = (EditText) findViewById(R.id.edit_branch_address);
        edit_ifsc_code = (EditText) findViewById(R.id.edit_ifsc_code);
        edit_emergency_name = (EditText) findViewById(R.id.edit_emergency_name);
        edit_emergency_contact = (EditText) findViewById(R.id.edit_emergency_contact);
        edit_home_contact = (EditText) findViewById(R.id.edit_home_contact);
        edit_pf_no = (EditText) findViewById(R.id.edit_pf_no);
        edit_esi_no = (EditText) findViewById(R.id.edit_esi_no);
        edit_insurence_no = (EditText) findViewById(R.id.edit_insurence_no);
        edit_mfc_no = (EditText) findViewById(R.id.edit_mfc_no);
        edit_fat_no = (EditText) findViewById(R.id.edit_fat_no);
        edit_farm_no = (EditText) findViewById(R.id.edit_farm_no);
        edit_ptid_no = (EditText) findViewById(R.id.edit_ptid_no);
        edit_sqpid_no = (EditText) findViewById(R.id.edit_sqpid_no);
        edit_ttp_no = (EditText) findViewById(R.id.edit_ttp_no);
        edit_wh_no = (EditText) findViewById(R.id.edit_wh_no);



        text_layout_mfc_no_title = (TextInputLayout) findViewById(R.id.text_layout_mfc_no_title);
        text_layout_fat_no_title = (TextInputLayout) findViewById(R.id.text_layout_fat_no_title);
        text_layout_farm_no_title = (TextInputLayout) findViewById(R.id.text_layout_farm_no_title);
        text_layout_ptid_no_title = (TextInputLayout) findViewById(R.id.text_layout_ptid_no_title);
        text_layout_sqpid_no_title = (TextInputLayout) findViewById(R.id.text_layout_sqpid_no_title);
        text_layout_ttp_no_title = (TextInputLayout) findViewById(R.id.text_layout_ttp_no_title);
        text_layout_wh_no_title = (TextInputLayout) findViewById(R.id.text_layout_wh_no_title);

        seach_spinner_marriage_status = (SearchableSpinner) findViewById(R.id.seach_spinner_marriage_status);
        seach_spinner_child = (SearchableSpinner) findViewById(R.id.seach_spinner_child);

        button_change_password = (Button) findViewById(R.id.button_change_password);
        button_submit = (Button) findViewById(R.id.button_submit);

        nested_list_view_childred = (NestedListview) findViewById(R.id.nested_list_view_childred);
        nested_list_view_id_proofs = (NestedListview) findViewById(R.id.nested_list_view_id_proofs);
        nested_list_view_other_document = (NestedListview) findViewById(R.id.nested_list_view_other_document);
        nested_list_view_experience = (NestedListview) findViewById(R.id.nested_list_view_experience);

        linear_layout_edit_profile = (LinearLayoutCompat) findViewById(R.id.linear_layout_edit_profile);

        swipe_refresh.setOnRefreshListener(this);

        nested_list_view_id_proofs.setEmptyView(text_view_message_id_proofs);
        nested_list_view_other_document.setEmptyView(text_view_message_other_document);
        nested_list_view_experience.setEmptyView(text_view_message_experience);

        linear_layout_edit_profile.setVisibility(View.GONE);
        button_submit.setVisibility(View.GONE);

        text_view_child.setVisibility(View.GONE);
        nested_list_view_childred.setVisibility(View.GONE);

        Function_Disable_Fields();

        /*************************
         *  GET DATA
         *************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                array_id_proof.clear();
                array_children.clear();
                array_experience.clear();
                array_other_doc.clear();

                swipe_refresh.setRefreshing(true);

                queue = Volley.newRequestQueue(Activity_Account_Details.this);
                Function_Get_Profile();

            }
        });

        /*************************
         *  SET DATA
         *************************/

        ArrayList<String> arrayList_marriage_status = new ArrayList<>();
        arrayList_marriage_status.add("Single");
        arrayList_marriage_status.add("Married");

        ArrayList<String> arrayList_child = new ArrayList<>();
        arrayList_child.add("Yes");
        arrayList_child.add("No");


        ArrayAdapter<String> arrayAdapter_marriage = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_marriage_status);

        ArrayAdapter<String> arrayAdapter_child = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList_child);

        seach_spinner_marriage_status.setAdapter(arrayAdapter_marriage);
        seach_spinner_child.setAdapter(arrayAdapter_child);

        /*************************
         *  ACTION
         *************************/

        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Alert_Change_Password();

            }
        });

        linear_layout_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Function_Enable_Fields();

                button_submit.setVisibility(View.VISIBLE);

                TastyToast.makeText(Activity_Account_Details.this, "Now you can Edit Profile", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();
            }
        });

        seach_spinner_marriage_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("### int_spinner_marriage_selected" + int_spinner_marriage_selected);

                if (int_spinner_marriage_selected > 1) {

                    str_marriage_status = parent.getSelectedItem().toString();

                    if (str_marriage_status.equals("Married")) {

                        seach_spinner_child.setVisibility(View.VISIBLE);
                        text_view_child.setVisibility(View.VISIBLE);
                        nested_list_view_childred.setVisibility(View.VISIBLE);

                    } else {

                        seach_spinner_child.setVisibility(View.GONE);
                        text_view_child.setVisibility(View.GONE);
                        nested_list_view_childred.setVisibility(View.GONE);
                    }

                }
                int_spinner_marriage_selected++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        seach_spinner_child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("### int_spinner_child_selected" + int_spinner_child_selected);

                if (int_spinner_child_selected > 1) {

                    str_children = parent.getSelectedItem().toString();

                    if (str_children.equals("Yes")) {

                        text_view_child.setVisibility(View.VISIBLE);
                        nested_list_view_childred.setVisibility(View.VISIBLE);

                    } else {

                        text_view_child.setVisibility(View.GONE);
                        nested_list_view_childred.setVisibility(View.GONE);

                    }

                }

                int_spinner_child_selected++;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                str_marriage_status
//                str_children
                str_email = edit_email.getText().toString();
                str_personal_email = edit_personal_email.getText().toString();
                str_personal_contact = edit_personal_contact.getText().toString();
                str_secondary_contact = edit_secondary_contact.getText().toString();
                str_aadhar_no = edit_aadhar_no.getText().toString();
                str_pan_no = edit_pan_no.getText().toString();
                str_address_1 = edit_address_1.getText().toString();
                str_address_2 = edit_address_2.getText().toString();
                str_city = edit_city.getText().toString();
                str_state = edit_state.getText().toString();
                str_pin = edit_pin.getText().toString();
                str_temparary_address_1 = edit_temp_address_1.getText().toString();
                str_temparary_address_2 = edit_temp_address_2.getText().toString();
                str_temparary_city = edit_temp_city.getText().toString();
                str_temparary_state = edit_temp_state.getText().toString();
                str_temparary_pin = edit_temp_pin.getText().toString();
                str_designation = edit_designation.getText().toString();
                str_job_profile = edit_job_profile.getText().toString();
                str_header = edit_header.getText().toString();
                str_category = edit_category.getText().toString();
                str_join_date = edit_joining_date.getText().toString();
                str_dearness_allowance = edit_da.getText().toString();
                str_account_holder_name = edit_account_holder_name.getText().toString();
                str_account_number = edit_account_number.getText().toString();
                str_branch_name = edit_branch_name.getText().toString();
                str_branch_address = edit_branch_address.getText().toString();
                str_ifsc_code = edit_ifsc_code.getText().toString();
                str_emergency_name = edit_emergency_name.getText().toString();
                str_emergency_contact = edit_emergency_contact.getText().toString();
                str_home_contact = edit_home_contact.getText().toString();

                if (str_marriage_status.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Select Marriage Status", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_email.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Email Id", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_personal_email.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Personal Email Id", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_personal_contact.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Personal Contact", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_secondary_contact.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Secondary Contact", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_aadhar_no.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Aadhar No", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_pan_no.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter PAN No", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_address_1.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Address", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_address_2.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Address 2", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_city.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter City", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_state.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter State", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_pin.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter PIN", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_temparary_address_1.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Temporary Address", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_temparary_city.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Temparary City", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_temparary_state.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Temparary State", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_temparary_pin.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Temparary PIN", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_designation.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Designation", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_job_profile.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Job Profile", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_header.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Header", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_category.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Category", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_join_date.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Join Date", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_dearness_allowance.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Dearness Allowance", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_account_holder_name.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Account Holder Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_account_number.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Account Number", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_branch_name.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Branch Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_branch_address.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Branch Address", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_ifsc_code.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter IFSC Code", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_emergency_name.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Emergency Name", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_emergency_contact.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Emergency Contact", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_home_contact.isEmpty()) {
                    TastyToast.makeText(Activity_Account_Details.this, "Please Enter Home Contact", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    if (str_marriage_status.equals("Single")) {

                        dialog = new SpotsDialog(Activity_Account_Details.this);
                        dialog.show();
                        queue = Volley.newRequestQueue(Activity_Account_Details.this);
                        Function_Update_Profile();

                    } else if (str_marriage_status.equals("Married")) {

                        if (str_children.isEmpty()) {
                            TastyToast.makeText(Activity_Account_Details.this, "Please Select Child", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                        } else {

                            dialog = new SpotsDialog(Activity_Account_Details.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Account_Details.this);
                            Function_Update_Profile();

                        }
                    }

                }

            }
        });

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

    public void Function_Disable_Fields() {

        edit_first_name.setEnabled(false);
        edit_last_name.setEnabled(false);
        edit_date_of_birth.setEnabled(false);
        edit_email.setEnabled(false);
        edit_designation.setEnabled(false);
        edit_address_1.setEnabled(false);
        edit_address_2.setEnabled(false);
        edit_city.setEnabled(false);
        edit_aadhar_no.setEnabled(false);
        edit_pan_no.setEnabled(false);
        edit_gender.setEnabled(false);
        edit_blood_group.setEnabled(false);
        seach_spinner_marriage_status.setEnabled(false);
        seach_spinner_child.setEnabled(false);
        edit_personal_email.setEnabled(false);
        edit_secondary_contact.setEnabled(false);
        edit_da.setEnabled(false);
        edit_state.setEnabled(false);
        edit_pin.setEnabled(false);
        edit_temp_address_1.setEnabled(false);
        edit_temp_address_2.setEnabled(false);
        edit_temp_city.setEnabled(false);
        edit_temp_state.setEnabled(false);
        edit_temp_pin.setEnabled(false);
        edit_job_profile.setEnabled(false);
        edit_header.setEnabled(false);
        edit_category.setEnabled(false);
        edit_joining_date.setEnabled(false);
        edit_personal_contact.setEnabled(false);
        edit_account_holder_name.setEnabled(false);
        edit_account_number.setEnabled(false);
        edit_branch_name.setEnabled(false);
        edit_branch_address.setEnabled(false);
        edit_ifsc_code.setEnabled(false);
        edit_emergency_name.setEnabled(false);
        edit_emergency_contact.setEnabled(false);
        edit_home_contact.setEnabled(false);
        edit_pf_no.setEnabled(false);
        edit_esi_no.setEnabled(false);
        edit_insurence_no.setEnabled(false);

        edit_mfc_no.setEnabled(false);
        edit_fat_no.setEnabled(false);
        edit_farm_no.setEnabled(false);
        edit_wh_no.setEnabled(false);
        edit_ptid_no.setEnabled(false);
        edit_sqpid_no.setEnabled(false);
        edit_ttp_no.setEnabled(false);

    }

    public void Function_Enable_Fields() {

       /* edit_first_name.setEnabled(true);
        edit_last_name.setEnabled(true);
        edit_date_of_birth.setEnabled(true);
        edit_blood_group.setEnabled(true);
        edit_gender.setEnabled(true);*/

        edit_email.setEnabled(true);
        edit_designation.setEnabled(true);
        edit_address_1.setEnabled(true);
        edit_address_2.setEnabled(true);
        edit_city.setEnabled(true);
        edit_aadhar_no.setEnabled(true);
        edit_pan_no.setEnabled(true);

        seach_spinner_marriage_status.setEnabled(true);
        seach_spinner_child.setEnabled(true);
        edit_personal_email.setEnabled(true);
        edit_secondary_contact.setEnabled(true);
        edit_da.setEnabled(true);
        edit_state.setEnabled(true);
        edit_pin.setEnabled(true);
        edit_temp_address_1.setEnabled(true);
        edit_temp_address_2.setEnabled(true);
        edit_temp_city.setEnabled(true);
        edit_temp_state.setEnabled(true);
        edit_temp_pin.setEnabled(true);
        edit_job_profile.setEnabled(true);
        edit_header.setEnabled(true);
        edit_category.setEnabled(true);
        edit_joining_date.setEnabled(true);
        edit_personal_contact.setEnabled(true);
        edit_account_holder_name.setEnabled(true);
        edit_account_number.setEnabled(true);
        edit_branch_name.setEnabled(true);
        edit_branch_address.setEnabled(true);
        edit_ifsc_code.setEnabled(true);
        edit_emergency_name.setEnabled(true);
        edit_emergency_contact.setEnabled(true);
        edit_home_contact.setEnabled(true);
        edit_pf_no.setEnabled(true);
        edit_esi_no.setEnabled(true);
        edit_insurence_no.setEnabled(true);

        edit_mfc_no.setEnabled(true);
        edit_fat_no.setEnabled(true);
        edit_farm_no.setEnabled(true);
        edit_wh_no.setEnabled(true);
        edit_ptid_no.setEnabled(true);
        edit_sqpid_no.setEnabled(true);
        edit_ttp_no.setEnabled(true);
    }

    /********************************
     *FUNCTION CHANGE PASSWORD
     *********************************/
    private void Function_Update_Profile() {

        System.out.println("### AppConfig.URL_UPDATE_PROFILE " + AppConfig.URL_UPDATE_PROFILE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE_PROFILE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_UPDATE_PROFILE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        dialog.dismiss();

                        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Account_Details.this);
                        alertdialog_builder.setCancelable(false);
                        alertdialog_builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                                Intent intent = new Intent(Activity_Account_Details.this, Activity_Main.class);
                                startActivity(intent);
                                finish();

                            }
                        });

                        AlertDialog alertDialog = alertdialog_builder.create();
                        alertDialog.setTitle("Update Profile");
                        alertDialog.setMessage("Profile Updated Successfully.");
                        alertDialog.show();

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

                System.out.println("### AppConfig.URL_UPDATE_PROFILE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_UPDATE_PROFILE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Account_Details.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_TYPE, str_user_type);
                params.put(TAG_USER_ROLE, str_user_role);

                params.put(TAG_MARRITAL_STATUS, str_marriage_status);
                params.put(TAG_CHILD, str_children);
                params.put(TAG_EMAIL, str_email);
                params.put(TAG_PERSONAL_EMAIL, str_personal_email);
                params.put(TAG_PERSONAL_CONTACT, str_personal_contact);
                params.put(TAG_SECONDARY_CONTACT, str_secondary_contact);
                params.put(TAG_AADHAR_NO, str_aadhar_no);
                params.put(TAG_PAN_NO, str_pan_no);
                params.put(TAG_ADDRESS_1, str_address_1);
                params.put(TAG_ADDRESS_2, str_address_2);
                params.put(TAG_CITY, str_city);
                params.put(TAG_STATE, str_state);
                params.put(TAG_POSTAL, str_pin);
                params.put(TAG_TEMP_ADDRESS, str_temparary_address_1);
                params.put(TAG_TEMP_ADDRESS_2, str_temparary_address_2);
                params.put(TAG_TEMP_CITY, str_temparary_city);
                params.put(TAG_TEMP_STATE, str_temparary_state);
                params.put(TAG_TEMP_POSTAL, str_temparary_pin);
                params.put(TAG_DESIGNATION, str_designation);
                params.put(TAG_JOB_PROFILE, str_job_profile);
                params.put(TAG_HEADER, str_header);
                params.put(TAG_CATEGORY, str_category);
                params.put(TAG_JOINING_DATE, str_join_date);
                params.put(TAG_ACCOUNT_HOLDER_NAME, str_account_holder_name);
                params.put(TAG_ACCOUNT_NUMBER, str_account_number);
                params.put(TAG_BRANCH_NAME, str_branch_name);
                params.put(TAG_BRANCH_ADDRESS, str_branch_address);
                params.put(TAG_IFSC_CODE, str_ifsc_code);
                params.put(TAG_EMERGENCY_NAME, str_emergency_name);
                params.put(TAG_EMERGENCY_CONTACT_NO, str_emergency_contact);
                params.put(TAG_HOME_CONTACT_NO, str_home_contact);

                System.out.println("### URL_UPDATE_PROFILE " + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_USER_TYPE + " : " + str_user_type);
                System.out.println("###" + TAG_USER_ROLE + " : " + str_user_role);
                System.out.println("###" + TAG_MARRITAL_STATUS + " : " + str_marriage_status);
                System.out.println("###" + TAG_CHILD + " : " + str_children);
                System.out.println("###" + TAG_EMAIL + " : " + str_email);
                System.out.println("###" + TAG_PERSONAL_EMAIL + " : " + str_personal_email);
                System.out.println("###" + TAG_PERSONAL_CONTACT + " : " + str_personal_contact);
                System.out.println("###" + TAG_SECONDARY_CONTACT + " : " + str_secondary_contact);
                System.out.println("###" + TAG_AADHAR_NO + " : " + str_aadhar_no);
                System.out.println("###" + TAG_PAN_NO + " : " + str_pan_no);
                System.out.println("###" + TAG_ADDRESS_1 + " : " + str_address_1);
                System.out.println("###" + TAG_ADDRESS_2 + " : " + str_address_2);
                System.out.println("###" + TAG_CITY + " : " + str_city);
                System.out.println("###" + TAG_STATE + " : " + str_state);
                System.out.println("###" + TAG_POSTAL + " : " + str_pin);
                System.out.println("###" + TAG_TEMP_ADDRESS + " : " + str_temparary_address_1);
                System.out.println("###" + TAG_TEMP_ADDRESS_2 + " : " + str_temparary_address_2);
                System.out.println("###" + TAG_TEMP_CITY + " : " + str_temparary_city);
                System.out.println("###" + TAG_TEMP_STATE + " : " + str_temparary_state);
                System.out.println("###" + TAG_TEMP_POSTAL + " : " + str_temparary_pin);
                System.out.println("###" + TAG_DESIGNATION + " : " + str_designation);
                System.out.println("###" + TAG_JOB_PROFILE + " : " + str_job_profile);
                System.out.println("###" + TAG_HEADER + " : " + str_header);
                System.out.println("###" + TAG_CATEGORY + " : " + str_category);
                System.out.println("###" + TAG_JOINING_DATE + " : " + str_join_date);
                System.out.println("###" + TAG_ACCOUNT_HOLDER_NAME + " : " + str_account_holder_name);
                System.out.println("###" + TAG_ACCOUNT_NUMBER + " : " + str_account_number);
                System.out.println("###" + TAG_BRANCH_NAME + " : " + str_branch_name);
                System.out.println("###" + TAG_BRANCH_ADDRESS + " : " + str_branch_address);
                System.out.println("###" + TAG_IFSC_CODE + " : " + str_ifsc_code);
                System.out.println("###" + TAG_EMERGENCY_NAME + " : " + str_emergency_name);
                System.out.println("###" + TAG_EMERGENCY_CONTACT_NO + " : " + str_emergency_contact);
                System.out.println("###" + TAG_HOME_CONTACT_NO + " : " + str_home_contact);

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

                new Utility().Function_Error_Dialog(Activity_Account_Details.this);


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

                        String str_first_name = obj_one.getString(TAG_FIRST_NAME);
                        String str_empda = obj_one.getString(TAG_EMPDA);
                        String str_last_name = obj_one.getString(TAG_LAST_NAME);
                        String str_dob = obj_one.getString(TAG_DOB);
                        String str_email = obj_one.getString(TAG_EMAIL);
                        String str_designation = obj_one.getString(TAG_DESIGNATION);
                        String str_job_profile = obj_one.getString(TAG_JOB_PROFILE);
                        String str_header = obj_one.getString(TAG_HEADER);
                        String str_category = obj_one.getString(TAG_CATEGORY);
                        String str_blood_group = obj_one.getString(TAG_BLOOD_GROUP);
                        String str_join_date = obj_one.getString(TAG_JOINING_DATE);
                        String str_personal_contact = obj_one.getString(TAG_PERSONAL_CONTACT);
                        String str_secondary_contact = obj_one.getString(TAG_SECONDARY_CONTACT);
                        String str_presonal_email = obj_one.getString(TAG_PERSONAL_EMAIL);
                        String str_address1 = obj_one.getString(TAG_ADDRESS_1);
                        String str_address2 = obj_one.getString(TAG_ADDRESS_2);
                        String str_city = obj_one.getString(TAG_CITY);
                        String str_state = obj_one.getString(TAG_STATE);
                        String str_postal = obj_one.getString(TAG_POSTAL);
                        String str_temp_address = obj_one.getString(TAG_TEMP_ADDRESS);
                        String str_temp_address_2 = obj_one.getString(TAG_TEMP_ADDRESS_2);
                        String str_temp_city = obj_one.getString(TAG_TEMP_CITY);
                        String str_temp_state = obj_one.getString(TAG_TEMP_STATE);
                        String str_temp_postal = obj_one.getString(TAG_TEMP_POSTAL);
                        String str_marriage_status = obj_one.getString(TAG_MARRITAL_STATUS);
                        String str_child = obj_one.getString(TAG_CHILD);
                        String str_account_holder_name = obj_one.getString(TAG_ACCOUNT_HOLDER_NAME);
                        String str_account_number = obj_one.getString(TAG_ACCOUNT_NUMBER);
                        String str_branch_name = obj_one.getString(TAG_BRANCH_NAME);
                        String str_branch_address = obj_one.getString(TAG_BRANCH_ADDRESS);
                        String str_ifsc_code = obj_one.getString(TAG_IFSC_CODE);
                        String str_emergency_name = obj_one.getString(TAG_EMERGENCY_NAME);
                        String str_emergency_contact_no = obj_one.getString(TAG_EMERGENCY_CONTACT_NO);
                        String str_home_contact_no = obj_one.getString(TAG_HOME_CONTACT_NO);
                        String str_aadhar_no = obj_one.getString(TAG_AADHAR_NO);
                        String str_pan_no = obj_one.getString(TAG_PAN_NO);
                        String str_gender = obj_one.getString(TAG_GENDER);
                        String str_photo = obj_one.getString(TAG_PHOTO);
                        String str_employee_level = obj_one.getString(TAG_EMPLOYEE_LEVEL);
                        String str_pf_number = obj_one.getString(TAG_PF_NUMBER);
                        String str_esi_number = obj_one.getString(TAG_ESI_NUMBER);
                        String str_insurence_no = obj_one.getString(TAG_INSURENCE_NO);

                        JSONObject json_object_certificate = obj.getJSONObject(TAG_CERTIFICATE);
                        String str_mfc = json_object_certificate.getString(TAG_CERTIFICATE_MFC);
                        String str_mfc_color = json_object_certificate.getString(TAG_CERTIFICATE_MFC_COLOR);
                        String str_fat = json_object_certificate.getString(TAG_CERTIFICATE_FAT_NO);
                        String str_fat_color = json_object_certificate.getString(TAG_CERTIFICATE_FAT_COLOR);
                        String str_farm_no = json_object_certificate.getString(TAG_CERTIFICATE_FARM_NO);
                        String str_form_color = json_object_certificate.getString(TAG_CERTIFICATE_FORM_COLOR);
                        String str_wh = json_object_certificate.getString(TAG_CERTIFICATE_WH_NO);
                        String str_wh_color = json_object_certificate.getString(TAG_CERTIFICATE_WH_COLOR);
                        String str_pti_no = json_object_certificate.getString(TAG_CERTIFICATE_PTI_NO);
                        String str_pti_color = json_object_certificate.getString(TAG_CERTIFICATE_PTI_COLOR);
                        String str_sqpid = json_object_certificate.getString(TAG_CERTIFICATE_SQPID_NO);
                        String str_sqpid_color = json_object_certificate.getString(TAG_CERTIFICATE_SQPID_COLOR);
                        String str_ttp_no = json_object_certificate.getString(TAG_CERTIFICATE_TTP_NO);
                        String str_ttp_color = json_object_certificate.getString(TAG_CERTIFICATE_TTP_COLOR);

                        JSONArray json_array_id_proofs = obj.getJSONArray(TAG_ID_PROOFS);

                        for (int count = 0; count < json_array_id_proofs.length(); count++) {
                            JSONObject item = (JSONObject) json_array_id_proofs.get(count);

                            HashMap<String, String> hash_item = new HashMap<>();
                            String str_id_name = item.getString(TAG_ID_NAME);
                            String str_id_path = item.getString(TAG_ID_PATH);
                            hash_item.put(TAG_ID_NAME, str_id_name);
                            hash_item.put(TAG_ID_PATH, str_id_path);

                            array_id_proof.add(hash_item);
                        }


                        JSONArray json_array_other_docs = obj.getJSONArray(TAG_OTHER_DOCS);

                        for (int count = 0; count < json_array_other_docs.length(); count++) {
                            JSONObject item = (JSONObject) json_array_other_docs.get(count);

                            HashMap<String, String> hash_item = new HashMap<>();
                            String str_id_name = item.getString(TAG_ID_NAME);
                            String str_id_path = item.getString(TAG_ID_PATH);
                            hash_item.put(TAG_ID_NAME, str_id_name);
                            hash_item.put(TAG_ID_PATH, str_id_path);

                            array_other_doc.add(hash_item);
                        }


                        JSONArray json_array_experience = obj.getJSONArray(TAG_EXPERIENCE);

                        for (int count = 0; count < json_array_experience.length(); count++) {
                            JSONObject item = (JSONObject) json_array_experience.get(count);

                            HashMap<String, String> hash_item = new HashMap<>();
                            String str_company_name = item.getString(TAG_COMPANY_NAME);
                            String str_desig = item.getString(TAG_DESIGNATION);
                            String str_work_from = item.getString(TAG_WORK_FROM);
                            String str_work_to = item.getString(TAG_WORK_TO);
                            String str_salary = item.getString(TAG_SALARTY);
                            String str_id_path = item.getString(TAG_ID_PATH);
                            hash_item.put(TAG_COMPANY_NAME, str_company_name);
                            hash_item.put(TAG_DESIGNATION, str_desig);
                            hash_item.put(TAG_WORK_FROM, str_work_from);
                            hash_item.put(TAG_WORK_TO, str_work_to);
                            hash_item.put(TAG_SALARTY, str_salary);
                            hash_item.put(TAG_ID_PATH, str_id_path);

                            array_experience.add(hash_item);
                        }

                        JSONArray json_array_children = obj.getJSONArray(TAG_CHILDREN);

                        for (int count = 0; count < json_array_children.length(); count++) {
                            JSONObject item = (JSONObject) json_array_children.get(count);

                            HashMap<String, String> hash_item = new HashMap<>();
                            String str_children_name = item.getString(TAG_CHILDREN_NAME);
                            String str_children_dob = item.getString(TAG_CHILDREN_DOB);
                            hash_item.put(TAG_CHILDREN_NAME, str_children_name);
                            hash_item.put(TAG_CHILDREN_DOB, str_children_dob);

                            array_children.add(hash_item);
                        }

                        if (!str_dob.isEmpty())
                            str_dob = utility.Function_Date_Formate(str_dob, "yyyy-MM-dd", "dd-MM-yyyy");

                        text_name.setText(str_first_name + " " + str_last_name);
                        text_designation.setText(str_designation + " - " + str_employee_level);

                        edit_first_name.setText(str_first_name);
                        edit_last_name.setText(str_last_name);
                        edit_date_of_birth.setText(str_dob);
                        edit_blood_group.setText(str_blood_group);
                        edit_gender.setText(str_gender);
                        edit_email.setText(str_email);
                        edit_personal_email.setText(str_presonal_email);
                        edit_personal_contact.setText(str_personal_contact);
                        edit_secondary_contact.setText(str_secondary_contact);
                        edit_aadhar_no.setText(str_aadhar_no);
                        edit_pan_no.setText(str_pan_no);
                        edit_address_1.setText(str_address1);
                        edit_address_2.setText(str_address2);
                        edit_city.setText(str_city);
                        edit_state.setText(str_state);
                        edit_pin.setText(str_postal);
                        edit_temp_address_1.setText(str_temp_address);
                        edit_temp_address_2.setText(str_temp_address_2);
                        edit_temp_city.setText(str_temp_city);
                        edit_temp_state.setText(str_temp_state);
                        edit_temp_pin.setText(str_temp_postal);
                        edit_designation.setText(str_designation);
                        edit_job_profile.setText(str_job_profile);
                        edit_header.setText(str_header);
                        edit_category.setText(str_category);
                        edit_joining_date.setText(str_join_date);
                        edit_da.setText("Rs." + str_empda);
                        edit_account_holder_name.setText(str_account_holder_name);
                        edit_account_number.setText(str_account_number);
                        edit_branch_name.setText(str_branch_name);
                        edit_branch_address.setText(str_branch_address);
                        edit_ifsc_code.setText(str_ifsc_code);
                        edit_emergency_name.setText(str_emergency_name);
                        edit_emergency_contact.setText(str_emergency_contact_no);
                        edit_home_contact.setText(str_home_contact_no);
                        edit_pf_no.setText(str_pf_number);
                        edit_esi_no.setText(str_esi_number);
                        edit_insurence_no.setText(str_insurence_no);

                        edit_mfc_no.setText(str_mfc);
                        edit_fat_no.setText(str_fat);
                        edit_farm_no.setText(str_farm_no);
                        edit_ptid_no.setText(str_pti_no);
                        edit_sqpid_no.setText(str_sqpid);
                        edit_ttp_no.setText(str_ttp_no);
                        edit_wh_no.setText(str_wh);

                        if (str_mfc.isEmpty())
                            text_layout_mfc_no_title.setVisibility(View.GONE);

                        if (str_fat.isEmpty())
                            text_layout_fat_no_title.setVisibility(View.GONE);

                        if (str_farm_no.isEmpty())
                            text_layout_farm_no_title.setVisibility(View.GONE);

                        if (str_pti_no.isEmpty())
                            text_layout_ptid_no_title.setVisibility(View.GONE);

                        if (str_sqpid.isEmpty())
                            text_layout_sqpid_no_title.setVisibility(View.GONE);

                        if (str_ttp_no.isEmpty())
                            text_layout_ttp_no_title.setVisibility(View.GONE);

                        if (str_wh.isEmpty())
                            text_layout_wh_no_title.setVisibility(View.GONE);

                        if (str_mfc_color.equals("1")) {
                            edit_mfc_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_fat_color.equals("1")) {
                            edit_fat_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_form_color.equals("1")) {
                            edit_farm_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_pti_color.equals("1")) {
                            edit_ptid_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_sqpid_color.equals("1")) {
                            edit_sqpid_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_ttp_color.equals("1")) {
                            edit_ttp_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_wh_color.equals("1")) {
                            edit_wh_no.setTextColor(getResources().getColor(R.color.colorRed));
                            int_certificate_warning_count++;
                        }

                        if (str_marriage_status.equals("Married")) {
                            seach_spinner_marriage_status.setSelection(1);

                            seach_spinner_child.setVisibility(View.VISIBLE);
                            text_view_child.setVisibility(View.VISIBLE);
                            nested_list_view_childred.setVisibility(View.VISIBLE);

                        } else {
                            seach_spinner_marriage_status.setSelection(0);

                            seach_spinner_child.setVisibility(View.GONE);
                            text_view_child.setVisibility(View.GONE);
                            nested_list_view_childred.setVisibility(View.GONE);
                        }

                        if (str_child.equals("Yes")) {

                            seach_spinner_child.setSelection(0);

                            text_view_child.setVisibility(View.VISIBLE);
                            nested_list_view_childred.setVisibility(View.VISIBLE);

                        } else {

                            seach_spinner_child.setSelection(1);
                            text_view_child.setVisibility(View.GONE);
                            nested_list_view_childred.setVisibility(View.GONE);

                        }

                        System.out.println("### str_photo " + str_photo);

                        Glide.with(Activity_Account_Details.this)
                                .load(str_photo)
                                .placeholder(R.drawable.ic_user_1)
                                .into(circle_image_profile);


                    } else if (status == 400) {

                        swipe_refresh.setRefreshing(false);

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Document_List adapter_id_proofs = new Adapter_Document_List(Activity_Account_Details.this, array_id_proof);
                    nested_list_view_id_proofs.setAdapter(adapter_id_proofs);

                    Adapter_Document_List adapter_other_document = new Adapter_Document_List(Activity_Account_Details.this, array_other_doc);
                    nested_list_view_other_document.setAdapter(adapter_other_document);

                    Adapter_Experience_List adapter_experience_list = new Adapter_Experience_List(Activity_Account_Details.this, array_experience);
                    nested_list_view_experience.setAdapter(adapter_experience_list);

                    Adapter_Children_List adapter_children_list = new Adapter_Children_List(Activity_Account_Details.this, array_children);
                    nested_list_view_childred.setAdapter(adapter_children_list);

                    swipe_refresh.setRefreshing(false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Account_Details.this);

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


    @Override
    public void onRefresh() {

        array_id_proof.clear();
        array_children.clear();
        array_experience.clear();
        array_other_doc.clear();

        swipe_refresh.setRefreshing(true);

        queue = Volley.newRequestQueue(Activity_Account_Details.this);
        Function_Get_Profile();

    }
}
