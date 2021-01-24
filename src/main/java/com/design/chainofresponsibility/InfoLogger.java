package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/22
 */
public class InfoLogger extends AbstractLogger {
    public InfoLogger() {
        this.level = INFO;
    }

    @Override
    protected void write(String message) {
        System.out.println("INFO::Logger: " + message);
    }
}
