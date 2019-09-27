package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_Profile;
import com.banyan.androidiws.fragment.Fragment_Main_Menu;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.banyan.androidiws.service.Service_Upload_Project_Data;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.sdsmdg.tastytoast.TastyToast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class Activity_Main extends AppCompatActivity {

    private static final String TAG_HOME = "HOME";
    private static final String TAG_PROJECT_UPDATE = "PROJECT_UPDATE";
    private static final String TAG_EXPENSE = "EXPENCE";
    private static final String TAG_PRODUCTIVITY = "PRODUCTIVITY";
    private static final String TAG_ACCOUNT = "ACCOUNT";
    private static final String TAG_ATTENDANCE = "ATTENDANCE";
    private static final String TAG_ATTENDANCE_REPORT = "ATTENDANCE_REPORT";
    private static final String TAG_INVENTORY = "APPLY LEAVE";
    private static final String TAG_ABOUTS = "ABOUTS";
    private static final String TAG_LOGOUT = "LOGOUT";

    private static final String TAG_USER_ID = "userid";
    private static final String TAG_USER_TYPE = "usertype";
    private static final String TAG_USER_ROLE = "userrole";
    private static final String TAG_USER_DEPARTMENT_ID = "departmentid";

    private static final String TAG = Activity_Main.class.getSimpleName();

    public static final String TAG_ACTIVITY_ATTEDANCE_REPORT = "Activity_Payroll_For_Months";
    public static final String TAG_ACTIVITY_LEAVE_REQUEST = "Activity_Leave_List";
    public static final String TAG_CALLING_ACTIVITY = "calling_activity";

    public static final String TAG_SALUTE = "salute";
    public static final String TAG_FIRST_NAME = "fname";
    public static final String TAG_LAST_NAME = "lname";
    public static final String TAG_DOB = "dob";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_DESIGNATION = "designation";
    public static final String TAG_ADDRESS_1 = "address1";
    public static final String TAG_ADDRESS_2 = "address2";
    public static final String TAG_CITY = "city";
    public static final String TAG_AADHAR_NO = "aadharno";
    public static final String TAG_PAN_NO = "panno";
    public static final String TAG_GENDER = "gender";
    public static final String TAG_PHOTO = "profile_photo";
    public static final String TAG_LEVEL = "level";

    // 6.0 Location & Call
    private final Integer LOCATION = 0x1;
    private final Integer CALL = 0x2;
    private final Integer GPS_SETTINGS = 0x7;

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private View navHeader;

    private CircleImageView image_profile;

    private TextView txt_name, text_profile_information, text_Notification_Item_Count;

    private Toolbar toolbar;

    private TimerTask timerTask;

    private final Handler handler = new Handler();

    // Session manager
    private Session_Manager session;

    private Session_Manager session_manager;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Utility utility;

    private DatabaseHandler db_handle;

    private String[] activityTitles;

    private String TAG_CURRENT = TAG_HOME, str_user_id = "", str_user_name = "", str_user_name_first_last ="", str_user_role = "", str_user_image = "", str_user_type = "", str_user_department_id = "";

    private  long back_pressed;

    public  int navItemIndex = 0, int_notification_count = 0;

    private boolean shouldLoadHomeFragOnBackPress = true, bol_is_online, bol_can_logout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();

        new Utility().Function_No_Internet_Dialog(this);

        /*************************
        *  SESSION
        **************************/

        session = new Session_Manager(getApplicationContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_department_id = user.get(Session_Manager.KEY_USER_DEPARTMENT_ID);
        str_user_image = user.get(Session_Manager.KEY_USER_IMAGE);
        str_user_name_first_last = user.get(Session_Manager.KEY_USER_NAME_FIRST_LAST);

        if (str_user_name_first_last == null)
            str_user_name_first_last = "";

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_role " + str_user_role);
        System.out.println("### str_user_image " + str_user_image);
        System.out.println("### str_user_name_first_last " + str_user_name_first_last);

        /*************************
         *  FIND VIEW BY ID
         **************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        txt_name = (TextView) navHeader.findViewById(R.id.profile_name);
        image_profile = (CircleImageView) navHeader.findViewById(R.id.img_profile);
        text_profile_information = (TextView) navHeader.findViewById(R.id.profile_information);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        /*************************
         *  SETUP
         **************************/

        Function_Verify_Network_Available(this);

        bol_is_online = utility.IsNetworkAvailable(this);
        db_handle = new DatabaseHandler(this);
        // initializing navigation menu
        setUpNavigationView();

        session_manager = new Session_Manager(this);

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        try {
            System.out.println("Called");
            turnGPSOn();
            System.out.println("Done");
        } catch (Exception e) {

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            showGPSDisabledAlertToUser();
        }



        /*************************
         *  SETUP
         **************************/




        /*************************
         *  GET DATA
         *************************/

     /*   dialog = new SpotsDialog(this);
        dialog.show();
        queue = Volley.newRequestQueue(this);
        Function_Get_Profile();*/

        if (bol_is_online){

            dialog = new SpotsDialog(this);
            dialog.show();
            queue = Volley.newRequestQueue(this);
            Function_Get_Profile();

        }else{
            // get data from local db
            Function_Offline_Get_Profile();
        }



        /*********************************
         * ACTION
         **********************************/

        if (savedInstanceState == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String back_option = preferences.getString(TAG_CALLING_ACTIVITY, "");
            if (back_option.isEmpty()){ // load default fragment

                navItemIndex = 0;
                TAG_CURRENT = TAG_HOME;
                loadHomeFragment();

            }else if (back_option.equals(TAG_ACTIVITY_LEAVE_REQUEST)) {

                navItemIndex = 6;
                TAG_CURRENT = TAG_INVENTORY;
                loadHomeFragment();

            }
            // set calling activity = ""
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(TAG_CALLING_ACTIVITY, "");
            editor.commit();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.action_cart);

        View actionView = menuItem.getActionView();
        text_Notification_Item_Count = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_cart){

                Intent intent = new Intent(Activity_Main.this, Activity_Notification_List.class);
                startActivity(intent);

            return true;
        }else if (item.getItemId() == R.id.action_logout){

            if (bol_can_logout){

                session_manager.logoutUser();
                Toast.makeText(Activity_Main.this, "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
            }else{
                TastyToast.makeText(Activity_Main.this, "You need to Clock Out First, Then only you can Logout.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            return true;
        }

        return false;
    }

    private void setupBadge() {

        if (text_Notification_Item_Count != null) {
            if (int_notification_count == 0) {
                if (text_Notification_Item_Count.getVisibility() != View.GONE) {
                    text_Notification_Item_Count.setVisibility(View.GONE);
                }
            } else {
                text_Notification_Item_Count.setText(String.valueOf(Math.min(int_notification_count, 99)));
                if (text_Notification_Item_Count.getVisibility() != View.VISIBLE) {
                    text_Notification_Item_Count.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        /*************************
         *  PUSH DATA
         *************************/

        db_handle = new DatabaseHandler(this);

        boolean bol_is_internet_connected = new Utility().IsNetworkAvailable(this);

        if (bol_is_internet_connected){

            Integer int_reached_site_count = db_handle.Function_Get_Reached_Site_Count();
            Integer int_declaration_count = db_handle.Function_Get_Declaration_Count();
            Integer int_ptw_request_count = db_handle.Function_Get_PTW_Request_Count();
            Integer int_start_work_count = db_handle.Function_Get_Start_Work_Count();
            Integer int_ohs_work_count = db_handle.Function_Get_OHS_Work_Count();

            System.out.println("### int_reached_site_count "+int_reached_site_count);
            System.out.println("### int_declaration_count "+int_declaration_count);
            System.out.println("### int_ptw_request_count "+int_ptw_request_count);
            System.out.println("### int_start_work_count "+int_start_work_count);
            System.out.println("### int_ohs_work_count "+int_ohs_work_count);

            if (int_reached_site_count == 0 && int_declaration_count == 0 && int_ptw_request_count == 0 &&
                    int_start_work_count == 0 && int_ohs_work_count == 0){

            }else{
                // call uploading service
                Intent intent_ptw_status = new Intent(this, Service_Upload_Project_Data.class);
                startService(intent_ptw_status);
            }

        }

    }

    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();
        System.out.println();
        System.out.println("###  getSupportFragmentManager().findFragmentByTag(TAG_CURRENT) "+getSupportFragmentManager().findFragmentByTag(TAG_CURRENT));
        if (getSupportFragmentManager().findFragmentByTag(TAG_CURRENT) != null) {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                System.out.println("###  loadHomeFragment TAG_CURRENT "+TAG_CURRENT);
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                System.out.println("###  loadHomeFragment fragment  "+fragment);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG_CURRENT);
                fragmentTransaction.commitAllowingStateLoss();

            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            handler.post(mPendingRunnable);
        }

        drawer.closeDrawers();

        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        System.out.println("### getHomeFragment navItemIndex "+navItemIndex);
        switch (navItemIndex) {
            case 0:
                Fragment_Main_Menu fragment_main_menu1 = new Fragment_Main_Menu();
                return fragment_main_menu1;

            case 1:
                Fragment_Main_Menu fragment_main_menu2 = new Fragment_Main_Menu();
                return fragment_main_menu2;

            case 2:
                Fragment_Main_Menu fragment_main_menu3 = new Fragment_Main_Menu();
                return fragment_main_menu3;

            case 3:
                Fragment_Main_Menu fragment_main_menu4 = new Fragment_Main_Menu();
                return fragment_main_menu4;


            case 4:
                Fragment_Main_Menu fragment_main_menu5 = new Fragment_Main_Menu();
                return fragment_main_menu5;


            case 5:
                Fragment_Main_Menu fragment_main_menu6 = new Fragment_Main_Menu();
                return fragment_main_menu6;


            case 6:
                Fragment_Main_Menu fragment_main_menu7 = new Fragment_Main_Menu();
                return fragment_main_menu7;


            case 7:
                Fragment_Main_Menu fragment_main_menu8 = new Fragment_Main_Menu();
                return fragment_main_menu8;


            case 8:
                session_manager.logoutUser();
                Toast.makeText(Activity_Main.this, "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                return new Fragment_Main_Menu();

            default:
                return new Fragment_Main_Menu();
        }
    }


    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {

        navigationView.getMenu().getItem(navItemIndex).setChecked(true);

    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        TAG_CURRENT = TAG_HOME;
                        System.out.println("### onNavigationItemSelected  TAG_HOME ");
                        break;

                    case R.id.nav_project_update:
                        navItemIndex = 1;
                        TAG_CURRENT = TAG_PROJECT_UPDATE;
//                        Intent intent = new Intent(Activity_Main.this, Activity_Main.class);
                        Intent intent = new Intent(Activity_Main.this, Activity_Project_Pager.class);
                        startActivity(intent);
                        break;

                    case R.id.nav_expense:
                        navItemIndex = 2;
                        TAG_CURRENT = TAG_EXPENSE;
                        Intent intent2 = new Intent(Activity_Main.this, Activity_Expense.class);
                        startActivity(intent2);
                        break;

                    case R.id.nav_productivity:
                        navItemIndex = 3;
                        TAG_CURRENT = TAG_PRODUCTIVITY;
                        Intent intent_productvity = new Intent(Activity_Main.this, Activity_Productivity_NI_NPO_TK.class);
                        startActivity(intent_productvity);
                        break;

                    case R.id.nav_my_account:
                        navItemIndex = 4;
                        TAG_CURRENT = TAG_ACCOUNT;
                        Intent intent_acount = new Intent(Activity_Main.this, Activity_Account_Details.class);
                        startActivity(intent_acount);
                        break;

                    case R.id.nav_attendance:
                        navItemIndex = 5;
                        TAG_CURRENT = TAG_ATTENDANCE;
                        Intent intent_attendance = new Intent(Activity_Main.this, Activity_Payroll_For_Months.class);
                        startActivity(intent_attendance);
                        break;

                    case R.id.nav_inventory:
                        navItemIndex = 6;
                        TAG_CURRENT = TAG_INVENTORY;
                        Intent intent3 = new Intent(Activity_Main.this, Activity_Inventory.class);
                        startActivity(intent3);
                        break;

                    case R.id.nav_about_us:
                        navItemIndex = 7;
                        TAG_CURRENT = TAG_ABOUTS;
                        Intent intent4 = new Intent(Activity_Main.this, Activity_About.class);
                        startActivity(intent4);
                        break;

                    case R.id.nav_logout:
                        navItemIndex = 0;

                        if (bol_can_logout){

                            TastyToast.makeText(getApplicationContext(), "Logged Out Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                            session_manager.logoutUser();

                        }else{
                            TastyToast.makeText(Activity_Main.this, "You need to Clock Out First, Then only you can Logout.", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                            Intent intent_5 = new Intent(Activity_Main.this, Activity_Main.class);
                            startActivity(intent_5);

                        }

                        break;

                    default:
                        navItemIndex = 0;

                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {

                    menuItem.setChecked(false);

                } else {

                    menuItem.setChecked(true);

                }

                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {

                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {

                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

            }
        };

        //Setting the actionbarToggle to drawer layout
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimary));
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

    }

    /********************************
     * Check GPS Connection is Enabled
     *********************************/

    private void showGPSDisabledAlertToUser() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);

                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                        finishAffinity();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    /*******************************
     * Enable GPS
     ******************************/

    private void turnGPSOn() {

        System.out.println("Inside GPS");
        System.out.println("Inside GPS 0 ");

        String provider = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getApplicationContext().sendBroadcast(poke);

            System.out.println("Inside GPS 1");
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

                        String str_salute = obj_one.getString(TAG_SALUTE);
                        String str_first_name = obj_one.getString(TAG_FIRST_NAME);
                        String str_last_name = obj_one.getString(TAG_LAST_NAME);
                        String str_dob = obj_one.getString(TAG_DOB);
                        String str_email = obj_one.getString(TAG_EMAIL);
                        String str_designation = obj_one.getString(TAG_DESIGNATION);
                        String str_address1 = obj_one.getString(TAG_ADDRESS_1);
                        String str_address2 = obj_one.getString(TAG_ADDRESS_2);
                        String str_city = obj_one.getString(TAG_CITY);
                        String str_aadhar = obj_one.getString(TAG_AADHAR_NO);
                        String str_pan_no = obj_one.getString(TAG_PAN_NO);
                        String str_gender = obj_one.getString(TAG_GENDER);
                        String str_photo = obj_one.getString(TAG_PHOTO);
                        String str_level = obj_one.getString(TAG_LEVEL);


                        System.out.println("### str_photo "+str_photo);
                        str_user_name = str_first_name+" "+str_last_name;
                        // name, website
                        if (str_user_name != null)
                            txt_name.setText(str_user_name);

                        // Loading profile image
                        if (str_photo != null && !str_photo.isEmpty())
                            Glide.with(Activity_Main.this)
                                    .load(str_photo )
                                    .placeholder(R.drawable.ic_user_1)
                                    .into(image_profile);

                        if (str_designation!= null)
                            text_profile_information.setText(str_designation + " - "+str_level);


                        int int_profile_count = db_handle.Function_Get_Profile_Count();

                        if (int_profile_count == 0){

                            Model_Profile model_profile = new Model_Profile(0, str_first_name, str_last_name, str_designation, str_photo, str_level);
                            db_handle.Function_Add_Profile(model_profile);

                        }else if (int_profile_count > 0){

                            int int_profile_id = db_handle.Function_Get_Last_Profile_id();
                            Model_Profile model_profile = new Model_Profile(int_profile_id, str_first_name, str_last_name, str_designation, str_photo, str_level);
                            db_handle.Function_Update_Profile(model_profile);

                        }

                    } else if (status == 400) {

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                queue = Volley.newRequestQueue(Activity_Main.this);
                Function_Check_Clock_In_Out();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Main.this);

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

                            bol_can_logout = true;

                        }else if (str_status_id.equals("2")){

                            bol_can_logout = false;

                        }else if (str_status_id.equals("3")){

                            bol_can_logout = true;
                        }

                    } else if (status == 400) {

                        TastyToast.makeText(Activity_Main.this, "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(Activity_Main.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }

                queue = Volley.newRequestQueue(Activity_Main.this);
                Function_Notification_List();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_ATTENDANCE_CHECK_IN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Main.this);

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
     *FUNCTION NOTIFICATION LIST
     *********************************/
    private void Function_Notification_List() {

        System.out.println("### AppConfig.URL_NOTIFICATION_LIST " + AppConfig.URL_NOTIFICATION_LIST);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_NOTIFICATION_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_NOTIFICATION_LIST : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200) {

                        JSONArray array_request = obj.getJSONArray("records");

                        int_notification_count = array_request.length();

                        dialog.dismiss();


                    } else if (status == 400) {

                        int_notification_count = 0;

                        dialog.dismiss();
                    } else if (status == 404) {

                        int_notification_count = 0;
                        dialog.dismiss();
                    }

                    setupBadge(); // update notification count

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

                System.out.println("### AppConfig.URL_NOTIFICATION_LIST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_NOTIFICATION_LIST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Main.this);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(Activity_Leave_List.TAG_USER_ID, str_user_id);

                System.out.println("###" + Activity_Leave_List.TAG_USER_ID + " : " + str_user_id);

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

    private void Function_Offline_Get_Profile(){

        int int_profile_count = db_handle.Function_Get_Profile_Count();

        if (int_profile_count > 0 ){

            int int_profile_id = db_handle.Function_Get_Last_Profile_id();
            Model_Profile profile = db_handle.Function_Get_Profile(int_profile_id);

            String str_first_name = profile.getStr_first_name();
            String str_last_name = profile.getStr_last_name();
            String str_designation = profile.getStr_designation();
            String str_photo = profile.getStr_photo();
            String str_level = profile.getStr_level();

            System.out.println("### str_photo "+str_photo);
            str_user_name = str_first_name+" "+str_last_name;
            // name, website
            if (str_user_name != null)
                txt_name.setText(str_user_name);

            // Loading profile image
            if (str_photo != null && !str_photo.isEmpty())
                Glide.with(Activity_Main.this)
                        .load(str_photo )
                        .placeholder(R.drawable.ic_user_1)
                        .into(image_profile);

            if (str_designation!= null)
                text_profile_information.setText(str_designation + " - "+str_level);

        }

    }

    /********************************
     *FUNCTION LOGOUT
     *********************************/
    private void Function_Logout() {

        System.out.println("### AppConfig.URL_LOGOUT " + AppConfig.URL_LOGOUT);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGOUT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LOGOUT : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 200) {

                        TastyToast.makeText(getApplicationContext(), "Logged Out Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        session_manager.logoutUser();
                        dialog.dismiss();

                    } else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Oops! Something Went Wrong, Try Again Later.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.URL_LOGOUT onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGOUT onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Main.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_TYPE, str_user_type);
                params.put(TAG_USER_ROLE, str_user_role);
                params.put(TAG_USER_DEPARTMENT_ID, str_user_department_id);

                System.out.println("### " + TAG_USER_ID + " : " + str_user_id);
                System.out.println("### " + TAG_USER_TYPE + " : " + str_user_type);
                System.out.println("### " + TAG_USER_ROLE + " : " + str_user_role);
                System.out.println("### " + TAG_USER_DEPARTMENT_ID + " : " + str_user_department_id);


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

    @Override
    public void onBackPressed() {

        if (back_pressed + 2000 > System.currentTimeMillis()) {

            this.moveTaskToBack(true);
        } else {

            Toast.makeText(getBaseContext(), "Press Once Again To Exit.", Toast.LENGTH_SHORT).show();

        }

        back_pressed = System.currentTimeMillis();

    }

}