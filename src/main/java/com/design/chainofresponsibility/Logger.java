package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/24
 */
public interface Logger {
    int DEBUG = 1;
    int INFO = 2;
    int ERROR = 3;

    void setNextLogger(Logger nextLogger);

    void logMessage(int level, String message);

    void log(String message);
}
