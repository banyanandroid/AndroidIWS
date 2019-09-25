package com.banyan.androidiws.activity;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Utility;


public class Activity_About extends AppCompatActivity {


    private TextView text_version;

    private Toolbar toolbar;
    private Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about);

        Function_Verify_Network_Available(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        text_version = (TextView)findViewById(R.id.text_version);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            text_version.setText("Version : "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void Function_Verify_Network_Available(Context context) {
        try {
            if (!utility.IsNetworkAvailable(this)) {
                utility.Function_Show_Not_Network_Message(this);
            }
            ;
        } catch (Exception e) {
            System.out.println("### Exception e " + e.getLocalizedMessage());
        }
    }
}
