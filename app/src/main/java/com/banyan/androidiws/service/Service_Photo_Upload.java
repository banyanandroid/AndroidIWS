package com.banyan.androidiws.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.RequestQueue;
import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Project_NI_NPO_TK_In_Progress;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_15_Minutes_Photo_Upload;
import com.banyan.androidiws.global.Config;

import java.util.List;

import static com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List.TAG_DPR_ID;

public class Service_Photo_Upload extends Service {

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;

    private RequestQueue queue;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("###  Service_Photo_Upload");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        //get photo upload
        List<Model_15_Minutes_Photo_Upload> model_15_minutes_photo_uploadList = new DatabaseHandler(Service_Photo_Upload.this).getAllPhotoUpload();

        for (int count = 0; count < model_15_minutes_photo_uploadList.size(); count++){
            Model_15_Minutes_Photo_Upload item = model_15_minutes_photo_uploadList.get(count);
            String str_dpr_id = item.getDPR_ID();

            //show 15 minutes photo upload notification
            editor.putString(TAG_DPR_ID, str_dpr_id);
            editor.commit();

            Integer int_notification_id  = sharedPreferences.getInt(Config.STR_NOTIFICATION_ID, 0);

            int_notification_id++;

            System.out.println("### int_notification_id "+int_notification_id);

            editor.putInt(Config.STR_NOTIFICATION_ID, int_notification_id);
            editor.commit();

            Intent intent = new Intent(Service_Photo_Upload.this, Activity_Project_NI_NPO_TK_In_Progress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingInten_notification = PendingIntent.getActivity(Service_Photo_Upload.this, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(Service_Photo_Upload.this, "1")
                    .setSmallIcon(R.mipmap.ic_iws)
                    .setContentTitle("PTW Status Accepted,")
                    .setContentText("Your Received a PTW Status As Accepted. Click To Proceed.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingInten_notification)
                    .setAutoCancel(true);

            createNotificationChannel();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_Photo_Upload.this);

            notificationManager.notify(int_notification_id, builder.build());

        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
