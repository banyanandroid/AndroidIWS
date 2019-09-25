package com.banyan.androidiws.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
import com.banyan.androidiws.database.Model_PTW_Status;
import com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Config;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.NestedListview;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
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
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List.TAG_DPR_ID;
import static com.banyan.androidiws.global.AppConfig.LONG_IMAGE_UPLOAD_DELAY_TIME;

public class Activity_Project_MM_In_Progress extends AppCompatActivity {

    public static final String TAG_DECLARATION_TYPE = "declaration_type";
    public static final String TAG_DECLARATION_O = "declaration-0";
    public static final String TAG_DECLARATION_D = "declaration-D";
    public static final String TAG_DECLARATION = "declaration";
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

    private AppCompatTextView text_view_ptw_status, text_view_timer, text_upload_image_message,
            text_view_ptw_copy, text_view_message_ptw_copy, text_view_message_ohs_work_photo;

    private AppCompatEditText edit_text_site_details, edit_text_ptw_no;

    private CardView cardview_work_setup, cardview_start_work, card_view_complete_work, card_view_timer;

    private SearchableSpinner search_spinner_work_status, spinner_ptw_required, search_spinner_work_at;

    private AppCompatButton button_site_reached, button_ptw_copy_upload_file, button_start_work, button_submit_ptw_request, button_upload_ohs_photo,
            button_submit_ohs_work_image, button_submit_work_status,
            button_complete_work, button_submit_left_site;

    private SpotsDialog dialog, spotDialog;

    private NestedListview nested_list_view_declaration_d, nested_list_view_declaration_o;

    private ImageView image_view;

    private LinearLayoutCompat linear_layout_ptw_no_copy_request;

    private RequestQueue queue;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private DatabaseHandler database_hanlde;

    private ArrayList<HashMap<String, String>> arrayList_declaration_d, arrayList_declaration_o;

    private ArrayList<String> Arraylist_image_encode;

    private ArrayList<Image> images = new ArrayList<>();

    private Adapter_Product_Declartion_List adapter_declaration_d, adapter_declaration_o;

    private Timer timer_PTW_Status, timer_Photo_Upload;

    private TimerTask timerTask_PTW_Status, timerTask_Photo_Upload;

    final Handler handler_PTW_Status = new Handler();
    final Handler handler_Photo_Upload = new Handler();

    private String str_user_id, str_user_name, str_user_type, str_user_role, str_site_details, str_selected_d1_d10, str_selected_o1_o4,
            str_work_status = "", str_work_at = "", str_local_travel_cost = "", str_vehicle_cost = "",
            str_dpr_id, image_type, listString, str_selected_image = "", str_ptw_required = "", str_ptw_no = "", str_ptw_copy = "",
            str_received_ptw_status = "", str_ohs_image = "", str_left_time = "";

    private int count;

