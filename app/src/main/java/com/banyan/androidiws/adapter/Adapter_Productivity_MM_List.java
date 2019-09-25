package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Productivity_MM;
import com.banyan.androidiws.activity.Activity_Productivity_NI_NPO_TK;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Productivity_MM_List extends BaseAdapter {

    private ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Productivity_MM_List(Context context, ArrayList<HashMap<String,String>> data){

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
        view = inflater.inflate(R.layout.list_production_mm_row,null);

        TextView text_view_month = (TextView) view.findViewById(R.id.text_view_month);
        TextView text_days_worked = (TextView)view.findViewById(R.id.text_days_worked);
        TextView text_view_time_sheet_received = (TextView)view.findViewById(R.id.text_view_time_sheet_received);
        TextView text_view_productivity = (TextView)view.findViewById(R.id.text_view_productivity);
        TextView text_view_rating = (TextView)view.findViewById(R.id.text_view_rating);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_month = result.get(Activity_Productivity_MM.TAG_MONTH);
        String str_days_worked = result.get(Activity_Productivity_MM.TAG_DAYS_WORKED);
        String str_time_sheet_received = result.get(Activity_Productivity_MM.TAG_TIME_SHEET_RECEIVED);
        String str_productivity = result.get(Activity_Productivity_MM.TAG_PRODUCTIVIY);
        String str_rating = result.get(Activity_Productivity_MM.TAG_RATING);


        text_view_month.setText(str_month);
        text_days_worked.setText(str_days_worked);
        text_view_time_sheet_received.setText(str_time_sheet_received);
        text_view_productivity.setText(str_productivity);
        text_view_rating.setText(str_rating);

        return view;
    }

}
