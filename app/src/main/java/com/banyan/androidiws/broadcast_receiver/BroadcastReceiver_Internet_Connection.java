package com.banyan.androidiws.broadcast_receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.banyan.androidiws.database.DatabaseHandler;
import com.banyan.androidiws.global.Utility;
import com.banyan.androidiws.service.Service_PTW_Status;
import com.banyan.androidiws.service.Service_Upload_Project_Data;

public class BroadcastReceiver_Internet_Connection  extends BroadcastReceiver {

    private DatabaseHandler db_handle;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("### BroadcastReceiver_Internet_Connection onReceive ");

      /*  *//*************************
        *  SETUP
        **************************//*

        db_handle = new DatabaseHandler(context);

        boolean bol_is_internet_connected = new Utility().IsNetworkAvailable(context);

        if (bol_is_internet_connected){

            Integer int_reached_site_count = db_handle.Function_Get_Reached_Site_Count();
            Integer int_declaration_count = db_handle.Function_Get_Declaration_Count();
            Integer int_ptw_request_count = db_handle.Function_Get_PTW_Request_Count();
            Integer int_start_work_count = db_handle.Function_Get_Start_Work_Count();
            Integer int_ohs_work_count = db_handle.Function_Get_OHS_Work_Count();

            System.out.println("### int_reached_site_count "+int_reached_site_count);
            System.out.println("### int_declaration_count "+int_declaration_count);
            System.out.println("### int_ptw_request_count "+int_ptw_request_count);
            System.out.println("### int_start_work_count "+int_start_work_count);
            System.out.println("### int_ohs_work_count "+int_ohs_work_count);

            if (int_reached_site_count == 0 && int_declaration_count == 0 && int_ptw_request_count == 0 &&
                    int_start_work_count == 0 && int_ohs_work_count == 0){

            }else{
                // call uploading service
                Intent intent_ptw_status = new Intent(context, Service_Upload_Project_Data.class);
                context.startService(intent_ptw_status);
            }

        }
*/

    }
}
