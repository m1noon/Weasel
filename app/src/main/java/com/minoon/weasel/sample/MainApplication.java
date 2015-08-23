package com.minoon.weasel.sample;

import android.app.Application;
import android.util.Log;

import com.minoon.weasel.util.Logger;

/**
 * Created by a13587 on 15/06/27.
 */
public class MainApplication extends Application {
    private static final String TAG = MainApplication.class.getSimpleName();

    private Thread.UncaughtExceptionHandler mSavedUncaughtExceptionHandler;

    private Thread.UncaughtExceptionHandler mUncaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        private boolean mCrashing;

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                if(!mCrashing) {
                    mCrashing = true;
                    Log.e(TAG, "Uncaught Exception.", ex);
                }
            } finally {
                mSavedUncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        // Exception Handler
        mSavedUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
        Logger.setLoggable(true);
    }
}
