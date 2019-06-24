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
import com.banyan.androidiws.R;
import com.banyan.androidiws.fragment.Fragment_Attendance;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Util;
import com.google.android.material.navigation.NavigationView;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;


public class Activity_Main_Not_Used extends AppCompatActivity {

    private static final String TAG_ATTENDANCE = "ATTENDANCE";
    private static final String TAG_ATTENDANCE_REPORT = "ATTENDANCE_REPORT";
    private static final String TAG_LEAVE = "APPLY LEAVE";
    private static final String TAG_MYPROFILE = "MY PROFILE";
    private static final String TAG_ABOUTS = "ABOUTS";
    private static final String TAG_LOGOUT = "LOGOUT";

    private static final String TAG_USER_ID = "userid";
    private static final String TAG_USER_TYPE = "usertype";
    private static final String TAG_USER_ROLE = "userrole";
    private static final String TAG_USER_DEPARTMENT_ID = "departmentid";

    private static final String TAG = Activity_Main_Not_Used.class.getSimpleName();

    public static final String TAG_ACTIVITY_ATTEDANCE_REPORT = "Activity_Attendance_Report_For_Months";
    public static final String TAG_ACTIVITY_LEAVE_REQUEST = "Activity_Leave_List";
    public static final String TAG_CALLING_ACTIVITY = "calling_activity";

    // 6.0 Location & Call
    private final Integer LOCATION = 0x1;
    private final Integer CALL = 0x2;
    private final Integer GPS_SETTINGS = 0x7;

    private NavigationView navigationView;

    private DrawerLayout drawer;

    private View navHeader;

    private CircleImageView image_profile;

    private TextView txt_name, text_profile_information;

    private Toolbar toolbar;

    private TimerTask timerTask;

    private final Handler handler = new Handler();

    // Session manager
    private Session_Manager session;

    private Session_Manager session_manager;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Util utility;

    private  long back_pressed;

    public  int navItemIndex = 0;

    public  String TAG_CURRENT = TAG_ATTENDANCE;

    private String[] activityTitles;

    private boolean shouldLoadHomeFragOnBackPress = true;

    private String str_user_id = "", str_user_name = "", str_user_name_first_last ="", str_user_role = "", str_user_image = "", str_user_type = "", str_user_department_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        /*********************************
         * SETUP
         **********************************/
        utility = new Util();

        /*************************
        *  SESSION
        **************************/
        Function_Verify_Network_Available(this);

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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navHeader = navigationView.getHeaderView(0);
        txt_name = (TextView) navHeader.findViewById(R.id.profile_name);
        image_profile = (CircleImageView) navHeader.findViewById(R.id.img_profile);
        text_profile_information = (TextView) navHeader.findViewById(R.id.profile_information);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);



        // load nav menu header data
        loadNavHeader();

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

        /*********************************
         * ACTION
         **********************************/

        if (savedInstanceState == null) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String back_option = preferences.getString(TAG_CALLING_ACTIVITY, "");
            if (back_option.isEmpty()){ // load default fragment

                navItemIndex = 0;
                TAG_CURRENT = TAG_ATTENDANCE;
                loadHomeFragment();

            }else if (back_option.equals(TAG_ACTIVITY_ATTEDANCE_REPORT)) {

                navItemIndex = 1;
                TAG_CURRENT = TAG_ATTENDANCE_REPORT;
                loadHomeFragment();

            }else if (back_option.equals(TAG_ACTIVITY_LEAVE_REQUEST)) {

                navItemIndex = 2;
                TAG_CURRENT = TAG_LEAVE;
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

        if (TAG_CURRENT.equals(TAG_LEAVE)){
            getMenuInflater().inflate(R.menu.menu_apply_leave, menu);
            return true;
        }else if (TAG_CURRENT.equals(TAG_ATTENDANCE_REPORT)){
            getMenuInflater().inflate(R.menu.menu_attendance_report, menu);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add_leave){

            Intent intent = new Intent(Activity_Main_Not_Used.this, Activity_Add_Leave.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            return true;
        }else if (item.getItemId() == R.id.action_attendance_report_for_days){

            Intent intent = new Intent(Activity_Main_Not_Used.this, Activity_Attendance_Report_For_Dates.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            return true;
        }
        return false;
    }


    private void loadNavHeader() {
        // name, website
        if (str_user_name_first_last != null)
            txt_name.setText(str_user_name_first_last);

        // Loading profile image
        if (str_user_image != null && !str_user_image.isEmpty())
            Picasso.with(Activity_Main_Not_Used.this)
                .load( str_user_image )
                .placeholder(R.drawable.user)
                .into(image_profile);

        if (str_user_role!= null)
        text_profile_information.setText(str_user_role);

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
         /*   case 0:
                Fragment_Attendance fragment_attendance = new Fragment_Attendance();
                return fragment_attendance;

            case 1:
                Activity_Attendance_Report_For_Months activity_attendance_report_for_month = new Activity_Attendance_Report_For_Months();
                return activity_attendance_report_for_month;

            case 2:
                Activity_Leave_List fragment_leave_requests = new Activity_Leave_List();
                return fragment_leave_requests;

            case 3:
                getSupportActionBar().setTitle(str_user_name_first_last);
                Activity_Account_Details fragment_profile = new Activity_Account_Details();
                return fragment_profile;

            case 4:
                Activity_About activity_about = new Activity_About();
                return activity_about;

            case 5:
                session_manager.logoutUser();
                Toast.makeText(Activity_Main_Not_Used.this, "Logout Sucessfully.", Toast.LENGTH_SHORT).show();
                return new Fragment_Attendance();
*/
            default:
                return new Fragment_Attendance();
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
                    case R.id.nav_attendance:
                        navItemIndex = 0;
                        TAG_CURRENT = TAG_ATTENDANCE;
                        System.out.println("### onNavigationItemSelected  TAG_ATTENDANCE ");
                        break;

                    case R.id.nav_attendance_report:
                        navItemIndex = 1;
                        TAG_CURRENT = TAG_ATTENDANCE_REPORT;
                        System.out.println("### onNavigationItemSelected  TAG_ATTENDANCE ");
                        break;

                    case R.id.nav_leave:
                        navItemIndex = 2;
                        TAG_CURRENT = TAG_LEAVE;
                        System.out.println("### onNavigationItemSelected  TAG_LEAVE ");
                        break;

                    case R.id.nav_myprofile:
                        navItemIndex = 3;
                        TAG_CURRENT = TAG_MYPROFILE;
                        break;
                    case R.id.nav_about_us:
                        navItemIndex = 4;
                        TAG_CURRENT = TAG_ABOUTS;
                        break;
                    case R.id.nav_logout:
                        navItemIndex = 0;

                        TastyToast.makeText(getApplicationContext(), "Logged Out Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                        session_manager.logoutUser();


                       /* try{

                            dialog = new SpotsDialog(Activity_Main_Not_Used.this);
                            dialog.show();
                            queue = Volley.newRequestQueue(Activity_Main_Not_Used.this);
                            Function_Logout();

                        }catch (Exception e){
                            System.out.println("### Exception "+e.getLocalizedMessage());
                        }*/

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