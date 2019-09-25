package com.banyan.androidiws.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Account_Details;
import com.banyan.androidiws.activity.Activity_Attendance_Report_For_Months;
import com.banyan.androidiws.activity.Activity_Expense;
import com.banyan.androidiws.activity.Activity_Inventory;
import com.banyan.androidiws.activity.Activity_Main;
import com.banyan.androidiws.activity.Activity_Productivity_NI_NPO_TK;
import com.banyan.androidiws.activity.Activity_Project_NI_NPO_TK_Completed;
import com.banyan.androidiws.activity.Activity_Project_Pager;
import com.banyan.androidiws.adapter.Adapter_Children_List;
import com.banyan.androidiws.adapter.Adapter_Document_List;
import com.banyan.androidiws.adapter.Adapter_Experience_List;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_Dashboard;
import com.banyan.androidiws.database.Model_Profile;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

import static android.content.Context.LOCATION_SERVICE;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_FARM_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_FAT_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_FAT_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_FORM_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_MFC;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_MFC_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_PTI_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_PTI_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_SQPID_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_SQPID_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_TTP_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_TTP_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_WH_COLOR;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_CERTIFICATE_WH_NO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_EMPLOYEE_LEVEL;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_LINE_MANAGER;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_PHOTO;
import static com.banyan.androidiws.activity.Activity_Account_Details.TAG_REPORT_MANAGER;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Fragment_Main_Menu extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = Fragment_Main_Menu.class.getSimpleName();

    private static final String TAG_WORK_START_TIME = "work_start_time";

    private SwipeRefreshLayout swipe_refresh;

    private CircleImageView image_view_profile;

    private GridLayout mainGrid;

    private TextView text_view_name, text_view_designation, text_view_current_project, c, text_view_line_manager, text_view_circle,
            text_view_reporting_manager, text_view_project_type, text_view_category, text_view_timer, text_view_no_document_expired;

    private CardView cardview_projct_update, cardview_expense, cardview_my_account, cardview_productivity, cardview_inventory,
            cardview_attendance_report;

    private AppCompatButton button_clock_in_out;

    private SpotsDialog dialog;

    private AlertDialog alert_gps;

    private LinearLayoutCompat linear_layout_timer, layout_document_expired;

    private Dialog dialog_location_permission, dialog_device_boot;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Session_Manager session_manager;

    private Utility utility;

    private RequestQueue queue;

    private LocationRequest mLocationRequest;

    private DatabaseHandler db_handle;

    private String str_user_id, str_user_name, str_user_type, str_user_role, str_lat = "", str_long = "", str_provider,
            str_user_department_id, str_project_id, str_header,  str_location;

    private int count, int_certificate_warning_count;

    private boolean bol_update_location;

    private long back_pressed;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    final Integer LOCATION = 0x1;
    final Integer RECEIVE_BOOT_COMPLETED = 0x2;

    private boolean bol_is_online;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        /*********************************
         *  SETUP
         **********************************/
        utility = new Utility();

        // get location

        Function_Setup_GPS();


        /*****************************
         *  SESSION
         *****************************/

        session_manager = new Session_Manager(getContext());
        session_manager.checkLogin();

        HashMap<String, String> user = session_manager.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);
        str_user_department_id = user.get(Session_Manager.KEY_USER_DEPARTMENT_ID);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_role " + str_user_role);
        System.out.println("### str_user_department_id " + str_user_department_id);

        /*****************************
        *  FIND VIEW BY ID
        *****************************/

        swipe_refresh = (SwipeRefreshLayout) root_view.findViewById(R.id.swipe_refresh);

        image_view_profile = (CircleImageView) root_view.findViewById(R.id.image_view_profile);
        mainGrid = (GridLayout) root_view.findViewById(R.id.mainGrid);
        cardview_projct_update = (CardView) root_view.findViewById(R.id.cardview_projct_update);
        cardview_expense = (CardView) root_view.findViewById(R.id.cardview_expense);
        cardview_productivity = (CardView) root_view.findViewById(R.id.cardview_productivity);
        cardview_my_account = (CardView) root_view.findViewById(R.id.cardview_my_account);
        cardview_attendance_report = (CardView) root_view.findViewById(R.id.cardview_attendance_report);
        cardview_inventory = (CardView) root_view.findViewById(R.id.cardview_inventory);
        text_view_name = (TextView) root_view.findViewById(R.id.text_view_name);
        text_view_designation = (TextView) root_view.findViewById(R.id.text_view_designation);
        text_view_current_project = (TextView) root_view.findViewById(R.id.text_view_current_project);
        text_view_category = (TextView) root_view.findViewById(R.id.text_view_category);
        text_view_circle = (TextView) root_view.findViewById(R.id.text_view_circle);
        text_view_reporting_manager = (TextView) root_view.findViewById(R.id.text_view_reporting_manager);
        text_view_line_manager = (TextView) root_view.findViewById(R.id.text_view_line_manager);
        text_view_project_type = (TextView) root_view.findViewById(R.id.text_view_project_type);
        text_view_timer = (TextView) root_view.findViewById(R.id.text_view_timer);
        text_view_no_document_expired = (TextView) root_view.findViewById(R.id.text_view_no_document_expired);
        button_clock_in_out = (AppCompatButton) root_view.findViewById(R.id.button_clock_in_out);

        linear_layout_timer = (LinearLayoutCompat) root_view.findViewById(R.id.linear_layout_timer);
        layout_document_expired = (LinearLayoutCompat) root_view.findViewById(R.id.layout_document_expired);

        swipe_refresh.setOnRefreshListener(this);

        layout_document_expired.setVisibility(View.GONE);
        linear_layout_timer.setVisibility(View.GONE);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        /*****************************
         *  SETUP
         *****************************/

        bol_is_online = utility.IsNetworkAvailable(getActivity());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        bol_is_online = utility.IsNetworkAvailable(getActivity());

        db_handle = new DatabaseHandler(getActivity());

        askForPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED, RECEIVE_BOOT_COMPLETED);

       /* *//*****************************
         *  GET DETAILS
         *****************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {


             /*   swipe_refresh.setRefreshing(true);

                dialog = new SpotsDialog(getContext());
                dialog.show();

                queue = Volley.newRequestQueue(getContext());
                Function_Get_Profile();*/

                if (bol_is_online){

                    swipe_refresh.setRefreshing(true);

                    dialog = new SpotsDialog(getContext());
                    dialog.show();

                    queue = Volley.newRequestQueue(getContext());
                    Function_Get_Profile();


                }else{

                    swipe_refresh.setRefreshing(true);

                    // get data from local db
                    Function_Offline_Get_Profile();
                }

            }
        });




        /*****************************
         *  ACTION
         *****************************/

        cardview_projct_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Project_Pager.class);
                startActivity(intent);

            }
        });

        cardview_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Expense.class);
                startActivity(intent);

            }
        });

        cardview_productivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Productivity_NI_NPO_TK.class);
