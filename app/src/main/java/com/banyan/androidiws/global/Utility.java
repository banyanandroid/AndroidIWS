package com.banyan.androidiws.global;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Web_View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utility {


	public void dateToString() throws ParseException {
		
		String  str_selected_from_date = "";
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date_selected_date = format.parse(str_selected_from_date);
		String format_date = new SimpleDateFormat("d, MMM, yyyy").format(date_selected_date.getTime());
										
	}

    public void Function_Error_Dialog(final Activity activity){

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog2);
        dialog.setCancelable(false);
        TextView text=(TextView)dialog.findViewById(R.id.text);
        text.setText("Oops! Something Went Wrong, Please Try Again.");
        Button ok=(Button)dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.finishAffinity();

            }
        });
       dialog.show();

    }


    public String getAddress(double lat, double lng, Context  context) {
        String add = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();


            Log.v("IGA", "Address" + add);

            System.out.println("### address "+add);

            //add = obj.getLocality();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return add;
    }

    public String Function_Date_Formate(String strDate, String strFromDateFormate, String strToDateFormate){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                strFromDateFormate);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(strToDateFormate);
        String finalDate = timeFormat.format(myDate);

        return finalDate;

    }


    public boolean  IsNetworkAvailable(final Context context) {
        System.out.println("### IsNetworkAvailable ");
        boolean networkAvailable = false;
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (connectivityManager != null && networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                networkAvailable = true;
            } else {
                networkAvailable = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("###IsNetworkAvailable Exception "+e.getLocalizedMessage());
        }

        return  networkAvailable;
    }

    public void Function_No_Internet_Dialog(final Activity activity) {

	    if (!IsNetworkAvailable(activity)){

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog2);
            dialog.setCancelable(false);
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Network Is Not Available");
            Button ok = (Button) dialog.findViewById(R.id.ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    activity.finishAffinity();

                }
            });
            dialog.show();

        }


    }

    public  boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

   /* public  boolean IsNetworkAvailable(final Context context) {
        boolean networkAvailable = false;
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (connectivityManager != null && networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                networkAvailable = true;
            } else {
                networkAvailable = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return networkAvailable;
    }*/

    public void Function_Show_Not_Network_Message(final Activity activity){

        new AlertDialog.Builder(activity)
                .setTitle("No Internet Connection")
                .setMessage("Internet Connection is Not Available.")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        activity.finishAffinity();

                    }
                }).show();

    }
    public String getCountOfDays(String createdDateString, String expireDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(createdDateString);
            expireCovertedDate = dateFormat.parse(expireDateString);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }

    /*Calendar todayCal = Calendar.getInstance();
    int todayYear = todayCal.get(Calendar.YEAR);
    int today = todayCal.get(Calendar.MONTH);
    int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);
    */

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);
        dayCount++;  // add one day because day start with 00 hr
        return ("" + (int) dayCount );
    }

    //go to new activity
    // overridePendingTransition(R.anim.slide_leave, R.anim.slide_enter);

    //go back activity
    //overridePendingTransition(R.anim.slide_enter,R.anim.slide_leave);

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }


   /* public String Function_Date_Formate(String strDate, String strFromDateFormate, String strToDateFormate){

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                strFromDateFormate);

        Date myDate = null;
        try {
            myDate = dateFormat.parse(strDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat(strToDateFormate);
        String finalDate = timeFormat.format(myDate);

        return finalDate;

    }*/

    public  boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    public String Function_BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,0, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }


    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap Function_StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    public String Function_Month_String_To_Number (String str_month){
        switch(str_month) {
            case "January":
            case "january":
            case "jan":
                str_month = "01";
                break;

            case "Febuary":
            case "febuary":
            case "feb":
                str_month = "02";
                break;

            case "March":
            case "march":
            case "mar":
                str_month = "03";
                break;

            case "April":
            case "april":
            case "apr":
                str_month = "04";
                break;

            case "May":
            case "may":
                str_month = "05";
                break;

            case "June":
            case "june":
            case "jun":
                str_month = "06";
                break;

            case "July":
            case "july":
            case "jul":
                str_month = "07";
                break;

            case "August":
            case "august":
            case "aug":
                str_month = "08";
                break;

            case "September":
            case "september":
            case "sep":
            case "sept":
                str_month = "09";
                break;

            case "October":
            case "october":
            case "oct":
                str_month = "10";
                break;

            case "November":
            case "november":
            case "nov":
                str_month = "11";
                break;

            case "December":
            case "december":
            case "dec":
                str_month = "12";
                break;
        }

        return str_month;
    }
}
