package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class Activity_Login extends AppCompatActivity {

    public static final String TAG_FORGOT_PASSWORD_USERNAME = "username";

    public static final String TAG_LOGIN_USERNAME = "username";
    public static final String TAG_LOGIN_PASSWORD = "password";

    public static final String TAG_LOGIN_USER_ID = "user_id";
    public static final String TAG_LOGIN_USER_NAME = "user_name";
    public static final String TAG_LOGIN_USER_ROLE = "user_role";
    public static final String TAG_LOGIN_USER_TYPE = "user_type";
    public static final String TAG_LOGIN_DEPARTMENT_ID = "department_id";
    public static final String TAG_LOGIN_PROFILE_ID = "profile_id";
    public static final String TAG_LOGIN_PROFILE_IMAGE = "profile_photo";
    public static final String TAG_LOGIN_FIRST_NAME = "fname";
    public static final String TAG_LOGIN_LAST_NAME = "lname";

    private EditText edit_username, edit_password;

    private Button button_login;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private Session_Manager session_manager;

    private LinearLayout layout_root;

    private Util utility;

    private String str_username = "", str_password = "", str_forgot_password_username = "";

    private long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__login);

        /*********************************
         *  SETUP
         **********************************/
        utility = new Util();


        /*********************************
        *  FIND VIEW BY ID
        **********************************/
        Function_Verify_Network_Available(this);

        session_manager = new Session_Manager(getApplicationContext());
        Boolean isLogin = session_manager.isLoggedIn();
        if (isLogin){
            Intent intent = new Intent(Activity_Login.this, MainActivity.class);
            startActivity(intent);

            finish();
        }

        layout_root = (LinearLayout)findViewById(R.id.layout_root);
        edit_username = (EditText)findViewById(R.id.edit_email);
        edit_password = (EditText)findViewById(R.id.edit_password);
        button_login = (Button)findViewById(R.id.button_login);

        /*********************************
         *  SESSION
         **********************************/

        session_manager = new Session_Manager(this);


        /*********************************
         *  ACTION
         **********************************/

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_username = edit_username.getText().toString().trim();
                str_password = edit_password.getText().toString().trim();

                if (!utility.IsNetworkAvailable(Activity_Login.this)){
                    utility.Function_Show_Not_Network_Message(Activity_Login.this);
                }else if (str_username.isEmpty()) {
                    TastyToast.makeText(Activity_Login.this, "Enter User Name ", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else if (str_password.isEmpty()) {
                    TastyToast.makeText(Activity_Login.this, "Enter Password ", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                } else {

                    dialog = new SpotsDialog(Activity_Login.this);
                    dialog.show();

                    queue = Volley.newRequestQueue(Activity_Login.this);
                    Function_Login();

                }

            }
        });

    }

    /********************************
     *FUNCTION FORGOT PASSWORD
     *********************************/
    private void Function_Forgot_Password() {

        System.out.println("### AppConfig.URL_FORGOT_PASSWORD " + AppConfig.URL_FORGOT_PASSWORD);

        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_FORGOT_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.url_student_login : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int success = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (success == 200) {


                        TastyToast.makeText(Activity_Login.this, "Password Reseted Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        dialog.dismiss();


                    } else if (success == 400){

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }else if (success == 404){

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.url_student_login onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.url_student_login onErrorResponse " + error.getLocalizedMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_FORGOT_PASSWORD_USERNAME, str_forgot_password_username);

                System.out.println("###" + TAG_FORGOT_PASSWORD_USERNAME + " : " + str_forgot_password_username);

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
    private void Function_Login() {

        System.out.println("### AppConfig.URL_LOGIN " + AppConfig.URL_LOGIN);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_LOGIN : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONObject obj_one = obj.getJSONObject("records");

                        String str_user_id = obj_one.getString(TAG_LOGIN_USER_ID);
                        String str_user_name = obj_one.getString(TAG_LOGIN_USER_NAME);
                        String str_user_role = obj_one.getString(TAG_LOGIN_USER_ROLE);
                        String str_user_type = obj_one.getString(TAG_LOGIN_USER_TYPE);
                        String str_user_department_id = obj_one.getString(TAG_LOGIN_DEPARTMENT_ID);
                        String str_user_profile_id = obj_one.getString(TAG_LOGIN_PROFILE_ID);


                        System.out.println("USER_DETAILS :"+str_user_id+" "+str_user_type+" "+str_user_department_id);
                        session_manager.createLoginSession(str_user_id, str_user_name, str_user_type, str_user_role, str_user_department_id, str_user_profile_id);
                        TastyToast.makeText(Activity_Login.this, "Login Successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                        Intent intent = new Intent(Activity_Login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left); // open new acitivity
                        dialog.dismiss();


                    } else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getApplicationContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

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

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_LOGIN_USERNAME, str_username);
                params.put(TAG_LOGIN_PASSWORD, str_password);

                System.out.println("###" + TAG_LOGIN_USERNAME + " : " + str_username);
                System.out.println("###" + TAG_LOGIN_PASSWORD + " : " + str_password);

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

            Toast.makeText(getBaseContext(), "Press Once Again To Exit!", Toast.LENGTH_SHORT).show();
        }

        back_pressed = System.currentTimeMillis();


    }
}
