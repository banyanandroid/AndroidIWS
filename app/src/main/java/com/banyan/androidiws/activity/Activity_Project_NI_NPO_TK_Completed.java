package com.banyan.androidiws.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Product_Declartion_List;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_DB_Declaration;
import com.banyan.androidiws.database.Model_PTW_Status;
import com.banyan.androidiws.fragment.Fragment_Project_Completed_List;
import com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Config;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.NestedListview;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.bumptech.glide.Glide;
import com.sdsmdg.tastytoast.TastyToast;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;


public class Activity_Project_NI_NPO_TK_Completed extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG_DECLARATION_TYPE = "declaration_type";
    public static final String TAG_DECLARATION_O = "declaration-0";
    public static final String TAG_DECLARATION_D = "declaration-D";
    public static final String TAG_DECLARATION = "declaration";
    public static final String TAG_DECLARATION_TEXT = "text";
    public static final String TAG_DECLARATION_CHECK_BOX = "chekbox";
    public static final String TAG_DECLARATION_IMAGE = "image";
    public static final String TAG_DECLARATION_COMMENT = "comments";
    public static final String TAG_PTW_STATUS = "ptw_status";

    public static final String TAG_PTW_REQUIRED = "ptwrequired";
    public static final String TAG_PTW_NO = "ptwno";
    public static final String TAG_PTW_COPY = "ptwcopy";

    public static final String TAG_SITE_LEFT_TIME = "left_time";

    public static final String TAG_TRAVEL_COST = "travelcost";
    public static final String TAG_VEHICLE_COPY = "vechiclecost";

    public static final String TAG_DECLARATION_TYPE_D = "1";
    public static final String TAG_DECLARATION_TYPE_O = "0";

    public static final String TAG_CALLING_TYPE_REFRESH = "calling_type_refresh";
    public static final String TAG_CALLING_TYPE_WORK_SETUP = "calling_type_work_setup";
    public static final String TAG_CALLING_TYPE = "calling_type";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private AppCompatTextView text_view_circle, text_view_site_name, text_view_project, text_view_category, text_view_zone,text_view_ptw_status,  text_upload_image_message,
            text_view_message_ohs_work_photo, text_view_ptw_required, text_view_local_travel_cost, text_view_vehicle_cost;

    private AppCompatEditText edit_text_ptw_no;

    private CardView cardview_work_setup, cardview_start_work, card_view_complete_work;

    private SearchableSpinner search_spinner_work_status, search_spinner_work_at;

    private SwipeRefreshLayout swipe_refresh;

    private AppCompatButton  button_start_work, button_upload_ohs_photo,
            button_submit_ohs_work_image, button_submit_work_status,
            button_submit_left_site;

    private SpotsDialog dialog, spotDialog;

    private NestedListview nested_list_view_declaration_d;

    private ImageView image_view, image_view_ptw_copy;

    private LinearLayoutCompat linear_layout_ptw_no_copy_request;

    private RequestQueue queue;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private DatabaseHandler database_hanlde;

    private ArrayList<Model_DB_Declaration> arrayList_declaration_d, arrayList_declaration_o;

    private ArrayList<String> Arraylist_image_encode;

    private ArrayList<Image> images = new ArrayList<>();

    private Adapter_Product_Declartion_List adapter_declaration_d;

    private Timer timer_PTW_Status, timer_Photo_Upload;

    private TimerTask timerTask_PTW_Status, timerTask_Photo_Upload;

    final Handler handler_PTW_Status = new Handler();
    final Handler handler_Photo_Upload = new Handler();

    private String str_user_id, str_user_name, str_user_type, str_user_role, str_selected_d1_d10, str_selected_o1_o4,
            str_work_status = "", str_work_at = "", str_local_travel_cost = "", str_vehicle_cost = "",
            str_dpr_id,str_site_name = "",  str_project_name = "", str_zone = "", str_circle = "", str_category = "",
            image_type, listString, str_selected_image = "", str_ptw_required = "", str_ptw_no = "", str_ptw_copy = "",
            str_received_ptw_status = "", str_ohs_image = "", str_left_time = "";

    private int count;

    private boolean bol_is_add_image, bol_ptw_copy_image = false, bol_ohs_work_image = false, bol_is_online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_ni_npo_tk_completed);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();
        bol_is_online = utility.IsNetworkAvailable(Activity_Project_NI_NPO_TK_Completed.this);

        /*****************************
         *  SESSION
         *****************************/
        Function_Verify_Network_Available(this);

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
        toolbar.setTitle("NI-NPO TK Project Completed");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Project_NI_NPO_TK_Completed.this, Activity_Project_Pager.class);
                startActivity(intent);

            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        cardview_work_setup = (CardView) findViewById(R.id.cardview_work_setup);
        cardview_start_work = (CardView) findViewById(R.id.cardview_start_work);
        card_view_complete_work = (CardView) findViewById(R.id.card_view_complete_work);

        text_view_circle = (AppCompatTextView) findViewById(R.id.text_view_circle);
        text_view_site_name = (AppCompatTextView) findViewById(R.id.text_view_site_name);
        text_view_project = (AppCompatTextView) findViewById(R.id.text_view_project);
        text_view_category = (AppCompatTextView) findViewById(R.id.text_view_category);
        text_view_zone = (AppCompatTextView) findViewById(R.id.text_view_zone);
        text_view_ptw_status = (AppCompatTextView) findViewById(R.id.text_view_ptw_status);
        text_view_message_ohs_work_photo = (AppCompatTextView) findViewById(R.id.text_view_message_ohs_work_photo);
        text_view_ptw_required = (AppCompatTextView) findViewById(R.id.text_view_ptw_required);
        text_view_local_travel_cost = (AppCompatTextView) findViewById(R.id.text_view_local_cost);
        text_view_vehicle_cost = (AppCompatTextView) findViewById(R.id.text_view_vehicle_cost);

        edit_text_ptw_no = (AppCompatEditText) findViewById(R.id.edit_text_ptw_no);

        nested_list_view_declaration_d = (NestedListview) findViewById(R.id.nested_list_view_declaration_d);


        search_spinner_work_status = (SearchableSpinner) findViewById(R.id.search_spinner_work_status);
        search_spinner_work_at = (SearchableSpinner) findViewById(R.id.search_spinner_work_at);

        image_view = (ImageView) findViewById(R.id.image_view);
        image_view_ptw_copy = (ImageView) findViewById(R.id.image_view_ptw_copy);

        linear_layout_ptw_no_copy_request = (LinearLayoutCompat) findViewById(R.id.linear_layout_ptw_no_copy_request);

        button_start_work = (AppCompatButton) findViewById(R.id.button_start_work);
        button_upload_ohs_photo = (AppCompatButton) findViewById(R.id.button_upload_ohs_photo);
        button_submit_ohs_work_image = (AppCompatButton) findViewById(R.id.button_submit_ohs_work_image);
        button_submit_work_status = (AppCompatButton) findViewById(R.id.button_submit_work_status);
        button_submit_left_site = (AppCompatButton) findViewById(R.id.button_submit_left_site);

        swipe_refresh.setOnRefreshListener(this);

        button_submit_left_site.setEnabled(false);
        button_submit_left_site.setAlpha(.5f);

        edit_text_ptw_no.setEnabled(false);


        /*************************
         *  SETUP
         *************************/

        cardview_start_work.setVisibility(View.GONE);

        cardview_work_setup.setVisibility(View.VISIBLE);
        card_view_complete_work.setVisibility(View.VISIBLE);

        button_start_work.setEnabled(false);
        button_start_work.setAlpha(.5f);

        button_upload_ohs_photo.setEnabled(false);
        button_upload_ohs_photo.setAlpha(.5f);

        button_submit_ohs_work_image.setEnabled(false);
        button_submit_ohs_work_image.setAlpha(.5f);

        button_submit_work_status.setEnabled(false);
        button_submit_work_status.setAlpha(.5f);

        button_submit_left_site.setEnabled(false);
        button_submit_left_site.setAlpha(.5f);

        ArrayList<String> arrayList_ptw_required = new ArrayList<>();
        arrayList_ptw_required.add("NA");
        arrayList_ptw_required.add("Yes");
        arrayList_ptw_required.add("No");


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        database_hanlde = new DatabaseHandler(this);

        arrayList_declaration_d = new ArrayList<>();
        arrayList_declaration_o = new ArrayList<>();
        Arraylist_image_encode = new ArrayList<>();



        /*************************
         *  GET DATA
         *************************/

        str_dpr_id = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_DPR_ID, "");
        str_site_name = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_SITE_NAME, "");
        str_project_name = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_PROJECT, "");
        str_zone = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_ZONE, "");
        str_circle = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_CIRCLE, "");
        str_category = sharedPreferences.getString(Fragment_Project_Completed_List.TAG_CATEGORY, "");


        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                arrayList_declaration_d.clear();
                arrayList_declaration_o.clear();

                dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
                dialog.show();

                queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                Function_Get_Work_Setup_Details(TAG_CALLING_TYPE_WORK_SETUP);

            }
        });





        /*************************
         *  SET DATA
         *************************/
        toolbar.setTitle("Site "+str_site_name +" - Completed");
        text_view_site_name.setText(str_site_name);
        text_view_project.setText(str_project_name);
        text_view_category.setText(str_category);
        text_view_zone.setText(str_zone);
        text_view_circle.setText(str_circle);

        /*************************
         *  ACTION
         *************************/

        image_view_ptw_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString(Activity_View_Image.TAG_IMAGE_URL, str_ptw_copy);
                editor.commit();

                Intent intent = new Intent(Activity_Project_NI_NPO_TK_Completed.this, Activity_View_Image.class);
                startActivity(intent);

            }
        });

        button_start_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
                dialog.show();
                Function_Start_Work();

            }
        });

        button_upload_ohs_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selected_image = "";

                bol_ohs_work_image = true;

                Arraylist_image_encode = new ArrayList<>();
                image_type = "Location photos";
                Function_ImagePicker();

            }
        });

        button_submit_ohs_work_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_ohs_image.isEmpty()) {
                    TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Please Upload OHS Work Image", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
                    dialog.show();
                    Function_Upload_OHS_Picture();

                }

            }
        });

        search_spinner_work_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_work_status = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_spinner_work_at.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                str_work_at = "" + position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        button_submit_work_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (str_work_status.isEmpty() || str_work_status.equals("0")) {
                    TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Please Select Work Status", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_work_at.isEmpty() || str_work_at.equals("0")) {
                    TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Please Select Work At", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                    Function_Update_Work_Status();

                }

            }
        });

        button_submit_left_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float float_alpha = button_submit_work_status.getAlpha();

                if (Float.compare(float_alpha, 1f) == 0){

                    TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Please Enter Work Status First.", TastyToast.LENGTH_SHORT, TastyToast.ERROR ).show();

                }else{

                    dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                    Function_Update_Left_Site();

                }



            }
        });

    }

    public void Function_Verify_Network_Available(Context context) {

        System.out.println("#### Function_Verify_Network_Available ");
        try {

            if (!utility.IsNetworkAvailable(Activity_Project_NI_NPO_TK_Completed.this)) {
                utility.Function_Show_Not_Network_Message(Activity_Project_NI_NPO_TK_Completed.this);
            }

        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }


    /*
     */

    /********************************
     *FUNCTION LOGIN
     ********************************
     *
     * @param str_calling_type*/
    private void Function_Get_Work_Setup_Details(final String str_calling_type) {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + AppConfig.URL_PROJECT_UPDATE_REACHED_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_REACHED_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_REACHED_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        String str_reached_time = obj.getString("reached_time");

                        if (str_reached_time.equals("0000-00-00 00:00:00")) {



                        } else {

                            //set timer_PTW_Status time
                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String str_current_time = format.format(c.getTime());
                            String str_work_start_time = sharedPreferences.getString("site_reached_time_" + str_dpr_id, "0000-00-00 00:00:00");

                            System.out.println("### str_current_time " + str_current_time);
                            System.out.println("### str_work_start_time " + str_work_start_time);

                            Date date_work_start_time = null;
                            Date date_current_time = null;

                            try {

                                date_work_start_time = format.parse(str_work_start_time);
                                date_current_time = format.parse(str_current_time);

                                //in milliseconds
                                long diff = date_current_time.getTime() - date_work_start_time.getTime();

                                long diffSeconds = diff / 1000;

                                System.out.println("### diffSeconds " + diffSeconds);
                                System.out.println("### diff " + diff);

                                count = (int) diffSeconds;
                                /*card_view_timer.setVisibility(View.VISIBLE);
                                Function_Time_Counter();*/

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("### exception " + e.getLocalizedMessage());
                            }


                            String str_start_time = obj.getString("start_time");

                            if (str_start_time.equals("0000-00-00 00:00:00")) {

                                button_start_work.setEnabled(true);
                                button_start_work.setAlpha(1f);

                            } else {

                                /*cardview_work_setup.setVisibility(View.GONE);
                                cardview_start_work.setVisibility(View.VISIBLE);

                                button_start_work.setEnabled(false);
                                button_start_work.setAlpha(.5f);*/

                            }


                        }

                        JSONArray array_declaration_d = obj.getJSONArray(TAG_DECLARATION_D);

                        for (int count = 0; count < array_declaration_d.length(); count++) {

                            JSONObject obj_one = (JSONObject) array_declaration_d.get(count);

                            String str_declaration_type = obj_one.getString(TAG_DECLARATION_TYPE);
                            String str_declaration = obj_one.getString(TAG_DECLARATION);
                            String str_declaration_text = obj_one.getString(TAG_DECLARATION_TEXT);
                            String str_check_box = obj_one.getString(TAG_DECLARATION_CHECK_BOX);
                            String str_image = obj_one.getString(TAG_DECLARATION_IMAGE);
                            String str_comment = obj_one.getString(TAG_DECLARATION_COMMENT);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_DECLARATION_TYPE, str_declaration_type);
                            item.put(TAG_DECLARATION, str_declaration);
                            item.put(TAG_DECLARATION_CHECK_BOX, str_check_box);
                            item.put(TAG_DECLARATION_IMAGE, str_image);
                            item.put(TAG_DECLARATION_COMMENT, str_comment);

                            // add declaration in sqlite
                            Bitmap bitmap_galary = BitmapFactory.decodeResource(getResources(),
                                    R.drawable.ic_galary);

                            Model_DB_Declaration model_db_declaration = new Model_DB_Declaration(0, "", "", str_declaration_type,
                                    str_declaration, str_declaration_text, str_check_box, bitmap_galary, str_comment, "", str_image);


                            arrayList_declaration_d.add(model_db_declaration);

                        }


                        JSONArray array_declaration_o = obj.getJSONArray(TAG_DECLARATION_O);

                        for (int count = 0; count < array_declaration_o.length(); count++) {

                            JSONObject obj_one = (JSONObject) array_declaration_o.get(count);

                            String str_declaration_type = obj_one.getString(TAG_DECLARATION_TYPE);
                            String str_declaration = obj_one.getString(TAG_DECLARATION);
                            String str_declaration_text = obj_one.getString(TAG_DECLARATION_TEXT);
                            String str_check_box = obj_one.getString(TAG_DECLARATION_CHECK_BOX);
                            String str_image = obj_one.getString(TAG_DECLARATION_IMAGE);
                            String str_comment = obj_one.getString(TAG_DECLARATION_COMMENT);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_DECLARATION_TYPE, str_declaration_type);
                            item.put(TAG_DECLARATION, str_declaration);
                            item.put(TAG_DECLARATION_CHECK_BOX, str_check_box);
                            item.put(TAG_DECLARATION_IMAGE, str_image);
                            item.put(TAG_DECLARATION_COMMENT, str_comment);

                            // add declaration in sqlite
                            Bitmap bitmap_galary = BitmapFactory.decodeResource(getResources(),
                                    R.drawable.ic_galary);

                            Model_DB_Declaration model_db_declaration = new Model_DB_Declaration(0, "", "", str_declaration_type,
                                    str_declaration, str_declaration_text, str_check_box, bitmap_galary, str_comment, "", str_image);

                            arrayList_declaration_o.add(model_db_declaration);

                        }

                        str_received_ptw_status = obj.getString(TAG_PTW_STATUS);
                        text_view_ptw_status.setText(str_received_ptw_status);

                        if (str_received_ptw_status.equals("Accepted")) {

                            // delete alarm data in sqlite
                            Model_PTW_Status model_ptw_status = new Model_PTW_Status(str_ptw_no);
                            database_hanlde.deletePTW(model_ptw_status);

                            /*cardview_work_setup.setVisibility(View.GONE);
                            cardview_start_work.setVisibility(View.VISIBLE);*/

                        } else {
                            /*cardview_work_setup.setVisibility(View.VISIBLE);
                            cardview_start_work.setVisibility(View.GONE);*/
                        }

                        if (str_calling_type.equals(TAG_CALLING_TYPE_REFRESH))
                            dialog.dismiss();

                    } else if (status == 400) {

                        if (str_calling_type.equals(TAG_CALLING_TYPE_REFRESH))
                            dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        if (str_calling_type.equals(TAG_CALLING_TYPE_REFRESH))
                            dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    System.out.println("### " + str_calling_type.equals(TAG_CALLING_TYPE_WORK_SETUP));

                    adapter_declaration_d = new Adapter_Product_Declartion_List(Activity_Project_NI_NPO_TK_Completed.this, arrayList_declaration_d, Activity_Project_NI_NPO_TK_Completed.this, bol_is_online);
                    nested_list_view_declaration_d.setAdapter(adapter_declaration_d);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

                if (str_calling_type.equals(TAG_CALLING_TYPE_WORK_SETUP)) {
                    //dialog.dismiss();
                    queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                    Function_Get_PTW(str_received_ptw_status);

                } else if (str_calling_type.equals(TAG_CALLING_TYPE_REFRESH)) {
                    dialog.dismiss();
                }

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                if (str_calling_type.equals(TAG_CALLING_TYPE_WORK_SETUP))
                    dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + "dpr_id" + " : " + str_dpr_id);

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


    /*******************************
     *  PIC UPLOADER
     * ***************************/

