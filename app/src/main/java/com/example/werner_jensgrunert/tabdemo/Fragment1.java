// https://www.youtube.com/watch?v=vyykjIPNBXY
package com.example.werner_jensgrunert.tabdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Werner-Jens Grunert on 2/5/2016.
 */
public class Fragment1 extends Fragment {
    int jens = 0;
    EditText latitude_destination;
    EditText longitude_destination;
    TextView textViewIntent;
    Button button;
    Button button_coordinates;

    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag1, container, false);

        latitude_destination = (EditText) view.findViewById(R.id.textViewLatitudeDestination);
        longitude_destination = (EditText) view.findViewById(R.id.textViewLongtitudeDestination);
        textViewIntent = (TextView) view.findViewById(R.id.textViewIntent);
        textViewIntent.setText(MainActivity.intent_string);

        latitude_destination.setText(String.format("%.6f",MainActivity.LatitudeDestination));
        longitude_destination.setText(String.format("%.6f", MainActivity.LongitudeDestination));

        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                osmand(v);
            }
        });

        button_coordinates = (Button) view.findViewById(R.id.button_coordinates);
        button_coordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coordinates(v);
            }
        });

        return view;
    }

   public boolean isPackageExisted(String targetPackage) {
        PackageManager pm = getActivity().getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

   public void osmand(View view) {
        if (isPackageExisted("net.osmand.plus")) {
            Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage("net.osmand.plus");
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(launchIntent);
            getActivity().finish();
        }
   }

    public void coordinates(View view) {
        MainActivity.LatitudeDestination = Float.valueOf(latitude_destination.getText().toString());
        MainActivity.LongitudeDestination = Float.valueOf(longitude_destination.getText().toString());
        sharedPreferences = getActivity().getSharedPreferences("TestPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("test", String.format("New,%.8f,%.8f", MainActivity.LatitudeDestination, MainActivity.LongitudeDestination)).apply();

        hideSoftKeyboard(latitude_destination, longitude_destination);
    }

    // stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
    protected void hideSoftKeyboard(EditText top, EditText bottom) {
        top.setInputType(1);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(top.getWindowToken(), 0);
        bottom.setInputType(1);
        imm.hideSoftInputFromWindow(bottom.getWindowToken(), 0);
    }


}
