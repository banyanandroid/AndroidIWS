package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Attendance_Report_For_Dates;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Adapter_Attendance_Report extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Attendance_Report(Context context, ArrayList<HashMap<String,String>> data){

        this.context =context;
        this.data = data;
        bgColors = context.getResources().getStringArray(R.array.string_array_color);
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
        view = inflater.inflate(R.layout.list_attendance_report_row,null);

        TextView text_date = (TextView) view.findViewById(R.id.text_date);
        TextView text_present_absent = (TextView)view.findViewById(R.id.text_present_absent);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_leave_date = result.get(Activity_Attendance_Report_For_Dates.TAG_ATTENDANCE_DATE);
        String str_leave_status = result.get(Activity_Attendance_Report_For_Dates.TAG_ATTENDANCE_STATUS);

        System.out.println("### getView str_leave_date "+str_leave_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date my_leave_Date = null;
        try {

            my_leave_Date = dateFormat.parse(str_leave_date);

            String format_day = new SimpleDateFormat("dd-MM-yyyy").format(my_leave_Date.getTime());

            text_date.setText(format_day);
            text_present_absent.setText(str_leave_status);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (str_leave_status.equals("1")){ // present
            text_present_absent.setText("Present");
//            text_present_absent.setBackground(context.getResources().getDrawable(R.drawable.bg_button_green));

        }else if (str_leave_status.equals("2")){ // bench
            text_present_absent.setText("Bench");
//            text_present_absent.setBackground(context.getResources().getDrawable(R.drawable.bg_button_orange));

        }else if (str_leave_status.equals("3")){ //training
            text_present_absent.setText("Training");
//            text_present_absent.setBackground(context.getResources().getDrawable(R.drawable.bg_button_purple));

        }else if (str_leave_status.equals("4")){ // leave
            text_present_absent.setText("Approved Leave");
//            text_present_absent.setBackground(context.getResources().getDrawable(R.drawable.bg_button_red));

        }else if (str_leave_status.equals("5")){ // lop
            text_present_absent.setText("LOP");
//            text_present_absent.setBackground(context.getResources().getDrawable(R.drawable.bg_button_grey));

        }

        return view;
    }

}
