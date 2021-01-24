package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/22
 */
public class DebugLogger extends AbstractLogger {
    public DebugLogger() {
        this.level = DEBUG;
    }

    @Override
    protected void write(String message) {
        System.out.println("DEBUG::Logger: " + message);
    }
}
