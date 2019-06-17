package com.banyan.androidiws.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Leave_Details;
import com.banyan.androidiws.adapter.Adapter_Leave_Request_List;
import com.banyan.androidiws.adapter.Adapter_Leave_balance_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.ItemOffSetDecorator;
import com.banyan.androidiws.global.Session_Manager;
import com.sdsmdg.tastytoast.TastyToast;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static com.banyan.androidiws.global.Util.IsNetworkAvailable;


public class Fragment_Leave_List extends Fragment {

    public static final String TAG_LEAVE_BALANCE_TITLE = "leavename";
    public static final String TAG_LEAVE_BALANCE_BALANCE = "balance_leave";
    public static final String TAG_LEAVE_BALANCE_TOTAL = "total_leavedays";

    public static final String TAG_LEAVE_REQUEST_SUBJECT = "subject";
    public static final String TAG_LEAVE_REQUEST_REASON = "reason";
    public static final String TAG_LEAVE_REQUEST_TYPE_NAME = "leavename";
    public static final String TAG_LEAVE_REQUEST_TYPE_ID = "leave_id";
    public static final String TAG_LEAVE_REQUEST_STATUS = "leave_status";
    public static final String TAG_LEAVE_START_DATE = "start_date";
    public static final String TAG_LEAVE_END_DATE = "end_date";
    public static final String TAG_LEAVE_REQUEST_TOTAL_DAYS = "days_count";

    public static final String TAG_USER_ID = "user_id";
    public static final String TAG_FILTER_STATUS = "status";

    private TextView text_filter;
    LinearLayout filter;
    private ListView list_view_leave_request;
    private RecyclerView recycler_view_leave_balance;
    private SpotsDialog dialog;
    private RequestQueue queue;
    private Session_Manager session;

    private String str_user_id = "", str_user_name = "";
    private int int_selected_filter_id = 0;

