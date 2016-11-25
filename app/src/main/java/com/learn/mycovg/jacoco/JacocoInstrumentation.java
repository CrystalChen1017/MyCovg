package com.learn.mycovg.jacoco;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

/**
 * using "adb shell am instrument ${pkgName}/${className} " to start your app，
 * any operates on app will be counted in code coverage untils MainAcitivity destroyed or received a JacocoStopBroadcast
 */
public class JacocoInstrumentation extends Instrumentation implements FinishListener {
    public static String TAG = "JacocoInstrumentation:";
    private final Bundle mResults = new Bundle();
    private Intent mIntent;
    public static JacocoInstrumentation instance;


    public JacocoInstrumentation() {
        instance = this;
    }

    @Override
    public void onCreate(Bundle arguments) {
        Log.d(TAG, "onCreate(" + arguments + ")");
        super.onCreate(arguments);
        mIntent = new Intent(getTargetContext(), InstrumentedActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        start();
    }

    @Override
    public void onStart() {
        super.onStart();
        Looper.prepare();
        InstrumentedActivity activity = (InstrumentedActivity) startActivitySync(mIntent);
        activity.setFinishListener(this);
    }

    /**
     * When received JacocoStopBroacast, ec file will be generated
     */
    public void stop() {
        JacocoUtils.generateEcFile(false);
        finish(Activity.RESULT_OK, mResults);
    }

    /**
     * When MainAcitivity was destoryed , ec file will be generated
     */
    @Override
    public void onActivityFinished() {
        JacocoUtils.generateEcFile(false);
        finish(Activity.RESULT_OK, mResults);
    }
}