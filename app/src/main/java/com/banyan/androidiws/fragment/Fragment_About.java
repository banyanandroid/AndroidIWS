package com.banyan.androidiws.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.banyan.androidiws.R;


public class Fragment_About extends Fragment {

    private TextView text_version;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root_view = inflater.inflate(R.layout.fragment_about, container, false);

        text_version = (TextView)root_view.findViewById(R.id.text_version);
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = pInfo.versionName;
            text_version.setText("Version : "+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return root_view;
    }

}