    ArrayList<HashMap<String, String>> arrayList_leave_details, arrayList_leave_request;
    private SwipeRefreshLayout swipe_refresh_list, swipe_refresh_message;
    private TextView text_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_leave_details, container, false);

        Function_Verify_Network_Available(getActivity());

        session = new Session_Manager(getActivity());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);

        recycler_view_leave_balance = (RecyclerView) root_view.findViewById(R.id.recycler_view_leave_balance);
        filter =(LinearLayout) root_view.findViewById(R.id.filter);
        text_filter = (TextView) root_view.findViewById(R.id.text_filter);
        list_view_leave_request = (ListView) root_view.findViewById(R.id.list_view_leave_request);
        text_message = (TextView) root_view.findViewById(R.id.text_message);

        //setup

        arrayList_leave_details = new ArrayList<>();
        arrayList_leave_request = new ArrayList<>();

        //get data

        HashMap<String, String> map = new HashMap<>();
        map.put(TAG_LEAVE_BALANCE_TITLE, "Medical Leave");
        map.put(TAG_LEAVE_BALANCE_BALANCE, "5");
        map.put(TAG_LEAVE_BALANCE_TOTAL, "10");

        arrayList_leave_details.add(map);

        HashMap<String, String> map2 = new HashMap<>();
        map2.put(TAG_LEAVE_BALANCE_TITLE, "Casual Leave");
        map2.put(TAG_LEAVE_BALANCE_BALANCE, "5");
        map2.put(TAG_LEAVE_BALANCE_TOTAL, "10");

        arrayList_leave_details.add(map2);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler_view_leave_balance.setLayoutManager(layoutManager);
        ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(getActivity(), R.dimen.dimen_horizontal);
        recycler_view_leave_balance.addItemDecoration(itemDecoration);

        Adapter_Leave_balance_List adapter_leave_details_list = new Adapter_Leave_balance_List(getActivity(), arrayList_leave_details);
        recycler_view_leave_balance.setAdapter(adapter_leave_details_list);

        HashMap<String, String> map_leave = new HashMap<>();
        map_leave.put(TAG_LEAVE_START_DATE, "31-05-2019");
        map_leave.put(TAG_LEAVE_END_DATE, "3-05-2019");
        map_leave.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, "1");
        map_leave.put(TAG_LEAVE_REQUEST_SUBJECT, "Sick Leave");
        map_leave.put(TAG_LEAVE_REQUEST_REASON, "Sick Leave");
        map_leave.put(TAG_LEAVE_REQUEST_TYPE_NAME, "Medical Lave");
        map_leave.put(TAG_LEAVE_REQUEST_TYPE_ID, "1");
        map_leave.put(TAG_LEAVE_REQUEST_STATUS, "Approved");

        arrayList_leave_request.add(map_leave);

        HashMap<String, String> map_leave2 = new HashMap<>();

        map_leave2.put(TAG_LEAVE_START_DATE, "28-05-2019");
        map_leave2.put(TAG_LEAVE_END_DATE, "29-05-2019");
        map_leave2.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, "2");
        map_leave2.put(TAG_LEAVE_REQUEST_SUBJECT, "Casual Leave");
        map_leave2.put(TAG_LEAVE_REQUEST_REASON, "To Visit Native");
        map_leave2.put(TAG_LEAVE_REQUEST_TYPE_NAME, "Casual Leave");
        map_leave2.put(TAG_LEAVE_REQUEST_TYPE_ID, "1");
        map_leave2.put(TAG_LEAVE_REQUEST_STATUS, "Approved");

        arrayList_leave_request.add(map_leave2);


        Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List(getActivity(), arrayList_leave_request);
        list_view_leave_request.setAdapter(adapter_leave_request_list);


      /*  try {

            dialog = new SpotsDialog(getActivity());
            dialog.show();

            queue = Volley.newRequestQueue(getActivity());
            Function_Leave_Balance_Request();

        } catch (Exception e) {
            System.out.println("### Exception " + e.getLocalizedMessage());
        }*/



        // set data



        // action

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Alert_Leave_Filter();

            }
        });


        list_view_leave_request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                
                HashMap<String, String> map = arrayList_leave_request.get(position);
                String str_request_from_date = map.get(TAG_LEAVE_START_DATE);
                String str_request_end_date = map.get(TAG_LEAVE_END_DATE);
                String str_request_total_days = map.get(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                String str_request_subject = map.get(TAG_LEAVE_REQUEST_SUBJECT);
                String str_request_reason = map.get(TAG_LEAVE_REQUEST_REASON);
                String str_request_type = map.get(TAG_LEAVE_REQUEST_TYPE_NAME);
                String str_request_type_id = map.get(TAG_LEAVE_REQUEST_TYPE_ID);
                String str_request_status = map.get(TAG_LEAVE_REQUEST_STATUS);

                System.out.println("### setOnItemClickListener str_request_type "+str_request_type);

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Activity_Leave_Details.TAG_LEAVE_FROM_DATE, str_request_from_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_TO_DATE, str_request_end_date);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_NO_OF_DAYS, str_request_total_days);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_SUBJECT, str_request_subject);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_APPLY_REASON, str_request_reason);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_LEAVE_TYPE, str_request_type);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_LEAVE_TYPE_ID, str_request_type_id);
                editor.putString(Activity_Leave_Details.TAG_LEAVE_STATUS, str_request_status);
                editor.commit();

                Intent intent = new Intent(getActivity(), Activity_Leave_Details.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);

            }
        });
        return root_view;
    }

    /********************************
     *FUNCTION LEAVE BALANCE
     *********************************/
    private void Function_Leave_Balance_Request() {

        System.out.println("### AppConfig.URL_LEAVE_BALANCE " + AppConfig.URL_LEAVE_BALANCE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_BALANCE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_BALANCE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 200) {

                        JSONArray array_balance = obj.getJSONArray("leave_count");

                        for (int count = 0; count < array_balance.length(); count++) {
                            JSONObject obj_balance = array_balance.getJSONObject(count);

                            String str_balance_title = obj_balance.getString(TAG_LEAVE_BALANCE_TITLE);
                            String str_balance_balance = obj_balance.getString(TAG_LEAVE_BALANCE_BALANCE);
                            String str_balance_total = obj_balance.getString(TAG_LEAVE_BALANCE_TOTAL);

                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_BALANCE_TITLE, str_balance_title);
                            map.put(TAG_LEAVE_BALANCE_BALANCE, str_balance_balance);
                            map.put(TAG_LEAVE_BALANCE_TOTAL, str_balance_total);

                            arrayList_leave_details.add(map);

                        }


                        JSONArray array_request = obj.getJSONArray("records");

                        for (int count = 0; count < array_request.length(); count++) {
                            JSONObject obj_balance = array_request.getJSONObject(count);

                            String str_start_date = obj_balance.getString(TAG_LEAVE_START_DATE);
                            String str_end_date = obj_balance.getString(TAG_LEAVE_END_DATE);
                            String str_total_day = obj_balance.getString(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                            String str_subject = obj_balance.getString(TAG_LEAVE_REQUEST_SUBJECT);
                            String str_reason = obj_balance.getString(TAG_LEAVE_REQUEST_REASON);
                            String str_request_type = obj_balance.getString(TAG_LEAVE_REQUEST_TYPE_NAME);
                            String str_request_type_id = obj_balance.getString(TAG_LEAVE_REQUEST_TYPE_ID);
                            String str_status = obj_balance.getString(TAG_LEAVE_REQUEST_STATUS);

                            System.out.println("### URL_LEAVE_BALANCE str_request_type "+str_request_type);

                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_START_DATE, str_start_date);
                            map.put(TAG_LEAVE_END_DATE, str_end_date);
                            map.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, str_total_day);
                            map.put(TAG_LEAVE_REQUEST_SUBJECT, str_subject);
                            map.put(TAG_LEAVE_REQUEST_REASON, str_reason);
                            map.put(TAG_LEAVE_REQUEST_TYPE_NAME, str_request_type);
                            map.put(TAG_LEAVE_REQUEST_TYPE_ID, str_request_type_id);
                            map.put(TAG_LEAVE_REQUEST_STATUS, str_status);

                            arrayList_leave_request.add(map);

                        }

                        list_view_leave_request.setVisibility(View.VISIBLE);
                        text_message.setVisibility(View.GONE);

                        if (array_request.length() == 0){
                            list_view_leave_request.setVisibility(View.GONE);
                            text_message.setVisibility(View.VISIBLE);
                        }

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_details.clear();
                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_details.clear();
                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Oops! Something Went Wrong, Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                    recycler_view_leave_balance.setLayoutManager(layoutManager);
                    ItemOffSetDecorator itemDecoration = new ItemOffSetDecorator(getActivity(), R.dimen.dimen_horizontal);
                    recycler_view_leave_balance.addItemDecoration(itemDecoration);

                    Adapter_Leave_balance_List adapter_leave_details_list = new Adapter_Leave_balance_List(getActivity(), arrayList_leave_details);
                    recycler_view_leave_balance.setAdapter(adapter_leave_details_list);

                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List(getActivity(), arrayList_leave_request);
                    list_view_leave_request.setAdapter(adapter_leave_request_list);

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

                TastyToast.makeText(getContext(), "Something Went Wrong, Try Again Later", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                System.out.println("### AppConfig.URL_LEAVE_BALANCE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_BALANCE onErrorResponse " + error.getLocalizedMessage());

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


    public void Alert_Leave_Filter() {

        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View view_alert = layoutInflater.inflate(R.layout.alert_leave_filter, null);

        final SearchableSpinner search_spinner_leave_filter = (SearchableSpinner) view_alert.findViewById(R.id.search_spinner_leave_filter);
        search_spinner_leave_filter.setTitle("Select Leave Status");

        search_spinner_leave_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                        int_selected_filter_id = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(getActivity());
        alertdialog_builder.setCancelable(false);
        alertdialog_builder.setView(view_alert);
        alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

               /* //get data
                try {
                    arrayList_leave_request.clear();
                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List(getActivity(), arrayList_leave_request);
                    list_view_leave_request.setAdapter(adapter_leave_request_list);

                    dialog = new SpotsDialog(getActivity());
                    dialog.show();

                    queue = Volley.newRequestQueue(getActivity());
                    Function_Leave_Balance();

                } catch (Exception e) {
                    System.out.println("### Exception " + e.getLocalizedMessage());
                }*/

            }
        });
        alertdialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertdialog_builder.create();
        alertDialog.setTitle("Leave Filter");
        alertDialog.show();

    }

    /********************************
     *FUNCTION LEAVE BALANCE
     *********************************/
    private void Function_Leave_Balance() {

        System.out.println("### AppConfig.URL_LEAVE_FILTER " + AppConfig.URL_LEAVE_FILTER);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LEAVE_FILTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LEAVE_FILTER : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");

                    if (status == 200) {


                        JSONArray array_request = obj.getJSONArray("records");

                        for (int count = 0; count < array_request.length(); count++) {
                            JSONObject obj_balance = array_request.getJSONObject(count);

                            String str_start_date = obj_balance.getString(TAG_LEAVE_START_DATE);
                            String str_end_date = obj_balance.getString(TAG_LEAVE_END_DATE);
                            String str_total_day = obj_balance.getString(TAG_LEAVE_REQUEST_TOTAL_DAYS);
                            String str_subject = obj_balance.getString(TAG_LEAVE_REQUEST_SUBJECT);
                            String str_reason = obj_balance.getString(TAG_LEAVE_REQUEST_REASON);
                            String str_request_type = obj_balance.getString(TAG_LEAVE_REQUEST_TYPE_NAME);
                            String str_request_type_id = obj_balance.getString(TAG_LEAVE_REQUEST_TYPE_ID);
                            String str_status = obj_balance.getString(TAG_LEAVE_REQUEST_STATUS);

                            System.out.println("### URL_LEAVE_FILTER str_request_type "+str_request_type);

                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_LEAVE_START_DATE, str_start_date);
                            map.put(TAG_LEAVE_END_DATE, str_end_date);
                            map.put(TAG_LEAVE_REQUEST_TOTAL_DAYS, str_total_day);
                            map.put(TAG_LEAVE_REQUEST_SUBJECT, str_subject);
                            map.put(TAG_LEAVE_REQUEST_REASON, str_reason);
                            map.put(TAG_LEAVE_REQUEST_TYPE_NAME, str_request_type);
                            map.put(TAG_LEAVE_REQUEST_TYPE_ID, str_request_type_id);
                            map.put(TAG_LEAVE_REQUEST_STATUS, str_status);

                            arrayList_leave_request.add(map);

                        }

                        list_view_leave_request.setVisibility(View.VISIBLE);
                        text_message.setVisibility(View.GONE);

                        if (array_request.length() == 0){

                            list_view_leave_request.setVisibility(View.GONE);
                            text_message.setVisibility(View.VISIBLE);
                        }

                        dialog.dismiss();


                    } else if (status == 400) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_leave_request.clear();

                        list_view_leave_request.setVisibility(View.GONE);
                        text_message.setVisibility(View.VISIBLE);

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Oops! Something Went Wrong, Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List(getActivity(), arrayList_leave_request);
                    list_view_leave_request.setAdapter(adapter_leave_request_list);

                    dialog.dismiss();
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

                System.out.println("### AppConfig.URL_LEAVE_FILTER onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LEAVE_FILTER onErrorResponse " + error.getLocalizedMessage());

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
                params.put(TAG_FILTER_STATUS, ""+int_selected_filter_id);

                System.out.println("###" + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_FILTER_STATUS + " : " + int_selected_filter_id);

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

    public void Function_Verify_Network_Available(Context context) {
        try {
            if (!IsNetworkAvailable(context)) {

                new AlertDialog.Builder(context)
                        .setTitle("No Internet Connection")
                        .setMessage("Internet Connection is Not Available.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getActivity().finishAffinity();

                            }
                        }).show();
            }
            ;
        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }

}
