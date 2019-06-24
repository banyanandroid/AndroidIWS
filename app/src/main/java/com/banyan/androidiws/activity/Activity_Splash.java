package com.banyan.androidiws.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.banyan.androidiws.R;

public class Activity_Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
				
                Intent intent = new Intent(Activity_Splash.this, Activity_Login.class );
                startActivity(intent);
                finish();
				
            }
        }, SPLASH_TIME_OUT);

    }
}
