package com.pingan.library.log;

public class NullIAgentLog implements IAgentLog {
    @Override
    public void debug(final String message) {
    }

    @Override
    public void info(final String message) {
    }

    @Override
    public void verbose(final String message) {
    }

    @Override
    public void error(final String message) {
    }

    @Override
    public void error(final String message, final Throwable cause) {
    }

    @Override
    public void warning(final String message) {
    }

    @Override
    public int getLevel() {
        return 5;
    }

    @Override
    public void setLevel(final int level) {
    }
}