//                Intent intent = new Intent(getContext(), Activity_Productivity_MM.class);
                startActivity(intent);

            }
        });

        cardview_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Account_Details.class);
                startActivity(intent);

            }
        });

        cardview_attendance_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Attendance_Report_For_Months.class);
                startActivity(intent);

            }
        });

        cardview_inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), Activity_Inventory.class);
                startActivity(intent);

            }
        });


        button_clock_in_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String str_button_name = button_clock_in_out.getText().toString();


                if (str_button_name.equals("CLOCK IN")){

                    Alert_Dialog_Check_In_Out("Clock In", "Are you sure to Clock In");

                }else if (str_button_name.equals("CLOCK OUT")){

                    Alert_Dialog_Check_In_Out("Clock Out", "Are you sure to Clock Out");

                }else if (str_button_name.equals("ALREADY CLOCKED OUT")){

                    TastyToast.makeText(getContext(), "Your Already Clocked Out", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();

                }


            }
        });

        return  root_view;
    }

    private void Function_Offline_Get_Profile(){

        int int_dashboard_count = db_handle.Function_Get_Dashboard_Count();

        if (int_dashboard_count > 0 ){

            int int_dashboard_id = db_handle.Function_Get_Last_Dashboard_id();
            Model_Dashboard dashboard = db_handle.Function_Get_Dashboard(int_dashboard_id);

            String str_first_name = dashboard.getStr_first_name();
            String str_last_name = dashboard.getStr_last_name();
            String str_designation = dashboard.getStr_designation();
            String str_category = dashboard.getStr_category();
            String str_project_name = dashboard.getStr_current_project();
            String str_project_type = dashboard.getStr_project_type();
            String str_photo = dashboard.getStr_photo();
            String str_level = dashboard.getStr_level();
            String str_report_manager = dashboard.getStr_reporting_manager();
            String str_line_manager = dashboard.getStr_line_manager();
            String str_employee_level = dashboard.getStr_level();
            String str_circle = dashboard.getStr_circle();


            Glide.with(getContext())
                    .load(str_photo)
                    .placeholder(R.drawable.ic_user_1)
                    .into(image_view_profile);

            if (str_project_name.isEmpty())
                str_project_name = "-";

            if (str_category.isEmpty())
                str_category = "-";

            if (str_project_type.isEmpty())
                str_project_type = "-";

            if (str_report_manager.isEmpty())
                str_report_manager = "-";

            if (str_line_manager.isEmpty())
                str_line_manager = "-";

            if (int_certificate_warning_count > 0) {
                layout_document_expired.setVisibility(View.VISIBLE);
                text_view_no_document_expired.setText("No of Document Expired : "+int_certificate_warning_count);
            }

            text_view_current_project.setText(str_project_name);
            text_view_category.setText(str_category);
            text_view_circle.setText(str_circle);
            text_view_project_type.setText(str_project_type);
            text_view_reporting_manager.setText(str_report_manager);
            text_view_line_manager.setText(str_line_manager);

            text_view_name.setText(str_first_name+" "+str_last_name);
            text_view_designation.setText(str_designation +" - "+str_level );

        }

        swipe_refresh.setRefreshing(false);

    }

    private void Alert_Dialog_Check_In_Out(String str_title,  String str_message){

        new AlertDialog.Builder(getContext())
                .setTitle(str_title)
                .setMessage(str_message)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String str_button_name = button_clock_in_out.getText().toString();

                        System.out.println("### str_button_name "+str_button_name);

                        Double double_lat = Double.valueOf(str_lat);
                        Double double_long = Double.valueOf(str_long);

                        str_location = new Utility().getAddress(double_lat, double_long, getContext());

                        if (str_button_name.equals("CLOCK IN")){

                            dialogInterface.dismiss();

                            dialog = new SpotsDialog(getContext());
                            dialog.show();
                            Function_Clock_In();

                        }else if (str_button_name.equals("CLOCK OUT")){

                            dialogInterface.dismiss();

                            dialog = new SpotsDialog(getContext());
                            dialog.show();
                            Function_Clock_Out();

                        }else if (str_button_name.equals("ALREADY CLOCKED OUT")){

                            dialogInterface.dismiss();

                            TastyToast.makeText(getContext(), "Your Already Clocked Out", TastyToast.LENGTH_SHORT, TastyToast.INFO).show();

                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(getContext(), "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(getContext(), "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalI == 0) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 1) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 2) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 3) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 4) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 5) {
                        Toast.makeText(getContext(), "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }

                    /*Intent intent = new Intent(getContext(),ActivityOne.class);
                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    startActivity(intent);*/

                }
            });
        }
    }


    public void Function_Setup_GPS(){

        System.out.println("### "+TAG+" Function_Setup_GPS");
        try{

            if (str_lat.isEmpty() || str_long.isEmpty()){


                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);


                LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

                try {
                    System.out.println("### "+TAG+" turnGPSOn Called");
                    turnGPSOn();
                    System.out.println("### "+TAG+" turnGPSOn Done");
                } catch (Exception e) {

                }

                if (alert_gps!= null)
                    alert_gps.dismiss();

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                } else {
                    showGPSDisabledAlertToUser();
                }

                if (str_provider.contains("gps")) { // get location only if gps is enabled
                    bol_update_location = true;
                    startLocationUpdates();
                }


            }

        }catch (Exception e){
            System.out.println("### "+TAG+" Exception "+e.getLocalizedMessage());
        }

    }



    /*******************************
     * Enable GPS
     ******************************/

    private void turnGPSOn() {

        System.out.println("Inside GPS 0 ");

        str_provider = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!str_provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            this.getActivity().sendBroadcast(poke);

            System.out.println("Inside GPS 1");
        }
    }

    /********************************
     * Check GPS Connection is Enabled
     *********************************/

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage("GPS IS DISABLED IN YOUR DEVICE. WOULD YOU LIKE TO ENABLE IT?")
                .setCancelable(false)
                .setPositiveButton("GOTO SETTTINGS PAGE TO ENABLE GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        getActivity().finish();
                        getActivity().finishAffinity();
                    }
                });
        alert_gps = alertDialogBuilder.create();
        if (alert_gps!= null)
            alert_gps.show();
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        System.out.println("### Fragment Attendance : startLocationUpdates ");
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//        old
//        mLocationRequest.setNumUpdates(1);

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(getActivity());
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the ic_user_1 grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
            return;
        }
        getFusedLocationProviderClient(getActivity()).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());

    }

    public void onLocationChanged(Location location) {
        System.out.println("### Fragment Attendance : onLocationChanged");
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        str_lat = String.valueOf(latitude);
        str_long = String.valueOf(longitude);

        // You can now create a LatLng Object for use with maps
        System.out.println("### msg "+msg);
        System.out.println("### LATT :: " + str_lat);
        System.out.println("### LONGG :: " + str_long);

        bol_update_location = false;

    }


    /*********************************
     * For Loaction
     ********************************/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if ic_user_1 has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(getActivity(), permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:

                    break;
                //Call
                case 2:

                    break;
            }

            Toast.makeText(getActivity(), "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestCode) {
                //Location
                case 1:

                    System.out.println("### location denied requestCode "+requestCode);

                    if (dialog_location_permission != null)
                        dialog_location_permission.dismiss();

                    dialog_location_permission = new Dialog(getActivity());
                    dialog_location_permission.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_location_permission.setContentView(R.layout.custom_dialog2);
                    dialog_location_permission.setCancelable(false);
                    TextView text=(TextView)dialog_location_permission.findViewById(R.id.text);
                    text.setText("Please give Location permission to use this app.");
                    Button ok=(Button)dialog_location_permission.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
                            dialog_location_permission.dismiss();

                        }
                    });
                    dialog_location_permission.show();

                    break;
                //Call
                case 2:
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();

                    if (dialog_device_boot != null)
                        dialog_device_boot.dismiss();

                    dialog_device_boot = new Dialog(getActivity());
                    dialog_device_boot.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_device_boot.setContentView(R.layout.custom_dialog2);
                    dialog_device_boot.setCancelable(false);
                    TextView text_boot=(TextView)dialog_device_boot.findViewById(R.id.text);
                    text_boot.setText("Please give permission to know when device booted to use this app.");
                    Button button_ok=(Button)dialog_device_boot.findViewById(R.id.ok);
                    button_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            askForPermission(Manifest.permission.RECEIVE_BOOT_COMPLETED, RECEIVE_BOOT_COMPLETED);
                            dialog_device_boot.dismiss();

                        }
                    });
                    dialog_device_boot.show();


                    break;
                //Device ID
                case 3:
                    Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
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

                        String str_salute = obj_one.getString(Activity_Account_Details.TAG_SALUTE);
                        String str_first_name = obj_one.getString(Activity_Account_Details.TAG_FIRST_NAME);
                        String str_last_name = obj_one.getString(Activity_Account_Details.TAG_LAST_NAME);
                        String str_dob = obj_one.getString(Activity_Account_Details.TAG_DOB);
                        String str_designation = obj_one.getString(Activity_Account_Details.TAG_DESIGNATION);
                        str_header = obj_one.getString(Activity_Account_Details.TAG_HEADER);
                        String str_category = obj_one.getString(Activity_Account_Details.TAG_CATEGORY);
                        String str_comp_city = obj_one.getString(Activity_Account_Details.TAG_COMP_CITY);
                        String str_project_name = obj_one.getString(Activity_Account_Details.TAG_PROJECT_NAME);
                        str_project_id = obj_one.getString(Activity_Account_Details.TAG_PROJECT_ID);
                        String str_project_type = obj_one.getString(Activity_Account_Details.TAG_PROJECT_TYPE);
                        String str_photo = obj_one.getString(TAG_PHOTO);
                        String str_level = obj_one.getString(TAG_EMPLOYEE_LEVEL);
                        String str_report_manager = obj_one.getString(TAG_REPORT_MANAGER);
                        String str_line_manager = obj_one.getString(TAG_LINE_MANAGER);
                        String str_employee_level = obj_one.getString(TAG_EMPLOYEE_LEVEL);

                        JSONObject json_object_certificate = obj.getJSONObject(TAG_CERTIFICATE);
                        String str_mfc =json_object_certificate.getString(TAG_CERTIFICATE_MFC);
                        String str_mfc_color =json_object_certificate.getString(TAG_CERTIFICATE_MFC_COLOR);
                        String str_fat =json_object_certificate.getString(TAG_CERTIFICATE_FAT_NO);
                        String str_fat_color =json_object_certificate.getString(TAG_CERTIFICATE_FAT_COLOR);
                        String str_farm_no =json_object_certificate.getString(TAG_CERTIFICATE_FARM_NO);
                        String str_form_color =json_object_certificate.getString(TAG_CERTIFICATE_FORM_COLOR);
                        String str_wh =json_object_certificate.getString(TAG_CERTIFICATE_WH_NO);
                        String str_wh_color =json_object_certificate.getString(TAG_CERTIFICATE_WH_COLOR);
                        String str_pti_no =json_object_certificate.getString(TAG_CERTIFICATE_PTI_NO);
                        String str_pti_color =json_object_certificate.getString(TAG_CERTIFICATE_PTI_COLOR);
                        String str_sqpid =json_object_certificate.getString(TAG_CERTIFICATE_SQPID_NO);
                        String str_sqpid_color =json_object_certificate.getString(TAG_CERTIFICATE_SQPID_COLOR);
                        String str_ttp_no =json_object_certificate.getString(TAG_CERTIFICATE_TTP_NO);
                        String str_ttp_color =json_object_certificate.getString(TAG_CERTIFICATE_TTP_COLOR);


                        if (!str_dob.isEmpty())
                            str_dob = utility.Function_Date_Formate(str_dob, "yyyy-MM-dd", "dd-MM-yyyy");

                        Glide.with(getContext())
                                .load(str_photo)
                                .placeholder(R.drawable.ic_user_1)
                                .into(image_view_profile);

                        if (str_project_name.isEmpty())
                            str_project_name = "-";

                        if (str_category.isEmpty())
                            str_category = "-";

                        if (str_comp_city.isEmpty())
                            str_comp_city = "-";

                        if (str_project_type.isEmpty())
                            str_project_type = "-";

                        if (str_report_manager.isEmpty())
                            str_report_manager = "-";

                        if (str_line_manager.isEmpty())
                            str_line_manager = "-";

                        if (str_mfc_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_fat_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_form_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_wh_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_pti_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_sqpid_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (str_ttp_color.equals("1")) {
                            int_certificate_warning_count++;
                        }

                        if (int_certificate_warning_count > 0) {
                            layout_document_expired.setVisibility(View.VISIBLE);
                            text_view_no_document_expired.setText("No of Document Expired : "+int_certificate_warning_count);
                        }

                        text_view_current_project.setText(str_project_name);
                        text_view_category.setText(str_category);
                        text_view_circle.setText(str_comp_city);
                        text_view_project_type.setText(str_project_type);
                        text_view_reporting_manager.setText(str_report_manager);
                        text_view_line_manager.setText(str_line_manager);

                        text_view_name.setText(str_first_name+" "+str_last_name);
                        text_view_designation.setText(str_designation +" - "+str_employee_level );


                        int int_dashboard_count = db_handle.Function_Get_Dashboard_Count();

                        if (int_dashboard_count == 0){

                            Model_Dashboard model_dashboard = new Model_Dashboard(0, str_first_name, str_last_name, str_designation, str_photo, str_level,
                                    ""+int_certificate_warning_count, str_project_name,str_category, str_comp_city, str_project_type,
                                    str_report_manager,str_line_manager);

                            db_handle.Function_Add_Dashboard(model_dashboard);

                        }else if (int_dashboard_count > 0){

                            int int_profile_id = db_handle.Function_Get_Last_Profile_id();
                            Model_Dashboard model_dashboard = new Model_Dashboard(int_profile_id,str_first_name, str_last_name, str_designation, str_photo, str_level,
                                    ""+int_certificate_warning_count, str_project_name,str_category, str_comp_city, str_project_type,
                                    str_report_manager,str_line_manager);
                            db_handle.Function_Update_Dashboard(model_dashboard);

                        }

                    } else if (status == 400) {

                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }

                queue = Volley.newRequestQueue(getContext());
                Function_Check_Clock_In_Out();

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(getActivity());

                queue = Volley.newRequestQueue(getContext());
                Function_Check_Clock_In_Out();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("###" + "userid" + " : " + str_user_id);

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
     *FUNCTION CLOCK IN
     *********************************/
    private void Function_Clock_In() {

        System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN " + AppConfig.URL_ATTENDANCE_CHECK_IN);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_CHECK_IN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE_CHECK_IN : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        button_clock_in_out.setText("CLOCK OUT");

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());

                        editor.putString(TAG_WORK_START_TIME, formattedDate);
                        editor.commit();

                        linear_layout_timer.setVisibility(View.VISIBLE);
                        Function_Time_Counter();

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(getActivity());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);
                params.put("usertype", str_user_type);
                params.put("dept_id", str_user_department_id);
                params.put("lat", str_lat);
                params.put("lon", str_long);
                params.put("project_id", str_project_id);
                params.put("remarks", "");
                params.put("location", str_location);

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN " + "userid" + " : " + str_user_id);
                System.out.println("### " + "usertype" + " : " + str_user_type);
                System.out.println("### " + "dept_id" + " : " + str_user_department_id);
                System.out.println("### " + "lat" + " : " + str_lat);
                System.out.println("### " + "lon" + " : " + str_long);
                System.out.println("### " + "project_id" + " : " + str_project_id);
                System.out.println("### " + "remarks" + " : " + "");
                System.out.println("### " + "location" + " : " + str_location);

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
     *FUNCTION CLOCK IN
     *********************************/
    private void Function_Clock_Out() {

        System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_OUT " + AppConfig.URL_ATTENDANCE_CHECK_OUT);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_CHECK_OUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE_CHECK_OUT : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        button_clock_in_out.setText("ALREADY CLOCKED OUT");

                        editor.putString(TAG_WORK_START_TIME, "");
                        editor.commit();

                        linear_layout_timer.setVisibility(View.GONE);

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_OUT onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(getActivity());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);
                params.put("usertype", str_user_type);
                params.put("dept_id", str_user_department_id);
                params.put("lat", str_lat);
                params.put("lon", str_long);
                params.put("location", str_location);

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_OUT " + "userid" + " : " + str_user_id);
                System.out.println("### " + "usertype" + " : " + str_user_type);
                System.out.println("### " + "dept_id" + " : " + str_user_department_id);
                System.out.println("### " + "lat" + " : " + str_lat);
                System.out.println("### " + "lon" + " : " + str_long);
                System.out.println("### " + "location" + " : " + str_location);

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
     *FUNCTION CHECK CHECK IN
     *********************************/
    private void Function_Check_Clock_In_Out() {

        System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN " + AppConfig.URL_ATTENDANCE_CHECK);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_CHECK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE_CHECK_IN : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        String str_status_id = obj.getString("status_id");

                        if (str_status_id.equals("1")){

                            button_clock_in_out.setText("CLOCK IN");

                        }else if (str_status_id.equals("2")){

                            linear_layout_timer.setVisibility(View.VISIBLE);

                            Calendar c = Calendar.getInstance();

                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String str_current_time = format.format(c.getTime());
                            String str_work_start_time = sharedPreferences.getString(TAG_WORK_START_TIME, "");

                            System.out.println("### str_current_time "+str_current_time);
                            System.out.println("### str_work_start_time "+str_work_start_time);

                            Date date_work_start_time = null;
                            Date date_current_time = null;

                            try {
                                date_work_start_time = format.parse(str_work_start_time);
                                date_current_time = format.parse(str_current_time);

                                //in milliseconds
                                long diff = date_current_time.getTime() - date_work_start_time.getTime();

                                long diffSeconds = diff / 1000;

                                System.out.println("### diffSeconds "+diffSeconds);
                                System.out.println("### diff "+diff);

                                count = (int)diffSeconds;
                                Function_Time_Counter();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            button_clock_in_out.setText("CLOCK OUT");

                        }else if (str_status_id.equals("3")){
                            button_clock_in_out.setText("ALREADY CLOCKED OUT");

                            editor.putString(TAG_WORK_START_TIME, "");
                            editor.commit();

                            linear_layout_timer.setVisibility(View.GONE);
                        }

                    } else if (status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(getActivity());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);
                params.put("usertype", str_user_type);
                params.put("dept_id", str_user_department_id);

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN " + "userid" + " : " + str_user_id);
                System.out.println("### " + "usertype" + " : " + str_user_type);
                System.out.println("### " + "dept_id" + " : " + str_user_department_id);
                System.out.println("### " + "lat" + " : " + str_lat);


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

                if (getActivity() == null)
                    return;

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        int seconds = count;
                        int int_seconds = seconds % 60;
                        int int_hours = seconds / 60;
                        int int_minutes = int_hours % 60;
                        int_hours = int_hours / 60;

                        String string_seconds = "";
                        if (int_seconds >= 0 && int_seconds <= 9)
                            string_seconds = "0"+int_seconds;
                        else
                            string_seconds = ""+int_seconds;

                        String string_minutes = "00";
                        if (int_minutes >= 0 && int_minutes <= 9)
                            string_minutes = "0"+int_minutes;
                        else
                            string_minutes = ""+int_minutes;

                        String string_hours = "00";
                        if (int_hours >= 0 && int_hours <= 9)
                            string_hours = "0"+int_hours;
                        else
                            string_hours = ""+int_hours;

                        System.out.print(int_hours + ":" + int_minutes + ":" + int_seconds);
                        text_view_timer.setText(string_hours + ":" + string_minutes + ":" + string_seconds);

                    }
                });


            }
        }, 1000, 1000);

    }



    @Override
    public void onRefresh() {

          /*   swipe_refresh.setRefreshing(true);

                dialog = new SpotsDialog(getContext());
                dialog.show();

                queue = Volley.newRequestQueue(getContext());
                Function_Get_Profile();*/

        if (bol_is_online){

            swipe_refresh.setRefreshing(true);

            dialog = new SpotsDialog(getContext());
            dialog.show();

            queue = Volley.newRequestQueue(getContext());
            Function_Get_Profile();


        }else{

            swipe_refresh.setRefreshing(true);

            // get data from local db
            Function_Offline_Get_Profile();
        }

    }
}
