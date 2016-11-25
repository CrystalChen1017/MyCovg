package com.learn.mycovg.jacoco;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Utils for collecting code coverage
 */

public class JacocoUtils {
    static String TAG = "JacocoUtils";
    //ec file path
    private static String DEFAULT_COVERAGE_FILE_PATH = Environment.getExternalStorageDirectory()
            .getPath() + "/coverage.ec";

    /**
     * generate ec file
     *
     * @param isNew if delete old coverage.ec file needed
     */
    public static void generateEcFile(boolean isNew) {
        Log.d(TAG, "JacocoUtils_generateEcFile: ");
        OutputStream out = null;
        File mCoverageFilePath = new File(DEFAULT_COVERAGE_FILE_PATH);
        try {
            if (isNew && mCoverageFilePath.exists()) {
                Log.d(TAG, "JacocoUtils_generateEcFile: delete old coverage.ec");
                mCoverageFilePath.delete();
            }
            if (!mCoverageFilePath.exists()) {
                mCoverageFilePath.createNewFile();
            }
            out = new FileOutputStream(mCoverageFilePath.getPath(), true);
            Object agent = Class.forName("org.jacoco.agent.rt.RT")
                    .getMethod("getAgent")
                    .invoke(null);
            out.write((byte[]) agent.getClass().getMethod("getExecutionData", boolean.class)
                    .invoke(agent, false));
        } catch (Exception e) {
            Log.e(TAG, "generateEcFile: " + e.getMessage());
        } finally {
            if (out == null)
                return;
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
