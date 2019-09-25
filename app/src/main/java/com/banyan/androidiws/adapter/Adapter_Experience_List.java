package com.banyan.androidiws.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Account_Details;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Experience_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;

    private final SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    private Context context;
    public String[] bgColors;

    public Adapter_Experience_List(Context context, ArrayList<HashMap<String,String>> data){

        this.context =context;
        this.data = data;
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
        view = inflater.inflate(R.layout.list_experience_row,null);

        TextView text_view_company_name = (TextView) view.findViewById(R.id.text_view_company_name);
        TextView text_view_designation = (TextView) view.findViewById(R.id.text_view_designation);
        TextView text_view_work_from = (TextView) view.findViewById(R.id.text_view_work_from);
        TextView text_view_work_to = (TextView) view.findViewById(R.id.text_view_work_to);
        TextView text_view_salary = (TextView) view.findViewById(R.id.text_view_salary);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        final String str_company_name = result.get(Activity_Account_Details.TAG_COMPANY_NAME);
        final String str_designation = result.get(Activity_Account_Details.TAG_DESIGNATION);
        final String str_work_from = result.get(Activity_Account_Details.TAG_WORK_FROM);
        final String str_work_to = result.get(Activity_Account_Details.TAG_WORK_TO);
        final String str_salary = result.get(Activity_Account_Details.TAG_SALARTY);

        text_view_company_name.setText(str_company_name);
        text_view_designation.setText(str_designation);
        text_view_work_from.setText(str_work_from);
        text_view_work_to.setText(str_work_to);
        text_view_salary.setText(str_salary);

        return view;
    }

}