    private boolean bol_is_add_image, bol_ptw_copy_image = false, bol_ohs_work_image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_mm_in_progress);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();

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
        toolbar.setTitle("MM Project Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_Pager.class);
                startActivity(intent);

            }
        });


        cardview_work_setup = (CardView) findViewById(R.id.cardview_work_setup);
        cardview_start_work = (CardView) findViewById(R.id.cardview_start_work);
        card_view_complete_work = (CardView) findViewById(R.id.card_view_complete_work);
        card_view_timer = (CardView) findViewById(R.id.card_view_timer);

        text_view_ptw_status = (AppCompatTextView) findViewById(R.id.text_view_ptw_status);
        text_view_timer = (AppCompatTextView) findViewById(R.id.text_view_timer);
        text_view_ptw_copy = (AppCompatTextView) findViewById(R.id.text_view_ptw_copy);
        text_view_message_ptw_copy = (AppCompatTextView) findViewById(R.id.text_view_message_ptw_copy);
        text_view_message_ohs_work_photo = (AppCompatTextView) findViewById(R.id.text_view_message_ohs_work_photo);

        edit_text_site_details = (AppCompatEditText) findViewById(R.id.edit_text_site_details);
        edit_text_ptw_no = (AppCompatEditText) findViewById(R.id.edit_text_ptw_no);





        nested_list_view_declaration_d = (NestedListview) findViewById(R.id.nested_list_view_declaration_d);
        nested_list_view_declaration_o = (NestedListview) findViewById(R.id.nested_list_view_declaration_o);

        search_spinner_work_status = (SearchableSpinner) findViewById(R.id.search_spinner_work_status);
        search_spinner_work_at = (SearchableSpinner) findViewById(R.id.search_spinner_work_at);
        spinner_ptw_required = (SearchableSpinner) findViewById(R.id.spinner_ptw_required);

        image_view = (ImageView) findViewById(R.id.image_view);

        linear_layout_ptw_no_copy_request = (LinearLayoutCompat) findViewById(R.id.linear_layout_ptw_no_copy_request);

        button_site_reached = (AppCompatButton) findViewById(R.id.button_site_reached);
        button_ptw_copy_upload_file = (AppCompatButton) findViewById(R.id.button_ptw_copy_upload_file);
        button_start_work = (AppCompatButton) findViewById(R.id.button_start_work);
        button_upload_ohs_photo = (AppCompatButton) findViewById(R.id.button_upload_ohs_photo);
        button_submit_ohs_work_image = (AppCompatButton) findViewById(R.id.button_submit_ohs_work_image);
        button_submit_ptw_request = (AppCompatButton) findViewById(R.id.button_submit_ptw_request);
        button_submit_work_status = (AppCompatButton) findViewById(R.id.button_submit_work_status);
        button_complete_work = (AppCompatButton) findViewById(R.id.button_complete_work);
        button_submit_left_site = (AppCompatButton) findViewById(R.id.button_submit_left_site);

        linear_layout_ptw_no_copy_request.setVisibility(View.GONE);

        button_submit_left_site.setEnabled(false);
        button_submit_left_site.setAlpha(.5f);

        /*************************
         *  SETUP
         *************************/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        database_hanlde = new DatabaseHandler(this);

        arrayList_declaration_d = new ArrayList<>();
        arrayList_declaration_o = new ArrayList<>();
        Arraylist_image_encode = new ArrayList<>();


        /*************************
         *  GET DATA
         *************************/

        str_dpr_id = sharedPreferences.getString(Fragment_Project_In_Progress_List.TAG_DPR_ID, "");

        arrayList_declaration_d.clear();
        arrayList_declaration_o.clear();

        dialog = new SpotsDialog(this);
        dialog.show();
        queue = Volley.newRequestQueue(this);
        Function_Get_Work_Setup_Details(TAG_CALLING_TYPE_WORK_SETUP);


        /*************************
         *  SET DATA
         *************************/

        toolbar.setTitle("Site "+str_dpr_id +" - In Progress");

        cardview_start_work.setVisibility(View.GONE);
        card_view_complete_work.setVisibility(View.GONE);
        card_view_timer.setVisibility(View.GONE);


        /*************************
         *  ACTION
         *************************/
        button_site_reached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_site_details = edit_text_site_details.getText().toString();

                if (str_site_details.isEmpty()){
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Enter Site Details", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                }else{

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    dialog.show();
                    Function_Update_Site_Reached();
                }

            }
        });

        button_ptw_copy_upload_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_ptw_copy = "";

                bol_ptw_copy_image = true;

                bol_is_add_image = true;
                Arraylist_image_encode = new ArrayList<>();
                image_type = "Location photos";
                Function_ImagePicker();

            }
        });

        spinner_ptw_required.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("### spinner_ptw_required setOnItemSelectedListener");
                System.out.println("### position "+position);
                str_ptw_required = "" + position;

                if (str_ptw_required.equals("0")) { // NA
                    linear_layout_ptw_no_copy_request.setVisibility(View.GONE);
                } else if (str_ptw_required.equals("1")) { // YES
                    linear_layout_ptw_no_copy_request.setVisibility(View.VISIBLE);
                } else if (str_ptw_required.equals("2")) { // NO
                    linear_layout_ptw_no_copy_request.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button_submit_ptw_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean is_declaration_d_requested = adapter_declaration_d.Function_Is_Declaration_Request_Sent();
                boolean is_declaration_o_requested = adapter_declaration_o.Function_Is_Declaration_Request_Sent();


                str_ptw_no = edit_text_ptw_no.getText().toString();

                if (!is_declaration_d_requested) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Do Declartion D Request", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (!is_declaration_o_requested) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Do Declartion O Request", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_ptw_required.isEmpty()) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Select PTW Required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_ptw_required.equals("0")) {

                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Select PTW Required", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();

                } else if (str_ptw_required.equals("1")) {

                    if (str_ptw_no.isEmpty()) {
                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Enter PTW No", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    } else if (str_ptw_copy.isEmpty()) {
                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Upload PTW Copy File", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                    } else {

                        dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                        dialog.show();
                        Function_Update_PTW();
                    }

                } else if (str_ptw_required.equals("2")) {

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    dialog.show();
                    Function_Update_PTW();
                }

            }
        });



        button_start_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
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
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Upload OHS Work Image", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
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

                if (str_work_status.isEmpty()) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Select Work Status", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else if (str_work_at.isEmpty()) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Select Work At", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                    Function_Update_Work_Status();

                }

            }
        });

        button_submit_left_site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float float_alpha = button_submit_work_status.getAlpha();

                if (Float.compare(float_alpha, 1f) == 0){

                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Enter Work Status First.", TastyToast.LENGTH_SHORT, TastyToast.ERROR ).show();

                }else{

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                    Function_Update_Left_Site();

                }



            }
        });

        button_complete_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                dialog.show();
                queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                Function_Completed_Work();

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();

        if ((dialog != null) && dialog.isShowing())
            dialog.dismiss();
        dialog = null;
    }



    public void Function_Verify_Network_Available(Context context) {

        System.out.println("#### Function_Verify_Network_Available ");
        try {

            if (!utility.IsNetworkAvailable(Activity_Project_MM_In_Progress.this)) {
                utility.Function_Show_Not_Network_Message(Activity_Project_MM_In_Progress.this);
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

                            button_site_reached.setEnabled(true);
                            button_site_reached.setAlpha(1f);

                            button_ptw_copy_upload_file.setEnabled(false);
                            button_ptw_copy_upload_file.setAlpha(.5f);

                            button_submit_ptw_request.setEnabled(false);
                            button_submit_ptw_request.setAlpha(.5f);

                        } else {

                            button_site_reached.setEnabled(false);
                            button_site_reached.setAlpha(.5f);

                            button_ptw_copy_upload_file.setEnabled(true);
                            button_ptw_copy_upload_file.setAlpha(1f);

                            button_submit_ptw_request.setEnabled(true);
                            button_submit_ptw_request.setAlpha(1f);

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
                                card_view_timer.setVisibility(View.VISIBLE);
                                Function_Time_Counter();

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("### exception " + e.getLocalizedMessage());
                            }


                            String str_start_time = obj.getString("start_time");

                            if (str_start_time.equals("0000-00-00 00:00:00")) {

                                button_start_work.setEnabled(true);
                                button_start_work.setAlpha(1f);

                            } else {

                                cardview_work_setup.setVisibility(View.GONE);
                                cardview_start_work.setVisibility(View.VISIBLE);

                                button_start_work.setEnabled(false);
                                button_start_work.setAlpha(.5f);

                            }


                        }

                        JSONArray array_declaration_d = obj.getJSONArray(TAG_DECLARATION_D);

                        for (int count = 0; count < array_declaration_d.length(); count++) {

                            JSONObject obj_one = (JSONObject) array_declaration_d.get(count);

                            String str_declaration_type = obj_one.getString(TAG_DECLARATION_TYPE);
                            String str_declaration = obj_one.getString(TAG_DECLARATION);
                            String str_check_box = obj_one.getString(TAG_DECLARATION_CHECK_BOX);
                            String str_image = obj_one.getString(TAG_DECLARATION_IMAGE);
                            String str_comment = obj_one.getString(TAG_DECLARATION_COMMENT);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_DECLARATION_TYPE, str_declaration_type);
                            item.put(TAG_DECLARATION, str_declaration);
                            item.put(TAG_DECLARATION_CHECK_BOX, str_check_box);
                            item.put(TAG_DECLARATION_IMAGE, str_image);
                            item.put(TAG_DECLARATION_COMMENT, str_comment);

                            arrayList_declaration_d.add(item);

                        }


                        JSONArray array_declaration_o = obj.getJSONArray(TAG_DECLARATION_O);

                        for (int count = 0; count < array_declaration_o.length(); count++) {

                            JSONObject obj_one = (JSONObject) array_declaration_o.get(count);

                            String str_declaration_type = obj_one.getString(TAG_DECLARATION_TYPE);
                            String str_declaration = obj_one.getString(TAG_DECLARATION);
                            String str_check_box = obj_one.getString(TAG_DECLARATION_CHECK_BOX);
                            String str_image = obj_one.getString(TAG_DECLARATION_IMAGE);
                            String str_comment = obj_one.getString(TAG_DECLARATION_COMMENT);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_DECLARATION_TYPE, str_declaration_type);
                            item.put(TAG_DECLARATION, str_declaration);
                            item.put(TAG_DECLARATION_CHECK_BOX, str_check_box);
                            item.put(TAG_DECLARATION_IMAGE, str_image);
                            item.put(TAG_DECLARATION_COMMENT, str_comment);

                            arrayList_declaration_o.add(item);

                        }

                        str_received_ptw_status = obj.getString(TAG_PTW_STATUS);
                        text_view_ptw_status.setText(str_received_ptw_status);

                        if (str_received_ptw_status.equals("Accepted")) {

                            // delete alarm data in sqlite
                            Model_PTW_Status model_ptw_status = new Model_PTW_Status(str_ptw_no);
                            database_hanlde.deletePTW(model_ptw_status);

                            cardview_work_setup.setVisibility(View.GONE);
                            cardview_start_work.setVisibility(View.VISIBLE);

                        } else {
                            cardview_work_setup.setVisibility(View.VISIBLE);
                            cardview_start_work.setVisibility(View.GONE);
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

                    adapter_declaration_d = new Adapter_Product_Declartion_List(Activity_Project_MM_In_Progress.this, arrayList_declaration_d, Activity_Project_MM_In_Progress.this);
                    nested_list_view_declaration_d.setAdapter(adapter_declaration_d);

                    adapter_declaration_o = new Adapter_Product_Declartion_List(Activity_Project_MM_In_Progress.this, arrayList_declaration_o, Activity_Project_MM_In_Progress.this);
                    nested_list_view_declaration_o.setAdapter(adapter_declaration_o);


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

                if (str_calling_type.equals(TAG_CALLING_TYPE_WORK_SETUP)) {
                    //dialog.dismiss();
                    queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                    Function_Get_PTW(str_received_ptw_status);
                } else if (str_calling_type.equals(TAG_CALLING_TYPE_REFRESH)) {
                    dialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (str_calling_type.equals(TAG_CALLING_TYPE_WORK_SETUP))
                    dialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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

    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Update_Site_Reached() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE " + AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Site Reached Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        editor.putString("site_reached_time_" + str_dpr_id, formattedDate);
                        editor.commit();

                        card_view_timer.setVisibility(View.VISIBLE);
                        Function_Time_Counter();

                        button_site_reached.setEnabled(false);
                        button_site_reached.setAlpha(.5f);

                        button_ptw_copy_upload_file.setEnabled(true);
                        button_ptw_copy_upload_file.setAlpha(1f);

                        button_submit_ptw_request.setEnabled(true);
                        button_submit_ptw_request.setAlpha(1f);

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

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                //params.put("site_details", str_site_details);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE " + "dpr_id" + " : " + str_dpr_id);
                //System.out.println("###  " + "site_details" + " : " + str_site_details);

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

    public void Alert_PTW_Request(final String str_declaration_type, final String str_declaration, final String str_checkbox, final String str_image, final String str_comment) {

        LayoutInflater layoutInflater = LayoutInflater.from(Activity_Project_MM_In_Progress.this);
        View view_alert = layoutInflater.inflate(R.layout.alert_ptw_request, null);

        TextView text_view_declaration = (TextView) view_alert.findViewById(R.id.text_view_declaration);
        final EditText edit_text_comment = (EditText) view_alert.findViewById(R.id.edit_text_comment);
        text_upload_image_message = (AppCompatTextView) view_alert.findViewById(R.id.text_upload_image_message);
        final AppCompatButton button_uplod_image = (AppCompatButton) view_alert.findViewById(R.id.button_uplod_image);

        text_view_declaration.setText(str_declaration);
        edit_text_comment.setText(str_comment);

        button_uplod_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_selected_image = "";

                bol_is_add_image = true;
                Arraylist_image_encode = new ArrayList<>();
                image_type = "Location photos";
                Function_ImagePicker();


            }
        });

        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Project_MM_In_Progress.this);
        alertdialog_builder.setCancelable(false);
        alertdialog_builder.setView(view_alert);
        alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String str_checkbox = "1";

                String str_comment = edit_text_comment.getText().toString();

                System.out.println("###  str_selected_image " + str_selected_image);
                if (str_selected_image.isEmpty()) {
                    TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Please Upload Image", TastyToast.LENGTH_SHORT, TastyToast.ERROR).show();
                } else {

                    spotDialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    spotDialog.show();

                    queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                    Function_Update_PTW_Declaration_Request(str_declaration_type, str_declaration, str_checkbox, str_selected_image, str_comment);

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
        alertDialog.setTitle("PTW Request");
        alertDialog.show();

    }


    /*******************************
     *  PIC UPLOADER
     * ***************************/

