package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Notification_List;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Notification_List extends BaseAdapter {

    private ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Notification_List(Context context, ArrayList<HashMap<String,String>> data){

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
        view = inflater.inflate(R.layout.list_notification_row,null);

        TextView text_view_title = (TextView) view.findViewById(R.id.text_view_title);
        TextView text_view_description = (TextView)view.findViewById(R.id.text_view_description);
        TextView text_view_category = (TextView)view.findViewById(R.id.text_view_category);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_title = result.get(Activity_Notification_List.TAG_TITLE);
        String str_message = result.get(Activity_Notification_List.TAG_DESCRIPTION);
        String str_category = result.get(Activity_Notification_List.TAG_TYPE);

        if (str_category.equals("1")){
            str_category = "Document";
        }else if (str_category.equals("2")){
            str_category = "Leave";
        }

        text_view_title.setText(str_title);
        text_view_description.setText(str_message);
        text_view_category.setText(str_category);


        return view;
    }

}
