package com.banyan.androidiws.fragment;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Util;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.Context.LOCATION_SERVICE;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class Fragment_Attendance extends Fragment {

    public static final String TAG_USER_ID = "userid";
    public static final String TAG_USER_TYPE = "usertype";

    public static final String TAG_ATTEDANCE_DEPARTMENT_ID = "department_id";
    public static final String TAG_ATTEDANCE_LATITUDE = "latitude";
    public static final String TAG_ATTEDANCE_LONGITUDE = "longitude";
    public static final String TAG_ATTEDANCE_ADDRESS = "address";

    public static final String TAG_ATTEDANCE_STATUS_USER_ID = "user_id";
    public static final String TAG_ATTEDANCE_STATUS_DATE = "date";

    private Button button_present;
    private TextView text_date, text_time, text_attendance_status, text_attendance_remarks;
    private SpotsDialog dialog;
    private RequestQueue queue;
    private LinearLayout layout_attendance_status, layout_present;

    private Session_Manager session;
    private String str_user_id = "", str_user_name = "", str_user_type_id = "", str_user_department_id = "", str_lat = "", str_lon = "", address = "",
            str_current_date = "";

    // 6.0 Location & Call
    static final Integer GPS_SETTINGS = 0x7;
    static final Integer LOCATION = 0x1;
    static final Integer CALL = 0x2;

    private LocationRequest mLocationRequest;

    private Util utility;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    Double latitude, longitude;

    private boolean bol_update_location = false;
    private String str_provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_attendance, container, false);

        /*********************************
         * SETUP
         **********************************/
        utility = new Util();

        System.out.println("### onCreateView ");
        // check is network available or not
        Function_Verify_Network_Available(getActivity());

        //get location

       /* LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);

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

        startLocationUpdates();*/

        // get session details
        session = new Session_Manager(getActivity());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type_id = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_department_id = user.get(Session_Manager.KEY_USER_DEPARTMENT_ID);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_type_id " + str_user_type_id);
        System.out.println("### str_user_department_id " + str_user_department_id);

        text_date = (TextView)root_view.findViewById(R.id.text_date);
        text_time = (TextView)root_view.findViewById(R.id.text_time);
        button_present = (Button)root_view.findViewById(R.id.button_present);
        layout_attendance_status = (LinearLayout)root_view.findViewById(R.id.layout_attendance_status);
        text_attendance_status = (TextView)root_view.findViewById(R.id.text_attendance_status);
        text_attendance_remarks = (TextView)root_view.findViewById(R.id.text_attendance_remarks);
        layout_present = (LinearLayout)root_view.findViewById(R.id.layout_present);



        //get data
        Date currentTime = Calendar.getInstance().getTime();
        String format_date = new SimpleDateFormat("d, MMM, yyyy").format(currentTime.getTime());
        String format_time = new SimpleDateFormat("hh:mm a").format(currentTime.getTime());

        text_date.setText(format_date);
        text_time.setText(format_time);

        //get data


      /*  try{

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            str_current_date = df.format(c);

            dialog = new SpotsDialog(getContext());
            dialog.show();

            queue = Volley.newRequestQueue(getContext());
            Function_Attendance_Status();

        }catch (Exception e){
            System.out.println("### Exception "+e.getLocalizedMessage());
        }*/

        button_present.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                text_attendance_status.setText("Present");
