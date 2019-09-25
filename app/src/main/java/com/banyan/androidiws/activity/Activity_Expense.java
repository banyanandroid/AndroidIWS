package com.banyan.androidiws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Expense_List;
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


public class Activity_Expense extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static  final  String TAG_MONTH = "month";
    public static  final  String TAG_EXPENSE = "expense";
    public static  final  String TAG_APPROVED = "approved";
    public static  final  String TAG_PAID = "paid";
    public static  final  String TAG_BILL_STATUS = "bills";
    public static  final  String TAG_PENDING = "pending";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipe_refresh;

    private ListView list_view;

    private TextView textview_message;

    private SpotsDialog dialog;

    private Dialog dialog_location_permission;

    private RequestQueue queue;

    private ArrayList<HashMap<String, String>> arraylist_expense;

    private final Integer WRITE_EXTERNAL_STORAGE = 0x1;

    private String str_user_id, str_user_name, str_user_type, str_user_role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);


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
        toolbar.setTitle("Expense");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Expense.this, Activity_Main.class);
                startActivity(intent);
            }
        });

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        list_view = (ListView) findViewById(R.id.list_view);
        textview_message = (TextView) findViewById(R.id.textview_message);

        swipe_refresh.setOnRefreshListener(this);

        list_view.setEmptyView(textview_message);

        /*************************
         *  SETUP
         *************************/
        arraylist_expense = new ArrayList<>();
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);

        /*************************
         *  GET DATA
         *************************/

        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {

                swipe_refresh.setRefreshing(true);

                arraylist_expense.clear();

                queue = Volley.newRequestQueue(Activity_Expense.this);
                Function_Get_Expense();

            }
        });


        /*************************
         *  SET DATA
         *************************/


    }


    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(Activity_Expense.this)){
                utility.Function_Show_Not_Network_Message(Activity_Expense.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }

    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Get_Expense() {

        System.out.println("### AppConfig.URL_EXPENSE " + AppConfig.URL_EXPENSE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_EXPENSE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_EXPENSE : onResponse " + response);
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
                            String str_expense = obj_one.getString(TAG_EXPENSE);
                            String str_paid = obj_one.getString(TAG_PAID);
                            String str_bill_status = obj_one.getString(TAG_BILL_STATUS);
                            String str_approved = obj_one.getString(TAG_APPROVED);
//                            String str_pending = obj_one.getString(TAG_PENDING);

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_MONTH, str_month);
                            item.put(TAG_EXPENSE, str_expense);
                            item.put(TAG_PAID, str_paid);
                            item.put(TAG_BILL_STATUS, str_bill_status);
                            item.put(TAG_APPROVED, str_approved);
//                            item.put(TAG_PENDING, str_pending);

                            arraylist_expense.add(item);

                        }



                    } else if (status == 400) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Expense_List adapter_expense_list = new Adapter_Expense_List(Activity_Expense.this, arraylist_expense, str_user_id);
                    list_view.setAdapter(adapter_expense_list);

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

                System.out.println("### AppConfig.URL_EXPENSE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_EXPENSE onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(Activity_Expense.this);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_EXPENSE " + "userid" + " : " + str_user_id);

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

    /*********************************
     * For Loaction
     ********************************/
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if ic_user_1 has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            //Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Location
                case 1:

                    break;
                //Call
                case 2:

                    break;
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            switch (requestCode) {
                //Location
                case 1:

                    System.out.println("### location denied requestCode "+requestCode);

                    if (dialog_location_permission != null)
                        dialog_location_permission.dismiss();

                    dialog_location_permission = new Dialog(this);
                    dialog_location_permission.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog_location_permission.setContentView(R.layout.custom_dialog2);
                    dialog_location_permission.setCancelable(false);
                    TextView text=(TextView)dialog_location_permission.findViewById(R.id.text);
                    text.setText("Please give permission to access memory card to use this app.");
                    Button ok=(Button)dialog_location_permission.findViewById(R.id.ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE);
                            dialog_location_permission.dismiss();

                        }
                    });
                    dialog_location_permission.show();

                    break;
                //Call
                case 2:
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    break;
                //Device ID
                case 3:
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    break;

            }

        }
    }

    @Override
    public void onRefresh() {

        swipe_refresh.setRefreshing(true);

        arraylist_expense.clear();

        queue = Volley.newRequestQueue(Activity_Expense.this);
        Function_Get_Expense();

    }
}
