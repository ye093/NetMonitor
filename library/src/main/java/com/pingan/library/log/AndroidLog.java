package com.pingan.library.log;

import android.util.Log;

public class AndroidLog implements IAgentLog {

    private static final String TAG = AndroidLog.class.getSimpleName();
    private int level;

    public AndroidLog() {
        Log.d(TAG, "AndroidLog()");
        this.level = 5;
    }

    public void debug(final String message) {
        if (this.level == 5) {
            Log.d(TAG, message);
        }
    }

    public void verbose(final String message) {
        if (this.level >= 4) {
            Log.v(TAG, message);
        }
    }

    public void info(final String message) {
        if (this.level >= 3) {
            Log.i(TAG, message);
        }
    }

    public void warning(final String message) {
        if (this.level >= 2) {
            Log.w(TAG, message);
        }
    }

    public void error(final String message) {
        if (this.level >= 1) {
            Log.e(TAG, message);
        }
    }

    public void error(final String message, final Throwable cause) {
        if (this.level >= 1) {
            Log.e(TAG, message, cause);
        }
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(final int level) {
        if (level <= 5 && level >= 1) {
            this.level = level;
            return;
        }
        throw new IllegalArgumentException("Log level is not between ERROR and DEBUG");
    }
}
