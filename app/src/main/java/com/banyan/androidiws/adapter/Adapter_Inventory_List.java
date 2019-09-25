package com.banyan.androidiws.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Inventory;

import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Inventory_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private ArrayList<HashMap<String,String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Inventory_List(Context context, ArrayList<HashMap<String,String>> data){

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
        view = inflater.inflate(R.layout.list_inventory_row,null);

        TextView text_view_inventory_type = (TextView) view.findViewById(R.id.text_view_inventory_type);
        TextView text_view_inventory_configuration = (TextView)view.findViewById(R.id.text_view_inventory_configuration);
        TextView text_view_inventory_model = (TextView)view.findViewById(R.id.text_view_inventory_model);
        TextView text_view_serial_no = (TextView)view.findViewById(R.id.text_view_serial_no);

        HashMap<String,String> result = new HashMap<>();
        result = data.get(i);

        String str_inventory_type = result.get(Activity_Inventory.TAG_INVENTORY_TYPE);
        String str_inventory_configuration = result.get(Activity_Inventory.TAG_INVENTORY_CONFIGURATION);
        String str_inventory_model = result.get(Activity_Inventory.TAG_INVENTORY_MODEL);
        String str_serial_no = result.get(Activity_Inventory.TAG_INVENTORY_SNO);

        text_view_inventory_type.setText(str_inventory_type);
        text_view_inventory_configuration.setText(str_inventory_configuration);
        text_view_inventory_model.setText(str_inventory_model);
        text_view_serial_no.setText(str_serial_no);

        return view;
    }

}