// Recomended builder
    public void Function_ImagePicker() {
        if (bol_ptw_copy_image) {

            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, false);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
            startActivityForResult(intent, 1213);


        } else {

            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, ImageSelectActivity.class);
            intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, false);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
            intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);//default is true
            startActivityForResult(intent, 1213);

        }


    }

    // GET IMAGE FROM IMAGE PICKER
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1213 && resultCode == RESULT_OK && data != null) {


            String str_img_path =  data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);

            System.out.println("### str_img_path "+str_img_path);

            Bitmap bmBitmap = decodeSampledBitmapFromResource(str_img_path, 400, 400);

            //  image_view.setImageBitmap(bmBitmap);

            //Bitmap bmBitmap = BitmapFactory.decodeFile(str_img_path);
            System.out.println("### bmBitmap.getByteCount() "+bmBitmap.getByteCount());

            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            bmBitmap.compress(Bitmap.CompressFormat.JPEG, 25, bao);
            byte[] ba = bao.toByteArray();
            String encodedstring = Base64.encodeToString(ba, 0);
            System.out.println("### encodedstring " + encodedstring);
            Log.e("base64", "-----" + encodedstring);
            Log.e("### encodedstring", "-----" + encodedstring);

            str_selected_image = encodedstring;

            if (bol_ptw_copy_image) {

                str_ptw_copy = encodedstring;

                text_view_message_ptw_copy.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_view_message_ptw_copy.setText("Image Uploaded Successfully.");

                bol_ptw_copy_image = false;

                System.out.print("### str_ptw_copy " + str_ptw_copy);

            }else if (bol_ohs_work_image){

                str_ohs_image = encodedstring;

                text_view_message_ohs_work_photo.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_view_message_ohs_work_photo.setText("Image Uploaded Successfully.");

                bol_ohs_work_image = false;

            }else{

                System.out.print("### str_selected_image " + str_selected_image);

                Arraylist_image_encode.add(encodedstring);

                //hide to avoid out of memory issue
            /*    byte[] decodedString = Base64.decode(encodedstring, Base64.DEFAULT);
                Bitmap img_uploaded_image = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

               */

                text_upload_image_message.setTextColor(getResources().getColor(R.color.colorPrimary));
                text_upload_image_message.setText("Image Uploaded Successfully.");


            }



        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(String file,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, options);
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

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Work Started Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        button_start_work.setEnabled(false);
                        button_start_work.setAlpha(.5f);

                        //show notification 15 minutes once for photo upload
                        startTimer_Photo_Upload();

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

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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
     *FUNCTION LOGIN
     ********************************
     * @param */
    private void Function_Update_PTW_Declaration_Request(final String str_declaration_type, final String str_declaration, final String str_check_box,
                                                         final String str_image, final String str_comment) {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST " + AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200 || status == 404) {

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "PTW Request Updated Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        spotDialog.dismiss();

                    } else if (status == 400) {

                        spotDialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    /* else if (status == 404) {

                        spotDialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }*/

                    arrayList_declaration_d.clear();
                    arrayList_declaration_o.clear();

                    dialog = new SpotsDialog(Activity_Project_MM_In_Progress.this);
                    dialog.show();
                    queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                    Function_Get_Work_Setup_Details(TAG_CALLING_TYPE_REFRESH);


                    spotDialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
                spotDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                spotDialog.dismiss();

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("userid", str_user_id);
                params.put("declaration_type", str_declaration_type);
                params.put("declaration", str_declaration);
                params.put("chekbox", str_check_box);
                params.put("image", str_image);
                params.put("comments", str_comment);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "userid" + " : " + str_user_id);
                System.out.println("###  " + "declaration_type" + " : " + str_declaration_type);
                System.out.println("###  " + "declaration" + " : " + str_declaration);
                System.out.println("###  " + "chekbox" + " : " + str_check_box);
                System.out.println("###  " + "image " + " : " + str_image);
                System.out.println("###  " + "comments" + " : " + str_comment);

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
                            String str_ptw_copy = obj_records.getString(TAG_PTW_COPY);
                            Integer int_ptw_required = Integer.parseInt(str_ptw_required);

                            if (str_ptw_copy.isEmpty() || str_ptw_copy == null)
                                str_ptw_copy = "-";

                            edit_text_ptw_no.setText(str_ptw_no);


                            if (str_ptw_required.equals("1") || str_ptw_required.equals("2")) {
                                linear_layout_ptw_no_copy_request.setVisibility(View.GONE);
                            }

                            // show alert  dialog for ptw status
                            if (str_ptw_required.equals("1") || str_ptw_required.equals("2")) {

                                if (str_received_ptw_status.equals("Pending")){

                                    AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Project_MM_In_Progress.this);
                                    alertdialog_builder.setCancelable(false);
                                    alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_Pager.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                                    AlertDialog alertDialog = alertdialog_builder.create();
                                    alertDialog.setTitle("PTW Request");
                                    alertDialog.setMessage("PTW Requested Successfully, Please Wait for PTW Status.");
                                    alertDialog.show();



                                }else if (str_received_ptw_status.equals("Accepted")){

                                    stoptimer_PTW_Status();

                                    cardview_work_setup.setVisibility(View.GONE);
                                    cardview_start_work.setVisibility(View.VISIBLE);



                                } else if (str_received_ptw_status.equals("Rejected")){

                                    stoptimer_PTW_Status();

                                    AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Project_MM_In_Progress.this);
                                    alertdialog_builder.setCancelable(false);
                                    alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.dismiss();

                                            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_Pager.class);
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
                queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
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

                    new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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


    /********************************
     *FUNCTION GET PTW
     *********************************/
    private void Function_Update_PTW() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW " + AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "PTW Requested Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        button_ptw_copy_upload_file.setEnabled(false);
                        button_ptw_copy_upload_file.setAlpha(.5f);

                        button_submit_ptw_request.setEnabled(false);
                        button_submit_ptw_request.setAlpha(.5f);

                        // check for ptw status every minutes once
                        startTimer_PTW_Status();

                        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(Activity_Project_MM_In_Progress.this);
                        alertdialog_builder.setCancelable(false);
                        alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                                Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_Pager.class);
                                startActivity(intent);
                                finish();

                            }
                        });

                        AlertDialog alertDialog = alertdialog_builder.create();
                        alertDialog.setTitle("PTW Request");
                        alertDialog.setMessage("PTW Requested Successfully. Please Wait For PTW Request To Accept.");
                        alertDialog.show();


                     /*   //set alarm
                        Integer int_dpr_id = Integer.parseInt(str_dpr_id);

                        Intent alarmIntent = new Intent(Activity_Project_NI_NPO_TK_In_Progress.this, BroadcastReceiver_Alarm_Receiver_PTW_Status.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(Activity_Project_NI_NPO_TK_In_Progress.this, int_dpr_id, alarmIntent, 0);
                        Integer int_intevel = 60000;
                        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                                SystemClock.elapsedRealtime() + int_intevel,
                                int_intevel, pendingIntent);

                        // store alarm details in sqlite for set alarm on device reboot
                        Model_PTW_Status model_ptw_status = new Model_PTW_Status(""+int_dpr_id);

                        database_hanlde.addPTW(model_ptw_status);*/


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

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("ptwrequired", str_ptw_required);
                params.put("ptwno", str_ptw_no);
                params.put("ptwcopy", str_ptw_copy);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "ptwrequired" + " : " + str_ptw_required);
                System.out.println("###  " + "ptwno" + " : " + str_ptw_no);
                System.out.println("###  " + "ptwcopy" + " : " + str_ptw_copy);

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
    protected void onResume() {
        super.onResume();
    }

    public void startTimer_PTW_Status() {

        System.out.println("### startTimer_PTW_Status started ");

        timer_PTW_Status = new Timer();

        initializeTimerTask_PTW_Request();

        timer_PTW_Status.schedule(timerTask_PTW_Status, 60000, 60000); // call api for ` minutes once

    }

    public void stoptimer_PTW_Status() {

        if (timer_PTW_Status != null) {

            timer_PTW_Status.cancel();

            timer_PTW_Status = null;

        }

    }

    public void initializeTimerTask_PTW_Request() {

        timerTask_PTW_Status = new TimerTask() {

            public void run() {

                // use a handler_PTW_Status to run a toast that shows the current timestamp

                System.out.println("### initializeTimerTask_PTW_Request ");
                handler_PTW_Status.post(new Runnable() {

                    public void run() {

                        try {
                            System.out.println("LOCATING?");

                            //get ptw status
                            queue = Volley.newRequestQueue(Activity_Project_MM_In_Progress.this);
                            Function_Get_PTW_Status(str_dpr_id);

                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                });

            }

        };

    }

    /*****************************
    * PHOTO UPLOAD
    ******************************/

    public void startTimer_Photo_Upload() {

        System.out.println("### startTimer_Photo_Upload started ");

        timer_Photo_Upload = new Timer();

        initializeTimerTask_Photo_Upload();

        timer_Photo_Upload.schedule(timerTask_Photo_Upload, LONG_IMAGE_UPLOAD_DELAY_TIME, 60000); // call api for ` minutes once
        // delay time is we need to set time between two task - set 15 minutes delay time

    }

    public void stoptimer_Photo_Upload() {

        if (timer_Photo_Upload != null) {

            timer_Photo_Upload.cancel();

            timer_Photo_Upload = null;

        }

    }

    public void initializeTimerTask_Photo_Upload() {

        timerTask_Photo_Upload = new TimerTask() {

            public void run() {

                // use a handler_PTW_Status to run a toast that shows the current timestamp

                System.out.println("### initializeTimerTask_Photo_Upload ");
                handler_Photo_Upload.post(new Runnable() {

                    public void run() {

                        try {
                            System.out.println("LOCATING?");

                            // create notification
                            Integer int_notification_id  = sharedPreferences.getInt(Config.STR_NOTIFICATION_ID, 0);

                            int_notification_id++;

                            System.out.println("### int_notification_id "+int_notification_id);

                            editor.putInt(Config.STR_NOTIFICATION_ID, int_notification_id);
                            editor.commit();

                            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_MM_In_Progress.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingInten_notification = PendingIntent.getActivity(Activity_Project_MM_In_Progress.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_Project_MM_In_Progress.this, "1")
                                    .setSmallIcon(R.mipmap.ic_iws)
                                    .setContentTitle("OHS Work Photo Upload")
                                    .setContentText("You need to upload OHS Work Photo now, Click to Proceed")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingInten_notification)
                                    .setAutoCancel(true);

                            createNotificationChannel();

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Activity_Project_MM_In_Progress.this);

                            notificationManager.notify(int_notification_id, builder.build());

                            try {
                                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                                r.play();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                });

            }

        };

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /*
     *//********************************
     *FUNCTION LOGIN
     ********************************
     * @param str_dpr_id
     */
    private void Function_Get_PTW_Status(final String str_dpr_id) {

        System.out.println("###  Function_Get_PTW_Status  AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + AppConfig.URL_PROJECT_UPDATE_REACHED_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_REACHED_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("### Function_Get_PTW_Status  AppConfig.URL_PROJECT_UPDATE_REACHED_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        String str_ptw_status = obj.getString(Activity_Project_MM_In_Progress.TAG_PTW_STATUS);

                        if (str_ptw_status.equals("Accepted") || str_ptw_status.equals("Rejected") ){

                            String str_title = "";
                            String str_message = "";
                            if (str_ptw_status.equals("Accepted")){
                                str_title = "PTW Status Accepted";
                                str_message = "Your Received a PTW Status As Accepted. Click To Proceed.";
                            }else{
                                str_title = "PTW Status Rejected";
                                str_message = "Your Received a PTW Status As Rejected. Click To Proceed.";
                            }

                            stoptimer_PTW_Status();

                            editor.putString(TAG_DPR_ID, str_dpr_id);
                            editor.commit();

                            Integer int_notification_id  = sharedPreferences.getInt(Config.STR_NOTIFICATION_ID, 0);

                            int_notification_id++;

                            editor.putInt(Config.STR_NOTIFICATION_ID, int_notification_id);
                            editor.commit();

                            Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_MM_In_Progress.class);
                            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingInten_notification = PendingIntent.getActivity(Activity_Project_MM_In_Progress.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Activity_Project_MM_In_Progress.this, "1")
                                    .setSmallIcon(R.mipmap.ic_iws)
                                    .setContentTitle(str_title)
                                    .setContentText(str_message)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingInten_notification)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Activity_Project_MM_In_Progress.this);

                            notificationManager.notify(int_notification_id, builder.build());


                             try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        }

                    } else if (status == 400) {


                    } else if (status == 404) {


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println("### Function_Get_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("###  AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### Function_Get_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + "dpr_id" + " : " + str_dpr_id);

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

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "OHS Work Image Upload Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

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

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Work Status Updated Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        button_submit_work_status.setEnabled(false);
                        button_submit_work_status.setAlpha(.5f);

                        button_submit_left_site.setEnabled(true);
                        button_submit_left_site.setAlpha(1f);

                        // stop show notification for photo upload
                        stoptimer_Photo_Upload();

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

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);
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

                            cardview_work_setup.setVisibility(View.GONE);
                            cardview_start_work.setVisibility(View.GONE);
                            card_view_complete_work.setVisibility(View.VISIBLE);

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

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_GET_LEFT_SITE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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

                        TastyToast.makeText(Activity_Project_MM_In_Progress.this, "Left Site Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS).show();

                        cardview_start_work.setVisibility(View.GONE);
                        card_view_complete_work.setVisibility(View.VISIBLE);

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

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

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
     *********************************/
    private void Function_Completed_Work() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED " + AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Project_MM_In_Progress.this);

                        builder.setTitle("NI-NPO TK TASK ");
                        builder.setMessage("Work Completed Successfully.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Activity_Project_MM_In_Progress.this, Activity_Project_Pager.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

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

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Project_MM_In_Progress.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);


                System.out.println("### AppConfig.URL_PROJECT_UPDATE_WORK_COMPLETED " + "dpr_id" + " : " + str_dpr_id);

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


    public void Function_Time_Counter() {

        Timer T = new Timer();
        T.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                count++;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int seconds = count;
                        int int_seconds = seconds % 60;
                        int int_hours = seconds / 60;
                        int int_minutes = int_hours % 60;
                        int_hours = int_hours / 60;

                        String string_seconds = "";
                        if (int_seconds >= 0 && int_seconds <= 9)
                            string_seconds = "0" + int_seconds;
                        else
                            string_seconds = "" + int_seconds;

                        String string_minutes = "00";
                        if (int_minutes >= 0 && int_minutes <= 9)
                            string_minutes = "0" + int_minutes;
                        else
                            string_minutes = "" + int_minutes;

                        String string_hours = "00";
                        if (int_hours >= 0 && int_hours <= 9)
                            string_hours = "0" + int_hours;
                        else
                            string_hours = "" + int_hours;

                        System.out.print(int_hours + ":" + int_minutes + ":" + int_seconds);
                        text_view_timer.setText(string_hours + ":" + string_minutes + ":" + string_seconds);

                    }
                });


            }
        }, 1000, 1000);

    }

}
