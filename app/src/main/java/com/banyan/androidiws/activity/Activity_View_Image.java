package com.banyan.androidiws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Utility;
import com.bumptech.glide.Glide;


public class Activity_View_Image extends AppCompatActivity {

    public static final String TAG_IMAGE_URL = "image_url";

    private AppCompatImageView image_view_photo;

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private Toolbar toolbar;

    private boolean bol_is_online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        /***************************
        *  FIND VIEW BY ID
        ****************************/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("View Image");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_primary_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        image_view_photo = (AppCompatImageView)findViewById(R.id.image_view_photo);

        /***************************
         *  SETUP
         ****************************/
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        bol_is_online = new Utility().IsNetworkAvailable(Activity_View_Image.this);

        /***************************
         *  GET DATA
         ****************************/
        String str_image_url = sharedPreferences.getString(TAG_IMAGE_URL, "");

        System.out.println("### str_image_url "+str_image_url);

        if (str_image_url.isEmpty())
            str_image_url = "test";

        if (bol_is_online){

            Glide.with(this)
                    .load(str_image_url)
                    .placeholder(R.drawable.ic_galary)
                    .into(image_view_photo);

        }else{

            Bitmap bitmap = new Utility().Function_StringToBitMap(str_image_url);

            if (bitmap != null){
                image_view_photo.setImageBitmap(bitmap);
            }

        }


    }
}
