package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.fragment.Fragment_Project_Completed_List;
import com.banyan.androidiws.fragment.Fragment_Project_In_Progress_List;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Project_List extends BaseAdapter {

    public static final String TAG_VIEW_TYPE_IN_PROGRES = "in_progress";
    public static final String TAG_VIEW_TYPE_COMPLETE = "complete";

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Project_List(Context context, ArrayList<HashMap<String, String>> data){

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
        view = inflater.inflate(R.layout.list_project_row,null);

        TextView text_view_site_name = (TextView)view.findViewById(R.id.text_view_site_name);
        TextView text_view_project = (TextView)view.findViewById(R.id.text_view_project);
        TextView text_view_category = (TextView)view.findViewById(R.id.text_view_category);
        TextView text_view_zone = (TextView)view.findViewById(R.id.text_view_zone);
        TextView text_view_circle = (TextView)view.findViewById(R.id.text_view_circle);
        TextView text_view_date = (TextView)view.findViewById(R.id.text_view_date);
        TextView text_view_wp = (TextView)view.findViewById(R.id.text_view_wp);
        TextView text_view_assigned_by = (TextView)view.findViewById(R.id.text_view_assigned_by);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_dpr_id = result.get(Fragment_Project_Completed_List.TAG_DPR_ID);
        String str_site_name = result.get(Fragment_Project_Completed_List.TAG_SITE_NAME);
        String str_project = result.get(Fragment_Project_Completed_List.TAG_PROJECT);
        String str_category = result.get(Fragment_Project_Completed_List.TAG_CATEGORY);
        String str_zone = result.get(Fragment_Project_Completed_List.TAG_ZONE);
        String str_circle = result.get(Fragment_Project_Completed_List.TAG_CIRCLE);
        String str_date = result.get(Fragment_Project_Completed_List.TAG_DATE);
        String str_wp = result.get(Fragment_Project_Completed_List.TAG_WP);
        String str_assigned_by = result.get(Fragment_Project_Completed_List.TAG_ASSIGNED_BY);

        text_view_site_name.setText(str_site_name);
        text_view_project.setText(str_project);
        text_view_category.setText(str_category);
        text_view_zone.setText(str_zone);
        text_view_circle.setText(str_circle);
        text_view_date.setText(str_date);
        text_view_wp.setText(str_wp);
        text_view_assigned_by.setText(str_assigned_by);

        return view;
    }

}
