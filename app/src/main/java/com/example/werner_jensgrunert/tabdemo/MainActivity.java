// https://www.youtube.com/watch?v=-a2jJ92bmzw
// https://www.youtube.com/watch?v=RnM8aTof9V4
// http://www.amazon.com/gp/product/1449362184
// https://www.youtube.com/watch?v=sPvCEsGm8us
// http://stackoverflow.com/questions/19873063/handler-is-abstract-cannot-be-instantiated
// http://www.mopri.de/2010/timertask-bad-do-it-the-android-way-use-a-handler/
// http://android-er.blogspot.it/2013/12/example-of-using-timer-and-timertask-on.html
// http://code.hootsuite.com/orientation-changes-on-android/
// http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html

package com.example.werner_jensgrunert.tabdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    static String intent_string = "";
    static int klaus = 10;
    static Fragment1 f1 = null;
    static Fragment2 f2 = null;
    static Fragment3 f3 = null;
    static Fragment4 f4 = null;
    static Fragment5 f5 = null;
    static FragmentGPS f6 = null;
    TabLayout tabLayout;
    ViewPager viewPager;
    TextView textview;
    TextView textview_intent;
    Handler handler;
    Runnable runnable;

    SharedPreferences sharedPreferences;

    static public double LatitudeDestination = 45.463890f;
    static public double LongitudeDestination = 9.189277f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textview = (TextView) findViewById(R.id.textView3);
        textview_intent = (TextView) findViewById(R.id.textViewtmp);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        String data = intent.getDataString();

        sharedPreferences = getSharedPreferences("TestPreferences", MODE_PRIVATE);

        if (action.equals("android.intent.action.VIEW") && data.matches("geo:.*")) {
            String s[] = data.split(":|,|\\?");
            LatitudeDestination = Float.valueOf(s[1]);
            LongitudeDestination = Float.valueOf(s[2]);
            sharedPreferences.edit().putString("test", String.format("New,%.8f,%.8f", LatitudeDestination, LongitudeDestination)).apply();
            intent_string = "Action: " + action + "\nType: " + type + "\nData: " + data;
        } else {
            intent_string = "Action: " + action + "\nType: " + type + "\nData: " + data + "\nScheme: " + intent.getScheme() + "\nContent: " + intent.toString();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                StringBuilder str = new StringBuilder();
                str.append(intent_string);
                Set<String> keys = bundle.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    str.append("[" + key + "=" + bundle.get(key)+"]\n");
                }
                intent_string = str.toString();
            }
        }

        String result = sharedPreferences.getString("test", "Empty");

        if (result == "Empty") {
            sharedPreferences.edit().putString("test", String.format("Initial,%.8f,%.8f", LatitudeDestination, LongitudeDestination)).apply();
        } else {
            String[] array = result.split(",", -1);
            LatitudeDestination = Float.valueOf(array[1]);
            LongitudeDestination = Float.valueOf(array[2]);
        }

        handler = new Handler();
        handler.postDelayed(runnable, 100);


        if (f1 == null) {
            f1 = new Fragment1();
            f1.setRetainInstance(true);
        }

        if (f2 == null) {
            f2 = new Fragment2();
            f2.setRetainInstance(true);
        }

        if (f3 == null) {
            f3 = new Fragment3();
            f3.setRetainInstance(true);
        }

        if (f4 == null) {
            f4 = new Fragment4();
            f4.setRetainInstance(true);
        }

        if (f5 == null) {
            f5 = new Fragment5();
            f5.setRetainInstance(true);
        }

        if (f6 == null) {
            f6 = new FragmentGPS();
            f6.setRetainInstance(true);
        }

/*

            f3 = new Fragment3();
            f3.setRetainInstance(true);

            f4 = new Fragment4();
            f4.setRetainInstance(true);

            f5 = new Fragment5();
            f5.setRetainInstance(true);
            */
/*
/*
        f1.setRetainInstance(true);
        FragmentTransaction FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.fragment, f1);
        FT.addToBackStack("f1");
        FT.commit();
*/

        /*
        f2.setRetainInstance(true);
        FragmentTransaction FT1 = getSupportFragmentManager().beginTransaction();
        FT1.add(R.id.fragment2, f2);
        FT1.addToBackStack("f2");
        FT1.commit();
*/
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));
            viewPager.setOffscreenPageLimit(100);

            // up to here it's already working with swipes
            tabLayout = (TabLayout) findViewById(R.id.tabLayout);
            tabLayout.setupWithViewPager(viewPager);

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
            });


        new CountDownTimer(30000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (f2 != null && f2.isVisible()) {
                    f2.setTime("seconds remaining: " + millisUntilFinished / 1000);
                }
                textview.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

                f2.setTime("finished");
                textview.setText("fine");
            }

        }.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        f1.setJens(klaus);
//        klaus++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private class CustomAdapter extends FragmentPagerAdapter {
        private String fragments [] = {"OSMAND", "COUNTDOWN", "DIGITAL CLOCK", "LIST PACKAGES", "COMPASS", "GPS"};
        public CustomAdapter(FragmentManager fm, Context applicationContext) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //f1 = new Fragment1();
                    return f1;
                case 1:
                    //f2 = new Fragment2();
                    return f2;
                case 2:
                    //f3 = new Fragment3();
                    return f3;
                case 3:
                    //f4 = new Fragment4();
                    return f4;
                case 4:
                    //f5 = new Fragment5();
                    return f5;
                case 5:
                    //f6 = new FragmentGPS();
                    return f6;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
