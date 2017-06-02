package com.example.helllo.tim;


import android.os.Handler;
import android.os.SystemClock;

public class Timer extends Handler {

    private Runnable mCallback;
    private int mInterval;
    private long mLastUpdateTime;
    private boolean mRunning = false;

    public Timer(Runnable cb) {
        mCallback = cb;
        mInterval = 0;
        mLastUpdateTime = 0;
        mRunning = false;
    }

    public boolean start(int interval) {
        if(interval < 0) {
            return false;
        }

        if(mRunning) {
            return true;
        }

        stop();
        mInterval = interval;

        mLastUpdateTime = SystemClock.uptimeMillis();
        mRunning = true;
        periodicCallback();

        return true;
    }

    public void stop() {
        removeCallbacks(mInternalCallback);
        mRunning = false;
    }

    private void periodicCallback() {
        if(!mRunning) {
            return;
        }

        long now = SystemClock.uptimeMillis();
        long next = mLastUpdateTime + mInterval;

        while(now > next) {
            next += next + mInterval;
        }

        postAtTime(mInternalCallback, next);
        mLastUpdateTime = next;

        mCallback.run();
    }

    private Runnable mInternalCallback = new Runnable() {
        public void run() {
            periodicCallback();
        };
    };
}