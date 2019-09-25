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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Project_NI_NPO_TK_In_Progress;
import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.database.Model_PTW_Status;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Config;
import com.banyan.androidiws.global.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List.TAG_DPR_ID;

public class Service_PTW_Status  extends Service {

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

        System.out.println("###  Service_PTW_Status");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        //get ptw status
        List<Model_PTW_Status> list_ptw_status = new DatabaseHandler(Service_PTW_Status.this).getAllPTW();

        for (int count = 0; count < list_ptw_status.size(); count++){
            Model_PTW_Status item = list_ptw_status.get(count);
            String str_dpr_id = item.getDPR_ID();

            //get ptw status
            queue = Volley.newRequestQueue(this);
            Function_Get_PTW_Status(str_dpr_id);

        }

    }


    /*
     *//********************************
     *FUNCTION LOGIN
     ********************************
     * @param str_dpr_id
     */
    private void Function_Get_PTW_Status(final String str_dpr_id) {

        System.out.println("###  Service_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + AppConfig.URL_PROJECT_UPDATE_REACHED_SITE);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_PROJECT_UPDATE_REACHED_SITE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("### Service_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        String str_ptw_status = obj.getString(Activity_Project_NI_NPO_TK_In_Progress.TAG_PTW_STATUS);


                        editor.putString(TAG_DPR_ID, str_dpr_id);
                        editor.commit();

                        Integer int_notification_id  = sharedPreferences.getInt(Config.STR_NOTIFICATION_ID, 0);

                        int_notification_id++;

                        System.out.println("### int_notification_id "+int_notification_id);

                        editor.putInt(Config.STR_NOTIFICATION_ID, int_notification_id);
                        editor.commit();

                        Intent intent = new Intent(Service_PTW_Status.this, Activity_Project_NI_NPO_TK_In_Progress.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingInten_notification = PendingIntent.getActivity(Service_PTW_Status.this, 0, intent, 0);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(Service_PTW_Status.this, "1")
                                .setSmallIcon(R.mipmap.ic_iws)
                                .setContentTitle("PTW Status Accepted,")
                                .setContentText("Your Received a PTW Status As Accepted. Click To Proceed.")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingInten_notification)
                                .setAutoCancel(true);

                        createNotificationChannel();

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_PTW_Status.this);

                        notificationManager.notify(int_notification_id, builder.build());

                        if (str_ptw_status.equals("Accepted")){

                            /*
                            Integer int_dpr_id = Integer.parseInt(str_dpr_id);

                            Intent alarmIntent = new Intent(Service_PTW_Status.this, BroadcastReceiver_Alarm_Receiver_PTW_Status.class);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(Service_PTW_Status.this, int_dpr_id, alarmIntent, 0);

                            AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            alarmMgr.cancel(pendingIntent);


                            editor.putString(TAG_DPR_ID, str_dpr_id);
                            editor.commit();

                            Integer int_notification_id  = sharedPreferences.getInt(Config.STR_NOTIFICATION_ID, 0);

                            int_notification_id++;

                            editor.putInt(Config.STR_NOTIFICATION_ID, int_notification_id);
                            editor.commit();

                            Intent intent = new Intent(Service_PTW_Status.this, Activity_Project_NI_NPO_TK_In_Progress.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            PendingIntent pendingInten_notification = PendingIntent.getActivity(Service_PTW_Status.this, 0, intent, 0);

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(Service_PTW_Status.this, "1")
                                    .setSmallIcon(R.mipmap.ic_iws)
                                    .setContentTitle("PTW Status Accepted,")
                                    .setContentText("Your Received a PTW Status As Accepted. Click To Proceed.")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    // Set the intent that will fire when the user taps the notification
                                    .setContentIntent(pendingInten_notification)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Service_PTW_Status.this);

                            notificationManager.notify(int_notification_id, builder.build());
*/

                        }

                    } else if (status == 400) {


                    } else if (status == 404) {


                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("### exception "+e.getLocalizedMessage());

                }

                stopSelf();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                stopSelf();

                System.out.println("### Service_PTW_StatusAppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse");
                if (error != null)
                    System.out.println("### Service_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE onErrorResponse " + error.getLocalizedMessage());

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("dpr_id", str_dpr_id);

                System.out.println("### Service_PTW_Status AppConfig.URL_PROJECT_UPDATE_REACHED_SITE " + "dpr_id" + " : " + str_dpr_id);

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
