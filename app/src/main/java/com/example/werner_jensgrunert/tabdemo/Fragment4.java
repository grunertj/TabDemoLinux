package com.example.werner_jensgrunert.tabdemo;


import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment4 extends Fragment {

    Button button;
    TextView package_list;

    public Fragment4() {
        // Required empty public constructor
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag4, container, false);

        package_list = (TextView) view.findViewById(R.id.textView_package_names);
        package_list.setMovementMethod(new ScrollingMovementMethod());

        button = (Button) view.findViewById(R.id.button_list_packages);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_packages(v);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void list_packages(View view) {

        List<PackageInfo> packages = getActivity().getPackageManager().getInstalledPackages(0);
        List<String> packageNames = new ArrayList<String>();

        for (PackageInfo pi : packages) {
            packageNames.add(pi.packageName);
        }

        java.util.Collections.sort(packageNames);

        for(String n : packageNames) {
            package_list.append(n + "\n");
        }
    }
}
