package com.banyan.androidiws.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.banyan.androidiws.R;
import com.banyan.androidiws.global.Session_Manager;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.Calendar;
import java.util.HashMap;

import static com.banyan.androidiws.global.Util.IsNetworkAvailable;


public class Fragment_Profile extends Fragment {


    private Session_Manager session;

    private EditText edit_date_of_join, edit_rr_date, edit_dob;

    private Button button_change_password;

    private String str_user_id, str_user_name, str_user_type, str_user_role;

    private int mYear, mMonth, mDay;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_profile, container, false);

        /*************************
        *  SESSION
        *************************/
        Function_Verify_Network_Available(getActivity());

        session = new Session_Manager(getActivity());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        str_user_id = user.get(Session_Manager.KEY_USER_ID);
        str_user_name = user.get(Session_Manager.KEY_USER_NAME);
        str_user_type = user.get(Session_Manager.KEY_USER_TYPE_ID);
        str_user_role = user.get(Session_Manager.KEY_USER_ROLE);

        /*************************
         *  FIND VIEW BY ID
         *************************/


        edit_date_of_join = (EditText)root_view.findViewById(R.id.edit_date_of_join);
        edit_rr_date = (EditText)root_view.findViewById(R.id.edit_rr_date);
        edit_dob = (EditText)root_view.findViewById(R.id.edit_dob);

        button_change_password = (Button)root_view.findViewById(R.id.button_change_password);

        edit_date_of_join.setKeyListener(null);
        edit_rr_date.setKeyListener(null);
        edit_dob.setKeyListener(null);


        /*************************
         *  ACTION
         *************************/

        edit_date_of_join.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    edit_date_of_join.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });

        edit_rr_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    edit_rr_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });

        edit_dob.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    // Get Current Date
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    edit_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

                    return true;
                }
                return false;
            }
        });


        button_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertForgetPassword();

            }
        });


        return root_view;
    }


    public void AlertForgetPassword() {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view_alert = layoutInflater.inflate(R.layout.alert_change_password, null);

        final EditText edit_old_password = (EditText) view_alert.findViewById(R.id.edit_old_password);
        final EditText edit_new_password = (EditText) view_alert.findViewById(R.id.edit_new_password);

        AlertDialog.Builder alertdialog_builder = new AlertDialog.Builder(getContext());
        alertdialog_builder.setCancelable(false);
        alertdialog_builder.setView(view_alert);
        alertdialog_builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                TastyToast.makeText(getContext(), "Password Changed Successfully", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);


/*                str_forgot_password_username = edit_username.getText().toString();

                if (str_forgot_password_username.isEmpty()){
                    TastyToast.makeText(Activity_Login.this, "Enter User Name", TastyToast.LENGTH_SHORT, TastyToast.WARNING);
                }else{

                    dialog = new SpotsDialog(Activity_Login.this);
                    dialog.show();

                    queue = Volley.newRequestQueue(Activity_Login.this);
                    Function_Forgot_Password();

                }*/

            }
        });
        alertdialog_builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertdialog_builder.create();
        alertDialog.setTitle("Forgot Password");
        alertDialog.show();

    }


    /********************************
     *FUNCTION CHANGE PASSWORD
     *********************************/
 /*   private void Function_Change_Password() {

        System.out.println("### AppConfig.URL_CHANGE_PASSWORD " + AppConfig.URL_CHANGE_PASSWORD);
        StringRequest request = new StringRequest(Request.Method.POST,
                AppConfig.URL_CHANGE_PASSWORD, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                System.out.println("###  AppConfig.URL_PROFILE : onResponse " + response);
                Log.d("TAG", "### " + response.toString());
                try {
                    JSONObject obj = new JSONObject(response);
                    int status = obj.getInt("status");
                    String msg = obj.getString("msg");
                    if (status == 200) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Password Changed Successfully.", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                    } else if(status == 400) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), "Bad Request, Try Again.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    } else if(status == 404) {

                        dialog.dismiss();
                        TastyToast.makeText(getContext(), msg, TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    }
                    dialog.dismiss();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                dialog.dismiss();

                System.out.println("### AppConfig.URL_LOGIN onErrorResponse");
                if (error != null)
                    System.out.println("### AppConfig.URL_LOGIN onErrorResponse " + error.getLocalizedMessage());

                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.app_name)
                        .setMessage("Something Went Wrong, Try Again Later.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getActivity().finishAffinity();

                            }
                        }).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(TAG_USER_ID, str_user_id);
                params.put(TAG_USER_TYPE, str_user_type);
                params.put(TAG_USER_ROLE, str_user_role);
                params.put(TAG_CHANGE_PASSWORD_OLD_PASSWORD, str_old_password);
                params.put(TAG_CHANGE_PASSWORD_NEW_PASSWORD, str_new_password);

                System.out.println("###" + TAG_USER_ID + " : " + str_user_id);
                System.out.println("###" + TAG_USER_TYPE + " : " + str_user_type);
                System.out.println("###" + TAG_USER_ROLE + " : " + str_user_role);
                System.out.println("###" + TAG_CHANGE_PASSWORD_OLD_PASSWORD + " : " + str_old_password);
                System.out.println("###" + TAG_CHANGE_PASSWORD_NEW_PASSWORD + " : " + str_new_password);

                return checkParams(params);

            }

            private Map<String, String> checkParams(Map<String, String> map) {
                Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
                    if (pairs.getValue() == null) {
                        map.put(pairs.getKey(), "");
                    }
                }
                return map;
            }
        };

        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(
                new Constants().MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }*/

    public void Function_Verify_Network_Available(Context context){
        try{
            if (!IsNetworkAvailable(context)){

                new AlertDialog.Builder(context)
                        .setTitle("No Internet Connection")
                        .setMessage("Internet Connection is Not Available.")
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getActivity().finishAffinity();

                            }
                        }).show();
            };
        }catch (Exception e){
            System.out.println("### Exception e "+e.getLocalizedMessage());
        }
    }

}
