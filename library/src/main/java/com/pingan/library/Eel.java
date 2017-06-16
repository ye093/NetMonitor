package com.pingan.library;

import android.content.Context;

import com.pingan.library.log.AndroidLog;
import com.pingan.library.log.IAgentLog;
import com.pingan.library.log.LogManager;
import com.pingan.library.log.NullIAgentLog;

/**
 * Created by yejy on 17/5/19.
 */

public class Eel {
    protected static boolean started = false;
    protected static final IAgentLog log = LogManager.getAgentLog();
    protected static final EelConfiguration configuration = new EelConfiguration();
    protected boolean loggingEnabled;
    protected int logLevel;
    protected Eel(final String token) {
        this.loggingEnabled = true;
        this.logLevel = 5;
        Eel.configuration.setApplicationToken(token);
    }

    public static Eel withApplicationToken(final String token) {
        return new Eel(token);
    }

    public void start(final Context context) {
        if (Eel.started) {
            Eel.log.debug("Eel is already running.");
            return;
        }
        try {
            LogManager.setAgentLog(this.loggingEnabled ? new AndroidLog() : new NullIAgentLog());
            Eel.log.setLevel(this.logLevel);
            AndroidAspectjx.init(context, Eel.configuration);
            Eel.started = true;
        } catch (Throwable e) {
            Eel.log.error("Error occurred while starting the New Relic agent!", e);
        }
    }

}
