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


public class Adapter_Children_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;

    private final SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    private Context context;
    public String[] bgColors;

    public Adapter_Children_List(Context context, ArrayList<HashMap<String,String>> data){

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
        view = inflater.inflate(R.layout.list_children_row,null);

        TextView text_view_children_name = (TextView) view.findViewById(R.id.text_view_children_name);
        TextView text_view_dob = (TextView) view.findViewById(R.id.text_view_dob);


        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        final String str_name = result.get(Activity_Account_Details.TAG_CHILDREN_NAME);
        final String str_dob = result.get(Activity_Account_Details.TAG_CHILDREN_DOB);

        text_view_children_name.setText(str_name);
        text_view_dob.setText(str_dob);

        return view;
    }

}
