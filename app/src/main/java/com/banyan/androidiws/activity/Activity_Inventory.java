package com.banyan.androidiws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Inventory_List;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.NestedListview;
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

public class Activity_Inventory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_INVENTORY_TYPE = "inventory_type";
    public static final String TAG_INVENTORY_CONFIGURATION = "inventory_configuration";
    public static final String TAG_INVENTORY_MODEL = "inventory_model";
    public static final String TAG_INVENTORY_SNO = "sno";

    private Toolbar toolbar;

    private SwipeRefreshLayout swipe_refresh;

    private TextView text_view_message_computer, text_view_message_accessories,
            text_view_message_safty_kit, text_view_message_ti_tools, text_view_message_ue, text_view_message_special_kit,
            text_view_message_cue;

    private LinearLayoutCompat layout_computer, layout_accessories, layout_safety_kit, layout_ti_tools, layout_ue,
            layout_special_kits, layout_cue;

    private NestedListview nested_list_view_computer,
            nested_list_view_accessories, nested_list_view_safety_kit,
            nested_list_view_ti_tools, nested_list_view_ue,
            nested_list_view_special_kit, nested_list_view_cue;

    private AppCompatButton button_confirm;

    private SpotsDialog dialog;

    private CardView card_view_info;

    private ArrayList<HashMap<String, String>> array_list_computer,
            arraylist_accessories, arraylist_safty_kit, arraylist_ti_tools,
            arraylist_ue, arraylist_special_kit, arraylist_cue ;

    private RequestQueue queue;

    private Utility utility;

    private Session_Manager session;

    private String str_user_id, str_user_name, str_user_type, str_user_role;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);


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


        /*****************************
         *  FIND VIEW BY ID
         *****************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Inventory");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Inventory.this, Activity_Main.class);
                startActivity(intent);
            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        card_view_info = (CardView) findViewById(R.id.card_view_info);
        card_view_info.setVisibility(View.GONE);

        layout_computer = (LinearLayoutCompat) findViewById(R.id.layout_computer);
        layout_accessories = (LinearLayoutCompat) findViewById(R.id.layout_accessories);
        layout_safety_kit = (LinearLayoutCompat) findViewById(R.id.layout_safety_kit);
        layout_ti_tools = (LinearLayoutCompat) findViewById(R.id.layout_ti_tools);
        layout_ue = (LinearLayoutCompat) findViewById(R.id.layout_ue);
        layout_special_kits = (LinearLayoutCompat) findViewById(R.id.layout_special_kits);
        layout_cue = (LinearLayoutCompat) findViewById(R.id.layout_cue);

        nested_list_view_computer = (NestedListview) findViewById(R.id.nested_list_view_computer);
        nested_list_view_accessories = (NestedListview) findViewById(R.id.nested_list_view_accessories);
        nested_list_view_safety_kit = (NestedListview) findViewById(R.id.nested_list_view_safety_kit);
        nested_list_view_ti_tools = (NestedListview) findViewById(R.id.nested_list_view_ti_tools);
        nested_list_view_ue = (NestedListview) findViewById(R.id.nested_list_view_ue);
        nested_list_view_special_kit = (NestedListview) findViewById(R.id.nested_list_view_special_kit);
        nested_list_view_cue = (NestedListview) findViewById(R.id.nested_list_view_cue);

        text_view_message_computer = (TextView) findViewById(R.id.text_view_message_computer);
        text_view_message_accessories = (TextView) findViewById(R.id.text_view_message_accessories);
        text_view_message_safty_kit = (TextView) findViewById(R.id.text_view_message_safty_kit);
        text_view_message_ti_tools = (TextView) findViewById(R.id.text_view_message_ti_tools);
        text_view_message_ue = (TextView) findViewById(R.id.text_view_message_ue);
        text_view_message_special_kit = (TextView) findViewById(R.id.text_view_message_special_kit);
        text_view_message_cue = (TextView) findViewById(R.id.text_view_message_cue);

        button_confirm = (AppCompatButton) findViewById(R.id.button_confirm);

        swipe_refresh.setOnRefreshListener(this);

        nested_list_view_computer.setEmptyView(text_view_message_computer);
        nested_list_view_accessories.setEmptyView(text_view_message_accessories);
        nested_list_view_safety_kit.setEmptyView(text_view_message_safty_kit);
        nested_list_view_ti_tools.setEmptyView(text_view_message_ti_tools);
        nested_list_view_ue.setEmptyView(text_view_message_ue);
        nested_list_view_special_kit.setEmptyView(text_view_message_special_kit);
        nested_list_view_cue.setEmptyView(text_view_message_cue);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();
        array_list_computer = new ArrayList<>();
        arraylist_accessories = new ArrayList<>();
        arraylist_safty_kit = new ArrayList<>();
        arraylist_ti_tools = new ArrayList<>();
        arraylist_ue = new ArrayList<>();
        arraylist_special_kit = new ArrayList<>();
        arraylist_cue = new ArrayList<>();

        /*************************
         *  GET DATA
         *************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                array_list_computer.clear();
                arraylist_accessories.clear();
                arraylist_cue.clear();
                arraylist_safty_kit.clear();
                arraylist_special_kit.clear();
                arraylist_ti_tools.clear();
                arraylist_ue.clear();

                queue = Volley.newRequestQueue(Activity_Inventory.this);
                Function_Is_Confirm_Inventory();

            }
        });






        /*************************
         *  ACTION
         *************************/

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new SpotsDialog(Activity_Inventory.this);
                dialog.show();
                queue = Volley.newRequestQueue(Activity_Inventory.this);
                Function_Confirm_Inventory();

            }
        });
    }

    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(Activity_Inventory.this)){
                utility.Function_Show_Not_Network_Message(Activity_Inventory.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }

    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Is_Confirm_Inventory() {

        System.out.println("### AppConfig.URL_IS_INVENTORY_CONFIRM " + AppConfig.URL_IS_INVENTORY_CONFIRM);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_IS_INVENTORY_CONFIRM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("###  AppConfig.URL_IS_INVENTORY_CONFIRM : onResponse " + response);

                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        int int_confirm = obj.getInt("kuf");

                        if (int_confirm == 1){
                            card_view_info.setVisibility(View.VISIBLE);
                        }else{
                            card_view_info.setVisibility(View.GONE);
                        }

                    } else if (status == 400) {

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }

                queue = Volley.newRequestQueue(Activity_Inventory.this);
                Function_Get_Inventory();


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_IS_INVENTORY_CONFIRM onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_IS_INVENTORY_CONFIRM onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Inventory.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_IS_INVENTORY_CONFIRM " + "userid" + " : " + str_user_id);

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
    private void Function_Get_Inventory() {

        System.out.println("### AppConfig.URL_INVENTORY " + AppConfig.URL_INVENTORY);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_INVENTORY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_INVENTORY : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {



                        JSONArray array_computer = obj.getJSONArray("computer");

                        for (int count =0; count < array_computer.length(); count++){

                            JSONObject obj_one = (JSONObject) array_computer.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            array_list_computer.add(item);

                        }

                        JSONArray array_accessories = obj.getJSONArray("accessories");

                        for (int count =0; count < array_accessories.length(); count++){

                            JSONObject obj_one = (JSONObject) array_accessories.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_accessories.add(item);

                        }


                        JSONArray array_safety_kit = obj.getJSONArray("safety_kit");

                        for (int count =0; count < array_safety_kit.length(); count++){

                            JSONObject obj_one = (JSONObject) array_safety_kit.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_safty_kit.add(item);

                        }

                        JSONArray array_ti_tools = obj.getJSONArray("ti_tools");

                        for (int count =0; count < array_ti_tools.length(); count++){

                            JSONObject obj_one = (JSONObject) array_ti_tools.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_ti_tools.add(item);

                        }

                        JSONArray array_ue = obj.getJSONArray("ue");

                        for (int count =0; count < array_ue.length(); count++){

                            JSONObject obj_one = (JSONObject) array_ue.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_ue.add(item);

                        }



                        JSONArray array_special_kit = obj.getJSONArray("special_kit");

                        for (int count =0; count < array_special_kit.length(); count++){

                            JSONObject obj_one = (JSONObject) array_special_kit.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_special_kit.add(item);

                        }

                        JSONArray array_cue = obj.getJSONArray("cue");

                        for (int count =0; count < array_cue.length(); count++){

                            JSONObject obj_one = (JSONObject) array_cue.get(count);

                            String str_inventory_type = obj_one.getString(TAG_INVENTORY_TYPE);
                            String str_inventory_configuration = obj_one.getString(TAG_INVENTORY_CONFIGURATION);
                            String str_inventory_model = obj_one.getString(TAG_INVENTORY_MODEL);
                            String str_inventory_sno = obj_one.getString(TAG_INVENTORY_SNO);


                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_INVENTORY_TYPE, str_inventory_type);
                            item.put(TAG_INVENTORY_CONFIGURATION, str_inventory_configuration);
                            item.put(TAG_INVENTORY_MODEL, str_inventory_model);
                            item.put(TAG_INVENTORY_SNO, str_inventory_sno);

                            arraylist_cue.add(item);

                        }


                    } else if (status == 400) {

                        swipe_refresh.setRefreshing(false);

                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Inventory_List adapter_computer = new Adapter_Inventory_List(Activity_Inventory.this, array_list_computer);
                    nested_list_view_computer.setAdapter(adapter_computer);

                    Adapter_Inventory_List adapter_accessories = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_accessories);
                    nested_list_view_accessories.setAdapter(adapter_accessories);

                    Adapter_Inventory_List adapter_safety_kit = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_safty_kit);
                    nested_list_view_safety_kit.setAdapter(adapter_safety_kit);

                    Adapter_Inventory_List adapter_ti_tools = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_ti_tools);
                    nested_list_view_ti_tools.setAdapter(adapter_ti_tools);

                    Adapter_Inventory_List adapter_ue = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_ue);
                    nested_list_view_ue.setAdapter(adapter_ue);

                    Adapter_Inventory_List adapter_special_kit = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_special_kit);
                    nested_list_view_special_kit.setAdapter(adapter_special_kit);

                    Adapter_Inventory_List adapter_cue = new Adapter_Inventory_List(Activity_Inventory.this, arraylist_cue);
                    nested_list_view_cue.setAdapter(adapter_cue);


                    if (array_list_computer.isEmpty())
                        layout_computer.setVisibility(View.GONE);

                    if (arraylist_accessories.isEmpty())
                        layout_accessories.setVisibility(View.GONE);

                    if (arraylist_safty_kit.isEmpty())
                        layout_safety_kit.setVisibility(View.GONE);

                    if (arraylist_ti_tools.isEmpty())
                        layout_ti_tools.setVisibility(View.GONE);

                    if (arraylist_ue.isEmpty())
                        layout_ue.setVisibility(View.GONE);

                    if (arraylist_special_kit.isEmpty())
                        layout_special_kits.setVisibility(View.GONE);

                    if (arraylist_cue.isEmpty())
                        layout_cue.setVisibility(View.GONE);

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

                System.out.println("### AppConfig.URL_INVENTORY onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_INVENTORY onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Inventory.this);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_INVENTORY " + "userid" + " : " + str_user_id);

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
    public void Function_Confirm_Inventory() {

        System.out.println("### AppConfig.URL_INVENTORY_CONFIRM " + AppConfig.URL_INVENTORY_CONFIRM);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_INVENTORY_CONFIRM, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_INVENTORY_CONFIRM : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        TastyToast.makeText(getApplicationContext(), "Inventory Confirmed Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

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
                    System.out.println("### exception "+e.getLocalizedMessage());

                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_INVENTORY_CONFIRM onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_INVENTORY_CONFIRM onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Inventory.this);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_INVENTORY_CONFIRM " + "userid" + " : " + str_user_id);

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

        array_list_computer.clear();
        arraylist_accessories.clear();
        arraylist_cue.clear();
        arraylist_safty_kit.clear();
        arraylist_special_kit.clear();
        arraylist_ti_tools.clear();
        arraylist_ue.clear();

        queue = Volley.newRequestQueue(Activity_Inventory.this);
        Function_Is_Confirm_Inventory();

    }
}
