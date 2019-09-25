package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.banyan.androidiws.R;
import com.banyan.androidiws.adapter.Adapter_Productivity_MM_List;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Productivity_MM extends AppCompatActivity {

    public static  final  String TAG_MONTH = "month";
    public static  final  String TAG_DAYS_WORKED = "days_worked";
    public static  final  String TAG_TIME_SHEET_RECEIVED = "time_sheet_received";
    public static  final  String TAG_PRODUCTIVIY = "productivity";
    public static  final  String TAG_RATING = "rating";

    private Utility utility;

    private Session_Manager session;

    private Toolbar toolbar;

    private ListView list_view;

    private String str_user_id, str_user_name, str_user_type, str_user_role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productivity_mm);

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

                Intent intent = new Intent(Activity_Productivity_MM.this, Activity_Main.class);
                startActivity(intent);
            }
        });
        list_view = (ListView) findViewById(R.id.list_view);

        /*************************
         *  GET DAT
         *************************/
        ArrayList<HashMap<String, String>> arraylist_productivity = new ArrayList<>();

        HashMap<String, String> item = new HashMap<>();
        item.put(TAG_MONTH, "January");
        item.put(TAG_DAYS_WORKED, "10 days");
        item.put(TAG_TIME_SHEET_RECEIVED, "10 days");
        item.put(TAG_PRODUCTIVIY, "productivity");
        item.put(TAG_RATING, "Rating");

        arraylist_productivity.add(item);
        arraylist_productivity.add(item);
        arraylist_productivity.add(item);

        Adapter_Productivity_MM_List adapter_productivity_ni_npo_tk_list = new Adapter_Productivity_MM_List(this, arraylist_productivity);
        list_view.setAdapter(adapter_productivity_ni_npo_tk_list);

    }

    public void Function_Verify_Network_Available(Context context){

        System.out.println("#### Function_Verify_Network_Available ");
        try{

            if (!utility.IsNetworkAvailable(Activity_Productivity_MM.this)){
                utility.Function_Show_Not_Network_Message(Activity_Productivity_MM.this);
            }

        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }

    }

}
