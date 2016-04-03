// http://tamsler.blogspot.it/2011/11/android-viewpager-and-fragments-part-ii.html
// http://stackoverflow.com/questions/9798392/imageview-have-height-match-width
// http://stackoverflow.com/questions/17979238/android-getorientation-azimuth-gets-polluted-when-phone-is-tilted

package com.example.werner_jensgrunert.tabdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Werner-Jens Grunert on 2/5/2016.
 */
public class Fragment3 extends Fragment {
    TextView textView1;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    ImageView needle;

    FrameLayout frameLayout;

    String accuracy_names [] = {"SENSOR_STATUS_NO_CONTACT", "SENSOR_STATUS_UNRELIABLE",
            "SENSOR_STATUS_ACCURACY_LOW","SENSOR_STATUS_ACCURACY_MEDIUM","SENSOR_STATUS_ACCURACY_HIGH"};

    int mDisplayOrientation = 0;

    SensorManager sensorManager;
    Sensor sensor;
    Sensor accelerometer;


    private SensorEventListener sensorEventListener = new SensorEventListener() {
        float[] rotationMatrix;
        float[] rotationMatrixOut;
        float[] orientationValues;
        double azimuth;

        public static final float TWENTY_FIVE_DEGREE_IN_RADIAN = 0.436332313f;
        public static final float ONE_FIFTY_FIVE_DEGREE_IN_RADIAN = 2.7052603f;

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    rotationMatrix = new float[16];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                    rotationMatrixOut = new float[16];
                    SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrixOut);

                    // inclination is the degree of tilt by the device independent of orientation (portrait or landscape)
                    // if less than 25 or more than 155 degrees the device is considered lying flat
                    //float inclination = (float) Math.acos(rotationMatrix[8]);

                   // if (inclination < TWENTY_FIVE_DEGREE_IN_RADIAN || inclination > ONE_FIFTY_FIVE_DEGREE_IN_RADIAN) {
                        // device is flat just call getOrientation
                   //     textView4.setText(String.format("Flat: %.3f", inclination));

                    //} else {
                        // call remap
                    //    textView4.setText(String.format("Inclined: %.3f", inclination));
                    //}

                    orientationValues = new float[3];
                    SensorManager.getOrientation(rotationMatrixOut, orientationValues);
                    azimuth = Math.toDegrees(orientationValues[0]);

                    if (azimuth < 0) {
                        azimuth = 360 + azimuth;
                    }

                    textView3.setText(String.format("Azimuth: %.3f %d", azimuth, mDisplayOrientation));
                    needle.setRotation((360 - (float) azimuth) - mDisplayOrientation * 90);

                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    float valueAzimuth = event.values[0];
                    float valuePitch = event.values[1];
                    float valueRoll = event.values[2];
                    textView4.setText(String.format("Azimuth %d Pitch: %d Roll: %d", Math.round(valueAzimuth),Math.round(valuePitch),Math.round(valueRoll)));

                    break;
                default:
                    break;
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            textView2.setText(String.format("%s", accuracy_names[accuracy + 1]));
        }
    };

    public Fragment3() {
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag3, container, false);

        frameLayout = new FrameLayout(getActivity());
        frameLayout.removeAllViews();

        mDisplayOrientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
        textView1 = (TextView)view.findViewById(R.id.textViewFrag31);
        textView2 = (TextView)view.findViewById(R.id.textViewFrag32);
        textView3 = (TextView)view.findViewById(R.id.textViewFrag33);
        textView4 = (TextView)view.findViewById(R.id.textViewFrag34);
        needle = (ImageView)view.findViewById(R.id.imageViewNeedle);

        // return view;
        frameLayout.addView(view);
        return frameLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textView1.setText(sensor.getName() + "\n" + sensor.getVendor());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        frameLayout.removeAllViews();

        LayoutInflater inflater =  (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag3, null);

        frameLayout.addView(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()) {
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
            sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (isResumed()) {
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
