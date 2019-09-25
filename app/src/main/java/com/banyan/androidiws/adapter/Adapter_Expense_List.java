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


public class Adapter_Expense_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private final SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    private ArrayList<HashMap<String,String>> data;

    private final String str_user_id;

    private Context context;
    public String[] bgColors;

    private String str_download_url = "", str_file_name = "";

    public Adapter_Expense_List(Context context, ArrayList<HashMap<String, String>> data, String str_user_id){

        this.context =context;
        this.data = data;
        bgColors = context.getResources().getStringArray(R.array.string_array_color);
        this.str_user_id = str_user_id;

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
    public View getView(int position, View view, ViewGroup viewGroup) {

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_expense_row,null);

        TextView text_view_month = (TextView) view.findViewById(R.id.text_view_month);
        TextView text_view_expense = (TextView)view.findViewById(R.id.text_view_expense);
        TextView text_view_approved = (TextView)view.findViewById(R.id.text_view_approved);
        TextView text_view_paid = (TextView)view.findViewById(R.id.text_view_paid);
        TextView text_view_bill_status = (TextView)view.findViewById(R.id.text_view_bill_status);
        TextView text_view_pending = (TextView)view.findViewById(R.id.text_view_pending);
        AppCompatButton button_download = (AppCompatButton)view.findViewById(R.id.button_download);

        button_download.setTag(position);
        HashMap<String,String> result = new HashMap<>();
        result = data.get(position);

        String str_month = result.get(Activity_Expense.TAG_MONTH);
        String str_expense = result.get(Activity_Expense.TAG_EXPENSE);
        String str_approved = result.get(Activity_Expense.TAG_APPROVED);
        String str_paid = result.get(Activity_Expense.TAG_PAID);
        String str_bill_status = result.get(Activity_Expense.TAG_BILL_STATUS);
        Integer int_expense = Integer.parseInt(str_expense);
        Integer int_paid = Integer.parseInt(str_paid);
        Integer int_pending = int_expense - int_paid;

        text_view_month.setText(str_month);
        text_view_expense.setText(str_expense);
        text_view_approved.setText(str_approved);
        text_view_paid.setText(str_paid);
        text_view_bill_status.setText(str_bill_status);
        text_view_pending.setText(""+int_pending);


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
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/1/"+array_month[1];
                        str_file_name = "Expense"+"-1"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Feb")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/2/"+array_month[1];
                        str_file_name = "Expense"+"-2"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Mar")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/3/"+array_month[1];
                        str_file_name = "Expense"+"-3"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Apr")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/4/"+array_month[1];
                        str_file_name = "Expense"+"-4"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("May")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/5/"+array_month[1];
                        str_file_name = "Expense"+"-5"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Jun")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/6/"+array_month[1];
                        str_file_name = "Expense"+"-6"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Jul")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/7/"+array_month[1];
                        str_file_name = "Expense"+"-7"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Aug")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/8/"+array_month[1];
                        str_file_name = "Expense"+"-8"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Sep")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/9/"+array_month[1];
                        str_file_name = "Expense"+"-9"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Oct")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/10/"+array_month[1];
                        str_file_name = "Expense"+"-10"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Nov")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/11/"+array_month[1];
                        str_file_name = "Expense"+"-11"+"-"+array_month[1]+".xls";
                    }else if (array_month[0].equals("Dec")){
                        str_download_url = AppConfig.URL_EXPENSE_DOWNLOAD + str_user_id+"/12/"+array_month[1];
                        str_file_name = "Expense"+"-12"+"-"+array_month[1]+".xls";
                    }

                    //str_download_url = "http://epictech.in/iwsnew/Cron/expense_download/463/4/2019";

                    System.out.println("### recevied str_download_url "+str_download_url);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(str_download_url));
                    context.startActivity(browserIntent);

                     /*//
                    str_download_url = "http://s3.amazonaws.com/dynamiccatholic.com/Best+Lent+Ever+2016/BLE+2017/Parish+Resources/RH_StudyGuide_V2.pdf";
                    str_file_name = "RH_StudyGuide_V2.pdf";
                    System.out.println("### str_download_url "+str_download_url);*/
//                    new Adapter_Expense_List.DownloadFile().execute(str_download_url);

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
                folder = Environment.getExternalStorageDirectory() + File.separator + "IWS/Expense/";

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
