package com.learn.mycovg.jacoco;

import android.util.Log;

import com.learn.mycovg.MainActivity;


public class InstrumentedActivity extends MainActivity {
    public static String TAG = "InstrumentedActivity";

    private FinishListener mListener;

    public void setFinishListener(FinishListener listener) {
        mListener = listener;
    }

    public void onDestroy() {
        Log.d(TAG , "Activity onDestroy()");
        super.finish();
        if (mListener != null) {
            mListener.onActivityFinished();
        }
    }

}