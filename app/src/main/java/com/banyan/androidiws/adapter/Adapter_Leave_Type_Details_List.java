package com.banyan.androidiws.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Add_Leave;
import com.banyan.androidiws.activity.Activity_Leave_Details;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_Leave_Type_Details_List extends RecyclerView.Adapter<Adapter_Leave_Type_Details_List.ViewHolder>  {

    private final ArrayList<HashMap<String, String>> data;
    private final Context context;
    int row_index = -1;

    public Adapter_Leave_Type_Details_List(Context context, ArrayList<HashMap<String, String>> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_type_row, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        HashMap<String, String> result = data.get(position);

        String str_leave_name = result.get(Activity_Leave_Details.TAG_LEAVE_NAME);
        String str_leave_id = result.get(Activity_Leave_Details.TAG_LEAVE_ID);
        String str_leave_selected = result.get(Activity_Leave_Details.TAG_LEAVE_SELECTED);

        holder.text_leave_name.setText(str_leave_name);

        if (str_leave_selected.equals("YES"))
            row_index = position;


        if(row_index==position){
            holder.layout_leave_name.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.text_leave_name.setTextColor(Color.WHITE);

            Activity_Add_Leave.str_selected_leave_type_id = str_leave_id;
        }
        else
        {

            holder.layout_leave_name.setBackgroundColor(Color.WHITE);
            holder.text_leave_name.setTextColor(context.getResources().getColor(R.color.colorTextSecondary));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView text_leave_name;
        LinearLayout layout_leave_name;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout_leave_name = (LinearLayout)view.findViewById(R.id.layout_leave_name);
            text_leave_name = (TextView)view.findViewById(R.id.text_leave_name);
        }

    }


}