// Recomended builder
    public void Function_ImagePicker() {
        if (bol_ptw_copy_image) {

           /* ImagePicker.with(this)
                    .setFolderMode(true) // set folder mode (false by default)
                    .setFolderTitle("Folder") // folder selection title
                    .setImageTitle("Tap To Select") // image selection title
                    .setMultipleMode(true) // multi mode (default mode)
                    .setMaxSize(1)// max images can be selected (999 by default)
                    .setKeepScreenOn(true)
                    .start();

            com.nguyenhoanglam.imagepicker.model.Config config = new com.nguyenhoanglam.imagepicker.model.Config();
*/

        } else {

         /*   ImagePicker.with(this)
                    .setCameraOnly(true)
                    .setMaxSize(1)// max images can be selected (999 by default)
                    .setKeepScreenOn(true)
                    .start();

            com.nguyenhoanglam.imagepicker.model.Config config = new com.nguyenhoanglam.imagepicker.model.Config();
*/
        }


    }



    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Start_Work() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME " + AppConfig.URL_PROJECT_UPDATE_START_TIME);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_START_TIME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_START_TIME : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Work Started Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        button_start_work.setEnabled(false);
                        button_start_work.setAlpha(.5f);


                       /* //set alarm 15 minutes notification

                        Integer int_dpr_id = Integer.parseInt(str_dpr_id);
                        int_dpr_id = (int_dpr_id + 1000); // dpr+100 fro 15 minuter notification

                        Intent alarmIntent = new Intent(Activity_Project_NI_NPO_TK_In_Progress.this, BroadcastReceiver_Alarm_Receiver_PTW_Status.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(Activity_Project_NI_NPO_TK_In_Progress.this, int_dpr_id, alarmIntent, 0);
                        Integer int_intevel = 60000;
                        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() + int_intevel,
                                int_intevel, pendingIntent);

                        // store alarm details in sqlite for set alarm on device reboot
                        Model_15_Minutes_Photo_Upload model_15_minutes_photo_upload = new Model_15_Minutes_Photo_Upload(""+int_dpr_id);

                        database_hanlde.addPhotoUpload(model_15_minutes_photo_upload);
*/
                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME " + "dpr_id" + " : " + str_dpr_id);

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
     *FUNCTION GET PTW
     ********************************
     * @param str_received_ptw_status*/
    private void Function_Get_PTW(final String str_received_ptw_status) {

        try{

            System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_PTW " + AppConfig.URL_PROJECT_UPDATE_GET_PTW);
            StringRequest request = new StringRequest(Request.Method.POST,
                    AppConfig.URL_PROJECT_UPDATE_GET_PTW, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    System.out.println("###  AppConfig.URL_PROJECT_UPDATE_GET_PTW : onResponse " + response);
                    Log.d("TAG", "### " + response.toString());
                    try {
                        JSONObject obj = new JSONObject(response);
                        int status = obj.getInt("status");
                        String msg = obj.getString("msg");
                        if (status == 200) {

                            JSONObject obj_records = obj.getJSONObject("records");
                            String str_ptw_required = obj_records.getString(TAG_PTW_REQUIRED);
                            String str_ptw_no = obj_records.getString(TAG_PTW_NO);
                            str_ptw_copy = obj_records.getString(TAG_PTW_COPY);
                            Integer int_ptw_required = Integer.parseInt(str_ptw_required);

                            if (str_ptw_no.isEmpty() || str_ptw_no == null)
                                str_ptw_no = "-";

                            if (str_ptw_copy.isEmpty() || str_ptw_copy == null)
                                str_ptw_copy = "-";



                            if (str_ptw_required.equals("1")){

                                text_view_ptw_required.setText("Yes");
                                edit_text_ptw_no.setText(str_ptw_no);
                                Glide.with(Activity_Project_NI_NPO_TK_Completed.this)
                                        .load(str_ptw_copy)
                                        .placeholder(R.drawable.ic_galary)
                                        .into(image_view_ptw_copy);

                            }else if (str_ptw_required.equals("2")){

                                text_view_ptw_required.setText("No");
                                linear_layout_ptw_no_copy_request.setVisibility(View.GONE);
                            }

                            // show alert  dialog for ptw status
                            if (str_ptw_required.equals("1") || str_ptw_required.equals("2")) {

                                if (str_received_ptw_status.equals("Pending")){


                                }else if (str_received_ptw_status.equals("Accepted")){


/*
                                    cardview_work_setup.setVisibility(View.GONE);
                                    cardview_start_work.setVisibility(View.VISIBLE);
*/



                                } else if (str_received_ptw_status.equals("Rejected")){


                                    AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Project_NI_NPO_TK_Completed.this);
                                    alertdialog_builder.setCancelable(false);
                                    alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                            Intent intent = new Intent(Activity_Project_NI_NPO_TK_Completed.this, Activity_Project_Pager.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    AlertDialog alertDialog = alertdialog_builder.create();
                                    alertDialog.setTitle("PTW Request");
                                    alertDialog.setMessage("PTW Requested Rejected.");
                                    alertDialog.show();

                                }

                            }

                        } else if (status == 400) {

                            TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        } else if (status == 404) {

                            TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        System.out.println("### exception " + e.getLocalizedMessage());

                    }

                    //dialog.dismiss();

                    System.out.println("### dialog "+dialog);
                queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                Function_Get_Left_Site_Time();


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    dialog.dismiss();
                    System.out.println("### dialog "+dialog);
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_PTW onErrorResponse");
                    if (error != null)
                        System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_PTW onErrorResponse " + error.getLocalizedMessage());

                    new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("dpr_id", str_dpr_id);

                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_PTW " + "dpr_id" + " : " + str_dpr_id);

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

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
            System.out.println("### Exception e "+e.getLocalizedMessage());

        }


    }


    @Override
    protected void onResume() {
        super.onResume();

    }







    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Upload_OHS_Picture() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE " + AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "OHS Work Image Upload Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    text_view_message_ohs_work_photo.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                    text_view_message_ohs_work_photo.setText("Please Upload Image");

                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("userid", str_user_id);
                params.put("image", str_ohs_image);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "userid" + " : " + str_user_id);
                System.out.println("###  " + "image" + " : " + str_ohs_image);

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
    private void Function_Update_Work_Status() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS " + AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Work Status Updated Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        button_submit_work_status.setEnabled(false);
                        button_submit_work_status.setAlpha(.5f);

                        button_submit_left_site.setEnabled(true);
                        button_submit_left_site.setAlpha(1f);


                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("final_at", str_work_at);
                params.put("field_work", str_work_status);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_WORK_STATUS " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "final_at" + " : " + str_work_at);
                System.out.println("###  " + "field_work" + " : " + str_work_status);

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
    private void Function_Get_Left_Site_Time() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE " + AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        str_left_time = obj.getString(TAG_SITE_LEFT_TIME);

                        System.out.println("### str_left_time " + str_left_time);

                        if (str_left_time.isEmpty() || str_left_time.equals("0000-00-00 00:00:00")) {

                        } else {

                            /*cardview_work_setup.setVisibility(View.GONE);
                            cardview_start_work.setVisibility(View.GONE);
                            card_view_complete_work.setVisibility(View.VISIBLE);*/

                        }


                    } else if (status == 400) {

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

                //dialog.dismiss();
                queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
                Function_Get_Expense(str_left_time);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE " + "dpr_id" + " : " + str_dpr_id);

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
    private void Function_Update_Left_Site() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE " + AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_NI_NPO_TK_Completed.this, "Left Site Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        /*cardview_start_work.setVisibility(View.GONE);
                        card_view_complete_work.setVisibility(View.VISIBLE);*/

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_LEFT_SITE " + "dpr_id" + " : " + str_dpr_id);

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
     ********************************
     * @param str_left_time*/
    private void Function_Get_Expense(final String str_left_time) {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE " + AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONObject obj_record = obj.getJSONObject("records");

                        String str_travel_cost = obj_record.getString(TAG_TRAVEL_COST);
                        String str_vehicle_cost = obj_record.getString(TAG_VEHICLE_COPY);

                        if (str_travel_cost.isEmpty() && str_vehicle_cost.isEmpty()) {


                        } else {


                            if (!str_left_time.equals("0000-00-00 00:00:00")){ // left site completed

                                text_view_local_travel_cost.setText("Rs. "+str_travel_cost);
                                text_view_vehicle_cost.setText("Rs. "+str_vehicle_cost);

/*                                cardview_start_work.setVisibility(View.GONE);
                                card_view_complete_work.setVisibility(View.VISIBLE);

                                button_complete_work.setEnabled(true);
                                button_complete_work.setAlpha(1f);

                                button_submit_expense.setEnabled(false);
                                button_submit_expense.setAlpha(.5f);*/

                            }

                        }


                    } else if (status == 400) {

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_NI_NPO_TK_Completed.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("travelcost", str_local_travel_cost);
                params.put("vechiclecost", str_vehicle_cost);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_EXPENSE " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "travelcost" + " : " + str_local_travel_cost);
                System.out.println("###  " + "vechiclecost" + " : " + str_vehicle_cost);

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

        swipe_refresh.setRefreshing(true);

        arrayList_declaration_d.clear();
        arrayList_declaration_o.clear();

        dialog = new SpotsDialog(Activity_Project_NI_NPO_TK_Completed.this);
        dialog.show();

        queue = Volley.newRequestQueue(Activity_Project_NI_NPO_TK_Completed.this);
        Function_Get_Work_Setup_Details(TAG_CALLING_TYPE_WORK_SETUP);

    }
}
