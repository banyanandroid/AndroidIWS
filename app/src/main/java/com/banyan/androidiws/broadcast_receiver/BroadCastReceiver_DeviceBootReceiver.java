package com.banyan.androidiws.broadcast_receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.Toast;

import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_PTW_Status;

import java.util.List;

public class BroadCastReceiver_DeviceBootReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("###  BroadcastReceiver_Alarm_Receiver_PTW_Status");

        Toast.makeText(context, "Device booted", Toast.LENGTH_LONG ).show();

      /*  // set alarms after device boot
        List<Model_PTW_Status> list_ptw_status = new DatabaseHandler(context).getAllPTW();

        for (int count = 0; count < list_ptw_status.size(); count++){
            Model_PTW_Status model = list_ptw_status.get(count);

            String str_dpr_id = model.getDPR_ID();

            Integer int_dpr_id = Integer.parseInt(str_dpr_id);

            //set alarm
            Intent alarmIntent = new Intent(context, BroadcastReceiver_Alarm_Receiver_PTW_Status.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, int_dpr_id, alarmIntent, 0);
            Integer int_intevel = 60000;
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + int_intevel,
                    int_intevel, pendingIntent);

        }*/

    }

}