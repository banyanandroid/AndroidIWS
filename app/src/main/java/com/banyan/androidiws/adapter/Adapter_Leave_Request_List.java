package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Leave_List;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Adapter_Leave_Request_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private static ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Leave_Request_List(Context context, ArrayList<HashMap<String,String>> data){

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
        view = inflater.inflate(R.layout.list_leave_request_row,null);

        TextView text_month = (TextView) view.findViewById(R.id.text_month);
        TextView text_date = (TextView)view.findViewById(R.id.text_date);
        TextView text_title = (TextView)view.findViewById(R.id.text_title);
        TextView text_leave_type = (TextView)view.findViewById(R.id.text_leave_type);
        TextView text_leave_status = (TextView)view.findViewById(R.id.text_leave_status);
        TextView text_no_of_leave_day = (TextView)view.findViewById(R.id.text_no_of_leave_day);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_leave_date = result.get(Activity_Leave_List.TAG_LEAVE_START_DATE);
        String str_leave_title = result.get(Activity_Leave_List.TAG_LEAVE_REQUEST_SUBJECT);
        String str_leave_type = result.get(Activity_Leave_List.TAG_LEAVE_REQUEST_TYPE_NAME);
        String str_leave_status = result.get(Activity_Leave_List.TAG_LEAVE_REQUEST_STATUS);
        String str_leave_total_dates = result.get(Activity_Leave_List.TAG_LEAVE_REQUEST_TOTAL_DAYS);

        System.out.println("### getView str_leave_date "+str_leave_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date my_leave_Date = null;
        try {

            my_leave_Date = dateFormat.parse(str_leave_date);

            String format_date = new SimpleDateFormat("d").format(my_leave_Date.getTime());
            String format_month = new SimpleDateFormat("MMM").format(my_leave_Date.getTime());

            System.out.println("### format_date "+format_date);
            System.out.println("### format_month "+format_month);

            text_month.setText(format_month);
            text_date.setText(format_date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (str_leave_status.equals("Approved")){

            text_leave_status.setBackground(context.getResources().getDrawable(R.drawable.bg_button_green));

        }else if (str_leave_status.equals("Rejected")){

            text_leave_status.setBackground(context.getResources().getDrawable(R.drawable.bg_button_red));

        }else if (str_leave_status.equals("Pending")){

            text_leave_status.setBackground(context.getResources().getDrawable(R.drawable.bg_button_grey));

        }

        text_title.setText(str_leave_title);
        text_leave_type.setText(str_leave_type);
        text_leave_status.setText(str_leave_status);
        text_no_of_leave_day.setText(str_leave_total_dates+ " Days");

        return view;
    }

}
