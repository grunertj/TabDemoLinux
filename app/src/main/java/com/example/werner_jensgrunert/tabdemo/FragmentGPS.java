package com.example.werner_jensgrunert.tabdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Werner-Jens Grunert on 2/29/2016.
 */
public class FragmentGPS extends Fragment implements LocationListener {
    static final int USER_PERMISSION_REQUEST = 10;
    static boolean jens = false;

    float [] Distance = new float[10];

    LocationManager locationManager;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;

    ImageView destination_bearing;
    ImageView current_bearing;
    ImageView differential_bearing;

    FrameLayout frameLayout;


    float redValue = 1.0f;
    float greenValue = 0.29411764705882352941f;
    float blueValue = 0.31372549019607843137f;

    public FragmentGPS() {

    }

    int getDifferentialBearing(int cb, int db) {
        return ((((cb - db) % 360) + 540) % 360) - 180;
    }

    @Override
    public void onLocationChanged(Location location) {
        int DestinationBearing;
        int CurrentBearing;
        int DifferentialBearing;

        textView1.setText("onLocationChanged: " + location.getProvider()+" jens: "+jens);

        // destination_bearing.setRotation(250);
        // current_bearing.setRotation(50);

        textView4.setText(String.format("%.8f\t%.8f",location.getLatitude(),location.getLongitude()));

        Location.distanceBetween(location.getLatitude(), location.getLongitude(), MainActivity.LatitudeDestination, MainActivity.LongitudeDestination, Distance);

        DestinationBearing = Math.round(Distance[1]);

        if (DestinationBearing < 0 ) {
            DestinationBearing = 360 + DestinationBearing;
        }

        textView3.setText(String.format("Distance: %.2f km\nDestination Bearing: %d", Distance[0] / 1000.0, DestinationBearing));
        destination_bearing.setRotation(DestinationBearing);

        if (location.hasSpeed()) {
            textView2.setText(String.format("Speed: %d km/h", Math.round(location.getSpeed()*3.6)));
        }

        if(location.hasBearing()) {
            CurrentBearing = Math.round(location.getBearing());
            if (CurrentBearing < 0 ) {
                CurrentBearing = 360 + CurrentBearing;
            }
            DifferentialBearing = getDifferentialBearing(DestinationBearing,CurrentBearing);
            //if (DifferentialBearing < 0 ) {
            //    DifferentialBearing = 360 + DifferentialBearing;
            //}
            textView3.setText(String.format("Distance: %.2f km\nDestination Bearing: %d\nCurrent Bearing: %d\nDifferential Bearing: %d", Distance[0] / 1000.0, DestinationBearing, CurrentBearing, DifferentialBearing));
            current_bearing.setRotation(CurrentBearing);
            differential_bearing.setRotation(DifferentialBearing);
            redValue = (float)(Math.abs(DifferentialBearing-180)+75)/255;
            greenValue = (float)(255-Math.abs(DifferentialBearing-180))/255;
            blueValue = (float)(Math.abs(DifferentialBearing-180)-100)/255;
        }

        float[] colorMatrix = {
                redValue, 0, 0, 0, 0,  //red
                0, greenValue, 0, 0, 0, //green
                0, 0, blueValue, 0, 0,  //blue
                0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        differential_bearing.setColorFilter(colorFilter);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        textView1.setText("onStatusChanged: "+provider+" jens: "+jens);
    }

    @Override
    public void onProviderEnabled(String provider) {
        textView1.setText("onProviderEnabled: "+provider+" jens: "+jens);
    }

    @Override
    public void onProviderDisabled(String provider) {
        textView1.setText("onProviderDisabled: "+provider+" jens: "+jens);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        frameLayout = new FrameLayout(getActivity());

        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fraggps, container, false);
        // View view = (RelativeLayout) inflater.inflate(R.layout.fraggps, container, false);


        textView1 = (TextView) view.findViewById(R.id.textViewFraggps1);
        textView2 = (TextView) view.findViewById(R.id.textViewFraggps2);
        textView3 = (TextView) view.findViewById(R.id.textViewFraggps3);
        textView4 = (TextView) view.findViewById(R.id.textViewFraggps4);

        destination_bearing = (ImageView)view.findViewById(R.id.imageViewNeedleGPS);
        current_bearing = (ImageView)view.findViewById(R.id.imageViewNeedleDestGPS);
        differential_bearing = (ImageView)view.findViewById(R.id.imageViewNeedleDiffGPS);

        frameLayout.addView(view);
        return frameLayout;
        // return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        textView1.setText("onActivityCreated "+jens);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        frameLayout.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.fraggps, null);

        frameLayout.addView(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.INTERNET},
                        USER_PERMISSION_REQUEST);
                return;
            } else {
                jens = true;
            }
            if(jens) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, this);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            }
        } else if (isResumed()) {
            if (jens) {
                locationManager.removeUpdates(this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.INTERNET},
                    USER_PERMISSION_REQUEST);
            return;
        } else {
            jens = true;
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case USER_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Status.append("Permission granted");
                    jens = true;
                } else {
                    //Status.append("Permission denied");
                }
                return;
            }
        }
    }
}
