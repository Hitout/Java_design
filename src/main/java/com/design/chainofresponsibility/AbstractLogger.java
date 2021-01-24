package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/22
 */
public abstract class AbstractLogger implements Logger {
    protected int level;
    protected Logger nextLogger;

    @Override
    public void setNextLogger(Logger nextLogger) {
        this.nextLogger = nextLogger;
    }

    @Override
    public void logMessage(int level, String message) {
        if (this.level <= level) {
            write(message);
        }
        if (nextLogger != null) {
            nextLogger.logMessage(level, message);
        }
    }

    @Override
    public void log(String message) {
        logMessage(this.level, message);
    }

    abstract protected void write(String message);
}
