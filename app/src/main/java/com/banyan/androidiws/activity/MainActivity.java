package com.banyan.androidiws.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Session_Manager;
import com.banyan.androidiws.global.Util;

public class MainActivity extends AppCompatActivity {

    private GridLayout mainGrid;

    private CardView cardview_my_account, cardview_Leave_tracker,
            cardview_logout;

    private Session_Manager session_manager;

    private Util utility;

    private long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*********************************
         *  SETUP
         **********************************/
        utility = new Util();


        /*****************************
        *  FIND VIEW BY IDcardview_pay_slip
        *****************************/

        mainGrid = (GridLayout) findViewById(R.id.mainGrid);
        cardview_my_account = (CardView) findViewById(R.id.cardview_my_account);
        cardview_Leave_tracker = (CardView) findViewById(R.id.cardview_Leave_tracker);
        cardview_logout = (CardView) findViewById(R.id.cardview_logout);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);

        /*****************************
         *  SESSION
         *****************************/

        session_manager = new Session_Manager(this);
        session_manager.checkLogin();



        /*****************************
         *  ACTION
         *****************************/
        cardview_my_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Activity_Account_Details.class);
                startActivity(intent);

            }
        });

        cardview_Leave_tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, ActivityLeaveTracker.class);
                startActivity(intent);

            }
        });


        cardview_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                session_manager.logoutUser();
                Toast.makeText(MainActivity.this, "Logout Sucessfully.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(MainActivity.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(MainActivity.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (finalI == 0) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 1) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 2) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 3) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 4) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }else if (finalI == 5) {
                        Toast.makeText(MainActivity.this, "State : " + finalI, Toast.LENGTH_SHORT).show();
                    }

                    /*Intent intent = new Intent(MainActivity.this,ActivityOne.class);
                    intent.putExtra("info","This is activity from card item index  "+finalI);
                    startActivity(intent);*/

                }
            });
        }
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

            Toast.makeText(getBaseContext(), "Press Once Again To Exit.", Toast.LENGTH_SHORT).show();

        }

        back_pressed = System.currentTimeMillis();

    }

}
