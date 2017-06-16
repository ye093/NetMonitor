package com.pingan.library.log;

public class LogManager {

    private static LogAdapter instance;

    public static IAgentLog getAgentLog() {
        return LogManager.instance;
    }

    public static void setAgentLog(final IAgentLog instance) {
        LogManager.instance.setImpl(instance);
    }

    static {
        LogManager.instance = new LogAdapter();
    }
}