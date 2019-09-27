package com.banyan.androidiws.service;


import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_DPR_ID;
import com.banyan.androidiws.database.Model_DB_Declaration;
import com.banyan.androidiws.database.Model_OHS_Work;
import com.banyan.androidiws.database.Model_PTW_Request;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Constants;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Service_Upload_Project_Data extends Service {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private DatabaseHandler db_handle;

    private RequestQueue queue;

    private Session_Manager session;

    private String str_user_id = "";


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("###  Service_Upload_Project_Data");

        /********************************
        * SETUP
        ********************************/
        session = new Session_Manager(this);
        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        db_handle = new DatabaseHandler(this);

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


        /********************************
         * UPLOAD DATA
         ********************************/

         if (int_reached_site_count > 0){

             List<Model_DPR_ID> list_reached_site = db_handle.Function_Get_All_Reach_Site();

             for (int count = 0; count <  list_reached_site.size(); count++){

                 Model_DPR_ID reached_site = list_reached_site.get(count);

                 Integer int_id = reached_site.getID();
                 String str_dpr_id = reached_site.getDPR_ID();

                 queue = Volley.newRequestQueue(this);
                 Function_Update_Site_Reached(int_id, str_dpr_id);
             }
         }


        if (int_declaration_count > 0){

            List<Model_DB_Declaration> list_declaration = db_handle.Function_Get_All_Declaration();

            for (int count = 0; count <  list_declaration.size(); count++){

                Model_DB_Declaration reached_site = list_declaration.get(count);

                Integer int_id = reached_site.getID();
                String str_dpr_id = reached_site.getDPR_ID();
                String str_declaration_type = reached_site.getDeclaration_type();
                String str_declaration = reached_site.getDeclaration();
                String str_check_box = reached_site.getCheckbox();
                String str_comment = reached_site.getComments();
                String str_declaration_text = reached_site.getText();
                String str_customer_ptw = reached_site.getCustomer_ptw();
                String str_image = reached_site.getImage_url();

                System.out.println("### str_check_box "+str_check_box);
                System.out.println("### str_image "+str_image);
                System.out.println("### str_comment "+str_comment);

                queue = Volley.newRequestQueue(this);
                Function_Update_PTW_Declaration_Request(int_id, str_dpr_id, str_declaration_type, str_declaration, str_check_box, str_image,
                        str_comment, str_customer_ptw);

            }
        }

        if (int_ptw_request_count > 0){

            List<Model_PTW_Request> list_ptw_request = db_handle.Function_Get_All_PTW_REQUEST();

            for (int count = 0; count <  list_ptw_request.size(); count++){

                Model_PTW_Request ptw_request = list_ptw_request.get(count);

                Integer int_id = ptw_request.getID();
                String str_dpr_id = ptw_request.getDPR_ID();
                String str_ptw_required = ptw_request.getPtwrequired();
                String str_ptw_no = ptw_request.getPtwno();
                Bitmap bitmap = ptw_request.getPtwcopy();

                String str_ptw_copy = "";
                if (bitmap != null){
                    str_ptw_copy =  new Utility().Function_BitMapToString(bitmap);
                }


                queue = Volley.newRequestQueue(this);
                Function_Update_PTW(int_id, str_dpr_id, str_ptw_required, str_ptw_no, str_ptw_copy);
            }
        }

        if (int_start_work_count > 0){

            List<Model_DPR_ID> list_start_work = db_handle.Function_Get_All_Start_Work();

            for (int count = 0; count <  list_start_work.size(); count++){

                Model_DPR_ID start_work = list_start_work.get(count);

                Integer int_id = start_work.getID();
                String str_dpr_id = start_work.getDPR_ID();

                queue = Volley.newRequestQueue(this);
                Function_Start_Work(int_id, str_dpr_id);
            }
        }

        if (int_ohs_work_count > 0){

            List<Model_OHS_Work> list_ohs_work = db_handle.Function_Get_All_OHS_Work();

            for (int count = 0; count <  list_ohs_work.size(); count++){

                Model_OHS_Work ohs_work = list_ohs_work.get(count);

                Integer int_id = ohs_work.getID();
                String str_dpr_id = ohs_work.getDPR_ID();
                Bitmap bitmap = ohs_work.getImage();
                String str_ohs_image = new Utility().Function_BitMapToString(bitmap);

                queue = Volley.newRequestQueue(this);
                Function_Upload_OHS_Picture(int_id, str_dpr_id, str_user_id, str_ohs_image);

            }
        }

    }


    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Update_Site_Reached(final Integer int_id, final String str_dpr_id) {

        System.out.println("### Service_Upload_Project_Data AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE " + AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        Model_DPR_ID model_dpr_id = new Model_DPR_ID(int_id);
                        Integer int_result = db_handle.Function_Delete_Reached_Site(model_dpr_id);

                        System.out.println("### Function_Update_Site_Reached int_result "+int_result);

                    } else if (status == 400) {


                    } else if (status == 404) {

                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_REACHED_SITE " + "dpr_id" + " : " + str_dpr_id);

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
     *FUNCTION DECLARATION REQUEST
     ********************************
     * @param
     * @param str_customer_ptw */
    private void Function_Update_PTW_Declaration_Request(final Integer  int_id, final String str_dpr_id, final String str_declaration_type, final String str_declaration,
                                                         final String str_check_box,
                                                         final String str_image, final String str_comment, final String str_customer_ptw) {

        System.out.println("### Service_Upload_Project_Data AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST " + AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");

                    if (status == 200 || status == 404) {

                        Model_DB_Declaration model_declaration = new Model_DB_Declaration(int_id);
                        Integer int_result = db_handle.Function_Delete_Declaration(model_declaration);

                        System.out.println("### Function_Update_Site_Reached int_result "+int_result);

                    } else if (status == 400) {


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST onErrorResponse " + error.getLocalizedMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("userid", str_user_id);
                params.put("declaration_type", str_declaration_type);
                params.put("declaration", str_declaration);
                params.put("text", str_customer_ptw);
                params.put("chekbox", str_check_box);
                params.put("image", str_image);
                params.put("comments", str_comment);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_PTW_DECLARATION_REQUEST " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "userid" + " : " + str_user_id);
                System.out.println("###  " + "declaration_type" + " : " + str_declaration_type);
                System.out.println("###  " + "declaration" + " : " + str_declaration);
                System.out.println("###  " + "text" + " : " + str_customer_ptw);
                System.out.println("###  " + "chekbox" + " : " + str_check_box);
                System.out.println("###  " + "image " + " : " + str_image);
                System.out.println("###  " + "comments" + " : " + str_comment);

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
     *FUNCTION GET PTW
     *********************************/
    private void Function_Update_PTW(final Integer int_id, final String str_dpr_id, final String str_ptw_required, final String str_ptw_no,
                                     final String str_ptw_copy) {

        System.out.println("### Service_Upload_Project_Data AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW " + AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        Model_PTW_Request model_ptw_request = new Model_PTW_Request(int_id);
                        Integer int_result = db_handle.Function_Delete_PTW_Request(model_ptw_request);

                        System.out.println("### Function_Update_PTW int_result "+int_result);

                    } else if (status == 400) {


                    } else if (status == 404) {

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW onErrorResponse " + error.getLocalizedMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("ptwrequired", str_ptw_required);
                params.put("ptwno", str_ptw_no);
                params.put("ptwcopy", str_ptw_copy);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPDATE_PTW " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "ptwrequired" + " : " + str_ptw_required);
                System.out.println("###  " + "ptwno" + " : " + str_ptw_no);
                System.out.println("###  " + "ptwcopy" + " : " + str_ptw_copy);

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
     *FUNCTION START WORK
     *********************************/
    private void Function_Start_Work(final Integer int_id, final String str_dpr_id) {

        System.out.println("### Service_Upload_Project_Data AppConfig.URL_PROJECT_UPDATE_START_TIME " + AppConfig.URL_PROJECT_UPDATE_START_TIME);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_START_TIME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_START_TIME : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        Model_DPR_ID model_ptw_request = new Model_DPR_ID(int_id);
                        Integer int_result = db_handle.Function_Delete_Start_Work(model_ptw_request);

                        System.out.println("### Function_Start_Work int_result "+int_result);

                    } else if (status == 400) {


                    } else if (status == 404) {


                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME onErrorResponse " + error.getLocalizedMessage());


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_START_TIME " + "dpr_id" + " : " + str_dpr_id);

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
    private void Function_Upload_OHS_Picture(final int int_id, final String str_dpr_id, final String str_user_id, final String str_ohs_image) {

        System.out.println("### Service_Upload_Project_Data AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE " + AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        Model_OHS_Work model_ptw_request = new Model_OHS_Work(int_id);
                        Integer int_result = db_handle.Function_Delete_OHS_Work(model_ptw_request);

                        System.out.println("### Function_Upload_OHS_Picture int_result "+int_result);

                    } else if (status == 400) {


                    } else if (status == 404) {


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception " + e.getLocalizedMessage());

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE onErrorResponse " + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);
                params.put("userid", str_user_id);
                params.put("image", str_ohs_image);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_UPLOAD_OHS_WORK_IMAGE " + "dpr_id" + " : " + str_dpr_id);
                System.out.println("###  " + "userid" + " : " + str_user_id);
                System.out.println("###  " + "image" + " : " + str_ohs_image);

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

}
