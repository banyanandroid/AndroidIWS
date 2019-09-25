package com.banyan.androidiws.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Project_NI_NPO_TK_Completed;
import com.banyan.androidiws.adapter.Adapter_Project_List;
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

public class Fragment_Project_Completed_List extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public static final String TAG_DPR_ID = "dpr_id";
    public static final String TAG_SITE_NAME = "site_name";
    public static final String TAG_ZONE = "zone";
    public static final String TAG_CIRCLE = "circle";
    public static final String TAG_PROJECT = "project";
    public static final String TAG_CATEGORY = "category";
    public static final String TAG_DATE = "Date";
    public static final String TAG_WP = "wp";
    public static final String TAG_ASSIGNED_BY = "assigned_by";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private SwipeRefreshLayout swipe_refresh;

    private AppCompatTextView text_view_message;

    private ListView list_view;

    private SpotsDialog dialog;

    private RequestQueue queue;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private  ArrayList<HashMap<String, String>> arrayList;

    private String str_user_id, str_user_name, str_user_type, str_user_role;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_project_list, container, false);

        /*********************************
         * SETUP
         **********************************/
        utility = new Utility();


        /*****************************
         *  SESSION
         *****************************/

        session = new Session_Manager(getContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

//        //testing
//        str_user_id = "463";

        System.out.println("### str_user_id " + str_user_id);
        System.out.println("### str_user_name " + str_user_name);
        System.out.println("### str_user_role " + str_user_role);


        /*************************
         *  FIND VIEW BY ID
         *************************/

        swipe_refresh = (SwipeRefreshLayout) root_view.findViewById(R.id.swipe_refresh);
        list_view = (ListView) root_view.findViewById(R.id.list_view);
        text_view_message = (AppCompatTextView) root_view.findViewById(R.id.text_view_message);

        swipe_refresh.setOnRefreshListener(this);

        list_view.setEmptyView(text_view_message);

        /*************************
         *  SETUP
         *************************/
        arrayList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();

        /*************************
         *  GET DATA
         *************************/
        swipe_refresh.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh.setRefreshing(true);
                queue = Volley.newRequestQueue(getContext());
                Function_Project_Update_List();
            }
        });

        /*************************
         *  SET DATA
         *************************/



        /*************************
         *  ACTION
         *************************/
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                //testing
                // needed production type
                HashMap<String, String> item = arrayList.get(position);

                String str_dpr_id  = item.get(TAG_DPR_ID);
                String str_site_name  = item.get(TAG_SITE_NAME);
                String str_project_name  = item.get(TAG_PROJECT);
                String str_category  = item.get(TAG_CATEGORY);
                String str_zone  = item.get(TAG_ZONE);
                String str_circle  = item.get(TAG_CIRCLE);
                /*String str_date  = item.get(TAG_DATE);
                String str_wp  = item.get(TAG_WP);
                String str_assigned_by  = item.get(TAG_ASSIGNED_BY);*/

                editor.putString(TAG_DPR_ID, str_dpr_id);
                editor.putString(TAG_SITE_NAME, str_site_name);
                editor.putString(TAG_PROJECT, str_project_name);
                editor.putString(TAG_CATEGORY, str_category);
                editor.putString(TAG_ZONE, str_zone);
                editor.putString(TAG_CIRCLE, str_circle);
                /*editor.putString(TAG_DATE, str_date);
                editor.putString(TAG_WP, str_wp);
                editor.putString(TAG_ASSIGNED_BY, str_assigned_by);*/
                editor.commit();

                Intent intent = new Intent(getContext(), Activity_Project_NI_NPO_TK_Completed.class);
//              Intent intent = new Intent(getContext(), Activity_Project_MM_Completed.class);
                startActivity(intent);

            }
        });

        return root_view;

    }





    /********************************
     *FUNCTION LOGIN
     *********************************/
    private void Function_Project_Update_List() {

        System.out.println("### AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED " + AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        JSONArray array_one = obj.getJSONArray("records");

                        for (int count =0; count < array_one.length(); count++){

                            JSONObject obj_one = (JSONObject) array_one.get(count);

                            String str_dpr_id = obj_one.getString(TAG_DPR_ID);
                            String str_site_name = obj_one.getString(TAG_SITE_NAME);
                            String str_zone = obj_one.getString(TAG_ZONE);
                            String str_cicle = obj_one.getString(TAG_CIRCLE);
                            String str_project = obj_one.getString(TAG_PROJECT);
                            String str_category = obj_one.getString(TAG_CATEGORY);
                            /*String str_date = obj_one.getString(TAG_DATE);
                            String str_wp = obj_one.getString(TAG_WP);
                            String str_assigned_by = obj_one.getString(TAG_ASSIGNED_BY);*/

                            HashMap<String, String> item = new HashMap<>();
                            item.put(TAG_DPR_ID, str_dpr_id);
                            item.put(TAG_SITE_NAME, str_site_name);
                            item.put(TAG_ZONE, str_zone);
                            item.put(TAG_CIRCLE, str_cicle);
                            item.put(TAG_PROJECT, str_project);
                            item.put(TAG_CATEGORY, str_category);
                            /*item.put(TAG_DATE, str_date);
                            item.put(TAG_WP, str_wp);
                            item.put(TAG_ASSIGNED_BY, str_assigned_by);*/


                            arrayList.add(item);

                        }


                    } else if (status == 400) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if (status == 404) {

                        swipe_refresh.setRefreshing(false);
                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }

                    Adapter_Project_List adapter_project_update_task_list = new Adapter_Project_List(getContext(), arrayList);
                    list_view.setAdapter(adapter_project_update_task_list);

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

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED onErrorResponse " + error.getLocalizedMessage());

                new Utility().Function_Error_Dialog(getActivity());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                //testing
                // user id = 463
                params.put("userid", str_user_id);

                System.out.println("### AppConfig.URL_PROJECT_UPDATE_SITE_LIST_COMLETED " + "userid" + " : " + str_user_id);

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

        arrayList.clear();
        Adapter_Project_List adapter_project_update_task_list = new Adapter_Project_List(getContext(), arrayList);
        list_view.setAdapter(adapter_project_update_task_list);

        queue = Volley.newRequestQueue(getContext());
        Function_Project_Update_List();

    }
}
