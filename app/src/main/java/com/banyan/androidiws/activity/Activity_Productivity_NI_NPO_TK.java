package com.banyan.androidiws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Productivity_NI_NPO_TK_List;
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

public class Activity_Productivity_NI_NPO_TK extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    public static  final  String TAG_MONTH = "month";
    public static  final  String TAG_NI_WIP = "niwip";
    public static  final  String TAG_NI_COMPLETED = "nisc";
    public static  final  String TAG_NPO_WIP = "npowip";
    public static  final  String TAG_NPO_COMPLETED = "nposc";
    public static  final  String TAG_TOTAL_WIP = "totwip";
    public static  final  String TAG_TOTAL_COMPLETED = "totsc";
    public static  final  String TAG_PRODUCTIVIY = "productivity";
    public static  final  String TAG_RATING = "rating";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipe_refresh;

    private AppCompatTextView textview_message;

    private ListView list_view;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private ArrayList<HashMap<String, String>> arrayList_productivity;

    private String str_user_id, str_user_name, str_user_type, str_user_role;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity_ni_npo_tk);

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
        toolbar.setTitle("Productivity");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Productivity_NI_NPO_TK.this, Activity_Main.class);
                startActivity(intent);
            }
        });
        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        list_view = (ListView) findViewById(R.id.list_view);
        textview_message = (AppCompatTextView) findViewById(R.id.textview_message);
        list_view.setEmptyView(textview_message);

        swipe_refresh.setOnRefreshListener(this);

        /*************************
         *  SETUP
         *************************/
        arrayList_productivity = new ArrayList<>();

        /*************************
         *  GET DATA
         *************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                arrayList_productivity.clear();

                queue = Volley.newRequestQueue(Activity_Productivity_NI_NPO_TK.this);
                Function_Get_Productivity();

            }
        });





    }

    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(Activity_Productivity_NI_NPO_TK.this)){
                utility.Function_Show_Not_Network_Message(Activity_Productivity_NI_NPO_TK.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }


    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Get_Productivity() {

        System.out.println("### AppConfig.URL_PRODUCTIVITY_NI_NPO_TK " + AppConfig.URL_PRODUCTIVITY_NI_NPO_TK);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PRODUCTIVITY_NI_NPO_TK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PRODUCTIVITY_NI_NPO_TK : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONArray array_one = obj.getJSONArray("records");

                        for (int count =0; count < array_one.length(); count++){

                            JSONObject obj_one = (JSONObject) array_one.get(count);

                            String str_month = obj_one.getString(TAG_MONTH);
                            String str_ni_wip = obj_one.getString(TAG_NI_WIP);
                            String str_ni_completed = obj_one.getString(TAG_NI_COMPLETED);
                            String str_npo_wip = obj_one.getString(TAG_NPO_WIP);
                            String str_npo_completed = obj_one.getString(TAG_NPO_COMPLETED);
                            String str_total_wip = obj_one.getString(TAG_TOTAL_WIP);
                            String str_total_completed = obj_one.getString(TAG_TOTAL_COMPLETED);
                            String str_productivity = obj_one.getString(TAG_PRODUCTIVIY);
                            String str_rating = obj_one.getString(TAG_RATING);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_MONTH, str_month);
                            item.put(TAG_NI_WIP, str_ni_wip);
                            item.put(TAG_NI_COMPLETED, str_ni_completed);
                            item.put(TAG_NPO_WIP, str_npo_wip);
                            item.put(TAG_NPO_COMPLETED, str_npo_completed);
                            item.put(TAG_TOTAL_WIP, str_total_wip);
                            item.put(TAG_TOTAL_COMPLETED, str_total_completed);
                            item.put(TAG_PRODUCTIVIY, str_productivity);
                            item.put(TAG_RATING, str_rating);

                            arrayList_productivity.add(item);

                        }



                    } else if (status == 400) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Productivity_NI_NPO_TK_List adapter_productivity_ni_npo_tk_list = new Adapter_Productivity_NI_NPO_TK_List(Activity_Productivity_NI_NPO_TK.this, arrayList_productivity, str_user_id);
                    list_view.setAdapter(adapter_productivity_ni_npo_tk_list);

                    swipe_refresh.setRefreshing(false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }

                swipe_refresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                swipe_refresh.setRefreshing(false);

                System.out.println("### AppConfig.URL_PRODUCTIVITY_NI_NPO_TK onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PRODUCTIVITY_NI_NPO_TK onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Productivity_NI_NPO_TK.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_PRODUCTIVITY_NI_NPO_TK " + "userid" + " : " + str_user_id);

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

        arrayList_productivity.clear();

        queue = Volley.newRequestQueue(Activity_Productivity_NI_NPO_TK.this);
        Function_Get_Productivity();

    }
}
