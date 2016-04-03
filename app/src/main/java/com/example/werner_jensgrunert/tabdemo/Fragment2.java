// http://tamsler.blogspot.it/2011/11/android-viewpager-and-fragments-part-ii.html

package com.example.werner_jensgrunert.tabdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Werner-Jens Grunert on 2/5/2016.
 */
public class Fragment2 extends Fragment {
    String jens = "Start";
    TextView textView = null;
    TextView textViewCount;
    TextView textViewMain;
    FrameLayout frameLayout;

    TextView mClock;

    Calendar mCalendar;

    private Runnable mTicker;
    private Handler mHandler;

    String mFormat;
    SimpleDateFormat simpleDateFormat;

    private boolean mClockStopped = false;

    static int i = 0;

    public void setTime(final String jenss) {
        // this.jens = jens;
        // textView = (TextView) getActivity().findViewById(R.id.textView2);
        if (textView != null) {
            textView.setText(jenss);
        }
    }

    public void passData(final String value) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (textView != null) {
                    textView.setText(value);
                }
            }
        });
    }
    public Fragment2() {
        setRetainInstance(true);
        i++;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag2, container, false);

        frameLayout = new FrameLayout(getActivity());
        frameLayout.removeAllViews();


        textView = (TextView) view.findViewById(R.id.textView2);

        textView.setText(jens);

        textViewCount = (TextView) view.findViewById(R.id.textViewcount);
        textViewCount.setText("onCreateView " + i++);

        mClock = (TextView) view.findViewById(R.id.textViewClock);

        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        mCalendar = Calendar.getInstance();

        mHandler = new Handler();

        mTicker = new Runnable() {
            public void run() {
                if(mClockStopped) return;
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                mClock.setText(simpleDateFormat.format(mCalendar.getTime()));
                // mClock.invalidate();
                // long now = SystemClock.uptimeMillis();
                // long next = now + (1000 - now % 1000);
                // mHandler.postAtTime(mTicker, next);
                mHandler.postDelayed(mTicker, 1000);
            }
        };
        mTicker.run();


        // return view;
        frameLayout.addView(view);

        return frameLayout;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        frameLayout.removeAllViews();

        LayoutInflater inflater =  (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.frag2, null);

        textViewCount.setText("onConfigurationChanged " + i++);


        frameLayout.addView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        textView.setText(jens);
        textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
        textViewMain.setText("onResume Fragment2");
    }

    @Override
    public void onPause() {
        super.onPause();
        textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
        textViewMain.setText("onPause Fragment2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && isResumed()) {
            textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
            textViewMain.setText("setUserVisibleHint Fragment2");
        } else if (isResumed()) {
            textViewMain = (TextView) getActivity().findViewById(R.id.textView3);
            textViewMain.setText("setUserVisibleHint Not Fragment2");
        }
    }
}
