// http://blog.wittchen.biz.pl/how-to-change-fragment-layout-on-orientation-change/
// http://www.vogella.com/tutorials/AndroidFragments/article.html
// http://www.vogella.com/tutorials/AndroidSensor/article.html
// https://www.built.io/blog/2013/05/applying-low-pass-filter-to-android-sensors-readings/
// https://www.codeofaninja.com/2013/08/android-compass-code-example.html
// http://stackoverflow.com/questions/7978618/rotating-an-imageview-like-a-compass-with-the-north-pole-set-elsewhere
// http://stackoverflow.com/questions/7978618/rotating-an-imageview-like-a-compass-with-the-north-pole-set-elsewhere
// http://stackoverflow.com/questions/17979238/android-getorientation-azimuth-gets-polluted-when-phone-is-tilted
// http://www.journal.deviantdev.com/android-compass-azimuth-calculating/
// https://www.reddit.com/r/androiddev/comments/1av1la/
// http://www.kircherelectronics.com/blog/index.php/11-android/sensors/15-android-gyroscope-basics
// http://android-er.blogspot.it/2015/10/android-example-code-using-colorfilter.html


package com.example.werner_jensgrunert.tabdemo;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment5 extends Fragment {
    FrameLayout frameLayout;
    SeekBar seekBar;
    ImageView red;
    ImageView yellow;
    ImageView green;
    ImageView cyan;
    TextView angle;
    TextView v0;
    TextView v1;
    TextView v2;
    SensorManager sensorManager;
    Sensor sensor;
    float maximum_range = 0.0f;
    float resolution = 0.0f;
    int orientation = 0;

    String accuracy_names [] = {"SENSOR_STATUS_NO_CONTACT", "SENSOR_STATUS_UNRELIABLE",
    "SENSOR_STATUS_ACCURACY_LOW","SENSOR_STATUS_ACCURACY_MEDIUM","SENSOR_STATUS_ACCURACY_HIGH"};

    public Fragment5() {
        setRetainInstance(true);

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        frameLayout = new FrameLayout(getActivity());

        orientation = getActivity().getWindowManager().getDefaultDisplay().getRotation();

        // LayoutInflater inflater = (LayoutInflater.from(getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)));
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag5, container, false);

        frameLayout.addView(view);

        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        yellow = (ImageView)view.findViewById(R.id.imageViewYellow);
        red = (ImageView)view.findViewById(R.id.imageViewRed);
        green = (ImageView)view.findViewById(R.id.imageViewGreen);
        cyan = (ImageView)view.findViewById(R.id.imageViewCyan);
        angle = (TextView)view.findViewById(R.id.textViewAngle);
        v0 = (TextView)view.findViewById(R.id.textViewValue0);
        v1 = (TextView)view.findViewById(R.id.textViewValue1);
        v2 = (TextView)view.findViewById(R.id.textViewValue2);


        return frameLayout;
    }

    private double mod(double a, double b) {
        return a % b;
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        private final static double PI = Math.PI;
        private final static double TWO_PI = PI*2;
        float[] values;
        float[] inrotationMatrix;
        float[] rotationMatrix;
        float[] orientationValues;
        float heading;
        double azimuth;
        double currentAzimuth;
        int accuracy = SensorManager.SENSOR_STATUS_ACCURACY_HIGH;

        @Override
        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ROTATION_VECTOR:
                    inrotationMatrix = new float[16];
                    rotationMatrix = new float[16];
                    orientationValues = new float[3];
                    SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                    // SensorManager.remapCoordinateSystem(inrotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);
                    SensorManager.getOrientation(rotationMatrix, orientationValues);
                    azimuth = Math.toDegrees(orientationValues[0]);
		    //  float azimuthInDegress = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
                    if (azimuth < 0) {
                        azimuth = 360 + azimuth;
                    }

                    if (accuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
                        v0.setText(String.format("Azimuth: %.3f %d", azimuth, orientation));
                        red.setRotation((360 - (float) azimuth) - orientation * 90);
                    }

                    RotateAnimation ra = new RotateAnimation((float)currentAzimuth,
                            (float)-azimuth,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    ra.setDuration(210);
                    green.startAnimation(ra);
                    currentAzimuth = - azimuth;

                    // heading = (float) mod(orientationValues[0] + TWO_PI, TWO_PI);
                    // v2.setText(String.format("Heading: %.3f",heading));

                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            v2.setText(String.format("%s", accuracy_names[accuracy + 1]));
            this.accuracy = accuracy;

        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            float redValue = 255;
            float greenValue = 255;
            float blueValue = 255;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                yellow.setRotation(progress - 180);
                cyan.setRotation(progress);
                v1.setText(String.format("SeekBar: %d", Math.abs(progress - 180)));

                //redValue = (float)(Math.abs(progress-180)+75)/255;
                redValue = (float)(Math.abs(progress-180)+75)/255;
                greenValue = (float)(255-Math.abs(progress-180))/255;
                blueValue = (float)(Math.abs(progress-180)-100)/255;

                float[] colorMatrix = {
                        redValue, 0, 0, 0, 0,  //red
                        0, greenValue, 0, 0, 0, //green
                        0, 0, blueValue, 0, 0,  //blue
                        0, 0, 0, 1, 0    //alpha
                };

                ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
                yellow.setColorFilter(colorFilter);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        angle.setText(sensor.getName() + "\n" + sensor.getVendor());

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        frameLayout.removeAllViews();
        LayoutInflater inflater =  (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag5, null);

        frameLayout.addView(view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        TextView textViewMain;

        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()) {
            textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
            textViewMain.setText("setUserVisibleHint Fragment5");
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else if (isResumed()) {
            textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
            textViewMain.setText("setUserVisibleHint Not Fragment5");
            sensorManager.unregisterListener(sensorEventListener);
        }
    }
}