//                        text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_green));

                text_attendance_remarks.setText("-");
                //hide
                layout_present.setVisibility(View.GONE);
                //show
                layout_attendance_status.setVisibility(View.VISIBLE);

                TastyToast.makeText(getActivity(), "Attendance Registered Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


           /*     if(str_lat.isEmpty() && str_lon.isEmpty()){


                    Function_Get_Location();

                }else{

                    dialog = new SpotsDialog(getActivity());
                    dialog.show();

                    queue = Volley.newRequestQueue(getActivity());
                    Function_Attendance();

                }*/




            }
        });

        return root_view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        System.out.println("### startLocationUpdates ");
        askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
            // to handle the case where the user grants the permission. See the documentation
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
        System.out.println("### onLocationChanged");
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        str_lat = String.valueOf(latitude);
        str_lon = String.valueOf(longitude);

        // You can now create a LatLng Object for use with maps
        System.out.println("### msg "+msg);
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        Context context = getActivity();
        if (latitude != null && longitude!= null && context!= null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                System.out.println("### address " + address);
                System.out.println("### city " + city);
                System.out.println("### state " + state);
                System.out.println("### country " + country);
                System.out.println("### postalCode " + postalCode);
                System.out.println("### knownName " + knownName);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("### IOException "+e.getLocalizedMessage());
            }

        }

        if (!str_lat.isEmpty() && !str_lon.isEmpty() && bol_update_location){

            bol_update_location = false;

            queue = Volley.newRequestQueue(getActivity());
            Function_Attendance();

        }

    }


    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Attendance() {

        System.out.println("### AppConfig.URL_ATTENDANCE " + AppConfig.URL_ATTENDANCE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        text_attendance_status.setText("Present");
//                        text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_green));

                        text_attendance_remarks.setText("-");
                        //hide
                        layout_present.setVisibility(View.GONE);
                        //show
                        layout_attendance_status.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText(getActivity(), msg, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


                    }else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getActivity(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getActivity(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
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

                System.out.println("### AppConfig.URL_ATTENDANCE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_ATTENDANCE onErrorResponse " + error.getLocalizedMessage());

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage("Something Went Wrong, Try Again Later.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getActivity().finishAffinity();

                            }
                        }).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_TYPE, str_user_type_id);
                params.put(TAG_ATTEDANCE_DEPARTMENT_ID, str_user_department_id);
                params.put(TAG_ATTEDANCE_LATITUDE, str_lat);
                params.put(TAG_ATTEDANCE_LONGITUDE, str_lon);
                params.put(TAG_ATTEDANCE_ADDRESS, address);


                System.out.println("###" + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_USER_TYPE + " : " + str_user_type_id);
                System.out.println("###" + TAG_ATTEDANCE_DEPARTMENT_ID + " : " + str_user_department_id);
                System.out.println("###" + TAG_ATTEDANCE_LATITUDE + " : " + str_lat);
                System.out.println("###" + TAG_ATTEDANCE_LONGITUDE + " : " + str_lon);
                System.out.println("###" + TAG_ATTEDANCE_ADDRESS + " : " + address);

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
     *FUNCTION ATTENDANCE STATUS
     *********************************/
    private void Function_Attendance_Status() {

        System.out.println("### AppConfig.URL_ATTENDANCE_STATUS " + AppConfig.URL_ATTENDANCE_STATUS);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_ATTENDANCE_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_ATTENDANCE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONObject obj_record = obj.getJSONObject("records");
                        String str_attendance_status = obj_record.getString("Attendance_status");

                        if (str_attendance_status.equals("Registered")){

                            String str_status = obj_record.getString("status");
                            String str_status_id = obj_record.getString("status_id");
                            String str_remarks = obj_record.getString("remarks");

                            text_attendance_status.setText(str_status);

                            /*text_attendance_status.setTextColor(Color.WHITE);
                            if (str_status_id.equals("1")){ // present
                                text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_green));
                            }else if (str_status_id.equals("2")){ // bench
                                text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_orange));

                            }else if (str_status_id.equals("3")){ //training
                                text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_purple));

                            }else if (str_status_id.equals("4")){ // leave
                                text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_red));

                            }else if (str_status_id.equals("5")){ // lop
                                text_attendance_status.setBackground(getResources().getDrawable(R.drawable.bg_button_grey));
                            }*/

                            if (str_remarks.isEmpty())
                                str_remarks = "-";
                            text_attendance_remarks.setText(str_remarks);
                            //hide
                            layout_present.setVisibility(View.GONE);
                            //show
                            layout_attendance_status.setVisibility(View.VISIBLE);

                        }else{

                            //hide
                            layout_present.setVisibility(View.VISIBLE);
                            //show
                            layout_attendance_status.setVisibility(View.GONE);

                        }


                        dialog.dismiss();

                    }else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getActivity(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getActivity(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);
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

                System.out.println("### AppConfig.URL_ATTENDANCE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_ATTENDANCE onErrorResponse " + error.getLocalizedMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_ATTEDANCE_STATUS_USER_ID, str_user_id);
                params.put(TAG_ATTEDANCE_STATUS_DATE, str_current_date);

                System.out.println("### " + TAG_ATTEDANCE_STATUS_USER_ID + " : " + str_user_id);
                System.out.println("### " + TAG_ATTEDANCE_STATUS_DATE + " : " + str_current_date);

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
            if (!utility.IsNetworkAvailable(getActivity())){
                utility.Function_Show_Not_Network_Message(getActivity());
            };
        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }




    /*********************************
     * For Loaction
     ********************************/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {

                //This is called if user has denied the permission before
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
            Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    /********************************
     * Check GPS Connection is Enabled
     *********************************/

    private void showGPSDisabledAlertToUser() {

        System.out.println("### showGPSDisabledAlertToUser");

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                        getActivity().finish();
                        getActivity().finishAffinity();
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

        str_provider = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!str_provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            getActivity().getApplicationContext().sendBroadcast(poke);

            System.out.println("Inside GPS 1");
        }
    }

    private void Function_Get_Location(){

        dialog = new SpotsDialog(getActivity());
        dialog.setCancelable(false);
        dialog.show();

        System.out.println("### Function_Get_Location");

        System.out.println("### inside post run");
        LocationManager locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(LOCATION_SERVICE);

        try {
            System.out.println("### Called");
            turnGPSOn();
            System.out.println("Done");
        } catch (Exception e) {

        }

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        } else {
            showGPSDisabledAlertToUser();
        }

        if (str_provider.contains("gps")) { // get location only if gps is enabled
            bol_update_location = true;
            startLocationUpdates();
        }else{
            dialog.dismiss();
        }

    }
}
