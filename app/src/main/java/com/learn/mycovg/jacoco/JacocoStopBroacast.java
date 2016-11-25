package com.learn.mycovg.jacoco;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast for generage coverage.ec
 */

public class JacocoStopBroacast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("BroadcastReceiver", "JacocoStopBroacast_onReceive: ");
        JacocoInstrumentation.instance.stop();
    }
}
