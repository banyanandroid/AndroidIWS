package com.banyan.androidiws.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Expense;
import com.banyan.androidiws.activity.Activity_Productivity_NI_NPO_TK;
import com.banyan.androidiws.activity.Activity_Web_View;
import com.banyan.androidiws.global.AppConfig;
import com.banyan.androidiws.global.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Productivity_NI_NPO_TK_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;

    private Context context;

    private final SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    public String[] bgColors;

    private String str_download_url, str_file_name, str_user_id;



    public Adapter_Productivity_NI_NPO_TK_List(Context context, ArrayList<HashMap<String,String>> data, String str_user_id){

        this.context =context;

        this.data = data;

        this.str_user_id = str_user_id;

        bgColors = context.getResources().getStringArray(R.array.string_array_color);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_production_ni_npo_tk_row,null);


        /***************************
        *  FIND VIEW BY ID
        ****************************/

        TextView text_view_month = (TextView) view.findViewById(R.id.text_view_month);
        TextView text_view_ni_wip = (TextView)view.findViewById(R.id.text_view_ni_wip);
        TextView text_view_ni_completed = (TextView)view.findViewById(R.id.text_view_ni_completed);
        TextView text_view_npo_wip = (TextView)view.findViewById(R.id.text_view_npo_wip);
        TextView text_view_npo_completed = (TextView)view.findViewById(R.id.text_view_npo_completed);
        TextView text_view_total_wip = (TextView)view.findViewById(R.id.text_view_total_wip);
        TextView text_view_total_completed = (TextView)view.findViewById(R.id.text_view_total_completed);
        TextView text_view_productivity = (TextView)view.findViewById(R.id.text_view_productivity);
        TextView text_view_rating = (TextView)view.findViewById(R.id.text_view_rating);

        AppCompatButton button_download = (AppCompatButton)view.findViewById(R.id.button_download);

        button_download.setTag(i);

        /***************************
         *  GET DATA
         ****************************/

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_month = result.get(Activity_Productivity_NI_NPO_TK.TAG_MONTH);
        String str_ni_wip = result.get(Activity_Productivity_NI_NPO_TK.TAG_NI_WIP);
        String str_ni_completed = result.get(Activity_Productivity_NI_NPO_TK.TAG_NI_COMPLETED);
        String str_npo_wip = result.get(Activity_Productivity_NI_NPO_TK.TAG_NPO_WIP);
        String str_npo_completed = result.get(Activity_Productivity_NI_NPO_TK.TAG_NPO_COMPLETED);
        String str_total_wip = result.get(Activity_Productivity_NI_NPO_TK.TAG_TOTAL_WIP);
        String str_total_completed = result.get(Activity_Productivity_NI_NPO_TK.TAG_TOTAL_COMPLETED);
        String str_productivity = result.get(Activity_Productivity_NI_NPO_TK.TAG_PRODUCTIVIY);
        String str_rating = result.get(Activity_Productivity_NI_NPO_TK.TAG_RATING);

        if (str_rating.equals("Very Poor")){
            text_view_rating.setBackground(context.getResources().getDrawable(R.drawable.bg_border_curved_red));
        }else if (str_rating.equals("Poor")){
            text_view_rating.setBackground(context.getResources().getDrawable(R.drawable.bg_border_curved_gray));
        }else if (str_rating.equals("Average")){
            text_view_rating.setBackground(context.getResources().getDrawable(R.drawable.bg_border_curved_yellow));
        }else if (str_rating.equals("Good")){
            text_view_rating.setBackground(context.getResources().getDrawable(R.drawable.bg_border_curved_green));
        }

        text_view_month.setText(str_month);
        text_view_ni_wip.setText(str_ni_wip);
        text_view_ni_completed.setText(str_ni_completed);
        text_view_npo_wip.setText(str_npo_wip);
        text_view_npo_completed.setText(str_npo_completed);
        text_view_total_wip.setText(str_total_wip);
        text_view_total_completed.setText(str_total_completed);
        text_view_productivity.setText(str_productivity);
        text_view_rating.setText(str_rating);

        /***************************
         *  ACTION
         ****************************/

        button_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if SD card is present or not
                if (new Utility().isSDCardPresent()) {

                    Integer int_position = (Integer)v.getTag();

                    HashMap<String,String> result = new HashMap<>();
                    result = data.get(int_position);

                    String str_month = result.get(Activity_Expense.TAG_MONTH);

                    String[] array_month = str_month.split("-");

                    if (array_month[0].equals("Jan")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/1/"+array_month[1];
                        str_file_name = "Productivity"+"-1"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Feb")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/2/"+array_month[1];
                        str_file_name = "Productivity"+"-2"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Mar")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/3/"+array_month[1];
                        str_file_name = "Productivity"+"-3"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Apr")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/4/"+array_month[1];
                        str_file_name = "Productivity"+"-4"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("May")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/5/"+array_month[1];
                        str_file_name = "Productivity"+"-5"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Jun")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/6/"+array_month[1];
                        str_file_name = "Productivity"+"-6"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Jul")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/7/"+array_month[1];
                        str_file_name = "Productivity"+"-7"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Aug")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/8/"+array_month[1];
                        str_file_name = "Productivity"+"-8"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Sep")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/9/"+array_month[1];
                        str_file_name = "Productivity"+"-9"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Oct")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/10/"+array_month[1];
                        str_file_name = "Productivity"+"-10"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Nov")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/11/"+array_month[1];
                        str_file_name = "Productivity"+"-11"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Dec")){
                        str_download_url = AppConfig.URL_PRODUCTIVITY_DOWNLOAD + str_user_id+"/12/"+array_month[1];
                        str_file_name = "Productivity"+"-12"+"-"+array_month[1]+".xls";
                    }


                //    str_download_url = "http://epictech.in/iwsnew/cron/productivity_download/382/5/2019";

                    System.out.println("### recevied str_download_url "+str_download_url);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_download_url));
                    context.startActivity(browserIntent);

                    /*//
                    str_download_url = "http://s3.amazonaws.com/dynamiccatholic.com/Best+Lent+Ever+2016/BLE+2017/Parish+Resources/RH_StudyGuide_V2.pdf";
                    str_file_name = "RH_StudyGuide_V2.pdf";
                    System.out.println("### str_download_url "+str_download_url);*/

                    //new Adapter_Productivity_NI_NPO_TK_List.DownloadFile().execute(str_download_url);

                } else {
                    Toast.makeText(context,"SD Card not found", Toast.LENGTH_LONG).show();
                }

            }
        });


        return view;
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(context);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10000);


                //Extract file name from URL
                fileName = str_file_name;

                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "IWS/Productivity/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[10000];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d("TAG", "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            // Display File path after downloading
            Toast.makeText(context,
                    message, Toast.LENGTH_LONG).show();
        }
    }

}
