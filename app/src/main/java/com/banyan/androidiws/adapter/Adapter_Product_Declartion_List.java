package com.banyan.androidiws.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;

import com.banyan.androidiws.R;
import com.banyan.androidiws.activity.Activity_Project_NI_NPO_TK_In_Progress;
import com.banyan.androidiws.activity.Activity_View_Image;
import com.banyan.androidiws.global.AppConfig;
import com.bumptech.glide.Glide;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Adapter_Product_Declartion_List extends BaseAdapter {

    public static final String TAG_LEAVE_TITLE = "leave_title";
    public static final String TAG_LEAVE_TOTAL = "leave_total";
    public static final String TAG_LEAVE_BALANCE = "leave_balance";

    private final Activity activity;

    private final SharedPreferences sharedPreferences;

    private final SharedPreferences.Editor editor;

    private ArrayList<HashMap<String, String>> data;
    private Context context;
    public String[] bgColors;

    public Adapter_Product_Declartion_List(Context context, ArrayList<HashMap<String, String>> data, Activity activity) {

        this.context = context;
        this.data = data;
        bgColors = context.getResources().getStringArray(R.array.string_array_color);
        this.activity = activity;

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
    public View getView(int position, View view, ViewGroup viewGroup) {

        try{

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_project_update_declaration_row, null);

            CardView card_view_row = (CardView) view.findViewById(R.id.card_view_row);
            LinearLayoutCompat linear_layout_image = (LinearLayoutCompat) view.findViewById(R.id.linear_layout_image);
            ImageView image_view_photo = (ImageView) view.findViewById(R.id.image_view_photo);
            TextView text_view_declaration = (TextView) view.findViewById(R.id.text_view_declaration);
            TextView text_view_comment = (TextView) view.findViewById(R.id.text_view_comment);
            CheckBox check_box_selection = (CheckBox) view.findViewById(R.id.check_box_selection);

            card_view_row.setTag(position);
            linear_layout_image.setTag(position);

            HashMap<String, String> result = new HashMap<>();
            result = data.get(position);

            String str_declaration_type = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_TYPE);
            String str_declaration = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION);
            String str_declaration_text = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_TEXT);
            String str_check_box = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_CHECK_BOX);
            String str_declaration_image = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_IMAGE);
            String str_declaration_comment = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_COMMENT);

            System.out.println("### str_declaration_image "+str_declaration_image);
            System.out.println("### str_check_box "+str_check_box);


/*
            try{

                Bitmap bitmap = decodeSampledBitmapFromResource(str_declaration_image, AppConfig.IMAGE_SIZE, AppConfig.IMAGE_SIZE);
                image_view_photo.setImageBitmap(bitmap);

            }catch (Exception e){
                System.out.println("### Exception adapter 1 "+e.getLocalizedMessage());
            }*/


            if (!str_declaration_image.isEmpty())
            Glide.with(context)
                    .load(str_declaration_image)
                    .placeholder(R.drawable.ic_galary)
                    .into(image_view_photo);



            text_view_declaration.setText(str_declaration_text);
            text_view_comment.setText(str_declaration_comment);
            if (str_check_box.isEmpty())
                check_box_selection.setChecked(false);
            else if (str_check_box.equals("1"))
                check_box_selection.setChecked(true);
            else
                check_box_selection.setChecked(false);


            linear_layout_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Integer int_position = (Integer)v.getTag();
                    HashMap<String, String> result = new HashMap<>();
                    result = data.get(int_position);

                    String str_declaration_image = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_IMAGE);

                    editor.putString(Activity_View_Image.TAG_IMAGE_URL, str_declaration_image);
                    editor.commit();

                    Intent intent = new Intent(context, Activity_View_Image.class);
                    context.startActivity(intent);

                }
            });

            card_view_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {

                        Integer int_position = (Integer)v.getTag();
                        HashMap<String, String> result = new HashMap<>();
                        result = data.get(int_position);

                        String str_declaration_type = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_TYPE);
                        String str_declaration = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION);
                        String str_declaration_text = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_TEXT);
                        String str_check_box = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_CHECK_BOX);
                        String str_declaration_image = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_IMAGE);
                        String str_declaration_comment = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_COMMENT);

                        System.out.println("### setOnClickListener");
                        System.out.println("### activity simple name "+activity.getClass().getSimpleName());
                        System.out.println("### Activity_Project_NI_NPO_TK_In_Progress.class.getSimpleName() "+Activity_Project_NI_NPO_TK_In_Progress.class.getSimpleName());

                        if (activity.getClass().getSimpleName().equals(Activity_Project_NI_NPO_TK_In_Progress.class.getSimpleName()) ){
                            ((Activity_Project_NI_NPO_TK_In_Progress)activity).Alert_PTW_Request(str_declaration_type, str_declaration, str_declaration_text,
                                    str_check_box, str_declaration_image, str_declaration_comment);
                        }

                    }catch (Exception e){
                        System.out.println("### Exception "+e.getLocalizedMessage());
                    }

                }
            });


        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }

        return view;
    }

    public boolean Function_Is_Declaration_Request_Sent(){

        boolean is_requested = false;

        for (int count = 0; count < data.size(); count++){

            HashMap<String, String> result = new HashMap<>();
            result = data.get(count);

            String str_check_box = result.get(Activity_Project_NI_NPO_TK_In_Progress.TAG_DECLARATION_CHECK_BOX);
            if (str_check_box.equals("1")) {
                is_requested = true;
                break;
            }
        }

        return is_requested;

    }

    public  int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public  Bitmap decodeSampledBitmapFromResource(String file,
                                                         int reqWidth, int reqHeight) throws IOException {
// First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        URL url = new URL(file);

        try{



            BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

        }catch (Exception e){
            System.out.println("### Exception adpter "+e.getLocalizedMessage());
        }


        return BitmapFactory.decodeStream(url.openConnection().getInputStream(), null, options);
    }

}
