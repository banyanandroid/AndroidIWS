package com.banyan.androidiws.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.banyan.androidiws.R;
import com.banyan.androidiws.service.Service_PTW_Status;

public class BroadcastReceiver_Alarm_Receiver_PTW_Status extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("###  BroadcastReceiver_Alarm_Receiver_PTW_Status");

        // call ptw request
        Intent intent_ptw_status = new Intent(context, Service_PTW_Status.class);
        context.startService(intent_ptw_status);

    }

}