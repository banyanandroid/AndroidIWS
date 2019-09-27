package com.banyan.androidiws.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Leave_Request_List;
import com.banyan.androidiws.adapter.Adapter_Notification_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Notification_List extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    public static final String TAG_NOTIFICATION_ID = "notification_id";
    public static final String TAG_TITLE = "title";
    public static final String TAG_DESCRIPTION = "description";
    public static final String TAG_TYPE = "type";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipe_refresh;

    private ListView list_view;

    private TextView text_view_message;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private ArrayList<HashMap<String, String>> arrayList_notification;

    private String str_user_id = "", str_user_name = "", str_user_type = "", str_user_role = "", str_notification_id = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();

        Function_Verify_Network_Available(this);

        arrayList_notification = new ArrayList<>();

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
        toolbar.setTitle("Notifications");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Notification_List.this, Activity_Main.class);
                startActivity(intent);
            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        list_view = (ListView) findViewById(R.id.list_view);
        text_view_message = (TextView) findViewById(R.id.text_view_message);

        list_view.setEmptyView(text_view_message);

        swipe_refresh.setOnRefreshListener(this);

        /*************************
         *  GET DATA
         *************************/
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                queue = Volley.newRequestQueue( Activity_Notification_List.this);
                Function_Notification_List();

            }
        });

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> notification = arrayList_notification.get(position);

                String str_category = notification.get(TAG_TYPE);

                if (str_category.equals("1")){

                    Intent intent = new Intent(Activity_Notification_List.this, Activity_Account_Details.class);
                    startActivity(intent);
                }else if (str_category.equals("2")){

                    Intent intent = new Intent(Activity_Notification_List.this, Activity_Add_Leave.class);
                    startActivity(intent);

                }

            }
        });

       /* list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String, String> notification = arrayList_notification.get(position);

                str_notification_id = notification.get(TAG_NOTIFICATION_ID);

                Alert_Delete_Notification();

                return true;
            }
        });*/

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

                        for (int count = 0; count < array_request.length(); count++) {
                            JSONObject obj_balance = array_request.getJSONObject(count);

                            String str_title = obj_balance.getString(TAG_TITLE);
                            String str_description = obj_balance.getString(TAG_DESCRIPTION);
                            String str_type = obj_balance.getString(TAG_TYPE);

                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_TITLE, str_title);
                            map.put(TAG_DESCRIPTION, str_description);
                            map.put(TAG_TYPE, str_type);

                            arrayList_notification.add(map);

                        }


                        swipe_refresh.setRefreshing(false);


                    } else if (status == 400) {

                        arrayList_notification.clear();


                        swipe_refresh.setRefreshing(false);

                        TastyToast.makeText( Activity_Notification_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_notification.clear();

                        swipe_refresh.setRefreshing(false);

                        TastyToast.makeText( Activity_Notification_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Notification_List adapter_notification_list = new Adapter_Notification_List( Activity_Notification_List.this, arrayList_notification);
                    list_view.setAdapter(adapter_notification_list);

                    swipe_refresh.setRefreshing(false);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### JSONException "+e.getLocalizedMessage());
                }

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_NOTIFICATION_LIST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_NOTIFICATION_LIST onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Notification_List.this);


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", str_user_id);

                System.out.println("###" + "user_id" + " : " + str_user_id);

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
            if (!utility.IsNetworkAvailable(this)) {
                utility.Function_Show_Not_Network_Message(this);
            }

        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }

    public void Alert_Delete_Notification(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Notification")
                .setMessage("Do you want to Delete Notification")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {

                        dialog = new SpotsDialog(Activity_Notification_List.this);
                        dialog.show();

                        queue = Volley.newRequestQueue(Activity_Notification_List.this);
                        Function_Delete_Notification();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

    /********************************
     *FUNCTION DELETE NOTIFICATION
     *********************************/
    private void Function_Delete_Notification() {

        System.out.println("### AppConfig.URL_DELETE_NOTIFICATION " + AppConfig.URL_DELETE_NOTIFICATION);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_NOTIFICATION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_DELETE_NOTIFICATION : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200) {

                        dialog.dismiss();

                        TastyToast.makeText( Activity_Notification_List.this, "Notification Deleted Successfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                        onRefresh();

                    } else if (status == 400) {

                        arrayList_notification.clear();


                        dialog.dismiss();

                        TastyToast.makeText( Activity_Notification_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        arrayList_notification.clear();

                        dialog.dismiss();

                        TastyToast.makeText( Activity_Notification_List.this, msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List( Activity_Notification_List.this, arrayList_notification);
                    list_view.setAdapter(adapter_leave_request_list);

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

                System.out.println("### AppConfig.URL_DELETE_NOTIFICATION onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_DELETE_NOTIFICATION onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Notification_List.this);


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


    @Override
    public void onRefresh() {

        swipe_refresh.setRefreshing(true);

        arrayList_notification.clear();
        Adapter_Leave_Request_List adapter_leave_request_list = new Adapter_Leave_Request_List( Activity_Notification_List.this, arrayList_notification);
        list_view.setAdapter(adapter_leave_request_list);

        queue = Volley.newRequestQueue( Activity_Notification_List.this);
        Function_Notification_List();

    }
}
