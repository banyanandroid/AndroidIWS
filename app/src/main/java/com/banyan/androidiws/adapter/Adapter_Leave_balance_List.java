package com.banyan.androidiws.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.fragment.Fragment_Leave_List;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_Leave_balance_List extends RecyclerView.Adapter<Adapter_Leave_balance_List.ViewHolder> {

    private final ArrayList<HashMap<String, String>> data;
    private final Context context;
    private int[] array_color = {R.color.color_gray_attract, R.color.color_social_attract,
            R.color.color_wood_attract, R.color.color_rosana_attract
    };

    public Adapter_Leave_balance_List(Context context,ArrayList<HashMap<String, String>> data){
        this.context = context;
        this.data = data;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_leave_balance_row, parent, false);
        return new ViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
                HashMap<String, String> result = data.get(position);
        result = data.get(position);

        String str_leave_title = result.get(Fragment_Leave_List.TAG_LEAVE_BALANCE_TITLE);
        String str_leave_balance = result.get(Fragment_Leave_List.TAG_LEAVE_BALANCE_BALANCE);
        String str_leave_total = result.get(Fragment_Leave_List.TAG_LEAVE_BALANCE_TOTAL);

        viewHolder.text_leave_balance.setText(str_leave_balance);
        viewHolder.text_title.setText(str_leave_title);
        viewHolder.text_leave_total.setText(str_leave_total +" Leaves");

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_leave_request;
        TextView text_leave_balance, text_title, text_leave_total;

        public ViewHolder(@NonNull View view) {
            super(view);
            layout_leave_request = (LinearLayout) view.findViewById(R.id.layout_leave_request);
             text_leave_balance = (TextView) view.findViewById(R.id.text_leave_balance);
             text_title = (TextView)view.findViewById(R.id.text_title);
             text_leave_total = (TextView)view.findViewById(R.id.text_leave_total);
        }
    }
}
