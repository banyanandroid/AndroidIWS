package com.banyan.androidiws.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.banyan.androidiws.R;
import com.banyan.androidiws.fragment.Fragment_Project_Completed_List;
import com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List;
import com.banyan.androidiws.global.Session_Manager;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;


/**
 * Created by Karthik 07-07-2018.
 */


public class Activity_Project_Pager extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Session_Manager session;

    private Toolbar toolbar;

    public TabLayout tabLayout;

    public ViewPager viewPager;

    private String str_user_id = "", str_user_name = "", str_user_type_id = "";

    public  int int_items = 3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_pager);
        /*********************
         * SESSION
         * *******************/

        session = new Session_Manager(Activity_Project_Pager.this);
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type_id = user.get(Session_Manager.KEY_USER_TYPE_ID);

        System.out.println("str_user_id from Session ::::" + str_user_id);
        System.out.println("USER NAME from Session ::::" + str_user_name);
        System.out.println("str_user_type_id from Session ::::" + str_user_type_id);

        /*************************************
         *FIND VIEW BY ID
         *************************************/

        /*************************
         *  FIND VIEW BY ID
         *************************/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Project");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Activity_Project_Pager.this, Activity_Main.class);
                startActivity(intent);

            }
        });


        tabLayout = (TabLayout) findViewById(R.id.tabs_layout);
        viewPager = (ViewPager) findViewById(R.id.viewpager_enquiry);


        tabLayout.addTab(tabLayout.newTab().setText("IN PROGRESS"));
        //tabLayout.addTab(tabLayout.newTab().setText("HOLD"));
        tabLayout.addTab(tabLayout.newTab().setText("COMPLETED"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final MyAdapter adapter = new MyAdapter(Activity_Project_Pager.this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


       /* viewPager.setAdapter(new Activity_Project_Pager.MyAdapter(this, getSupportFragmentManager()));

        *//**
         * Now , this is a workaround ,
         * The setupWithViewPager dose't works without the runnable .
         * Maybe a Support Library Bug .
         *//*

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
*/

        /*********************************
         * SETUP
         *********************************/
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Activity_Project_Pager.this);
        editor = sharedPreferences.edit();


    }

    public class MyAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment_Project_In_Progress_List();
                case 1:
                    return new Fragment_Project_Completed_List();
                /*case 2:
                    return new Fragment_Project_Completed_List();*/
                default:
                    return null;
            }
        }
        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }

   /* class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        *//**
         * Return fragment with respect to Position .
         *//*

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Fragment_Project_In_Progress_List();
                case 1:
                    return new Fragment_Project_Completed_List();

            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }

        *//**
         * This method returns the title of the tab according to the position.
         *//*

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "IN PROGRESS";
                case 1:
                    return "COMPLETED";

            }
            return null;
        }
    }*/
}

