package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/22
 */
public class ErrorLogger extends AbstractLogger {
    public ErrorLogger() {
        this.level = ERROR;
    }

    @Override
    protected void write(String message) {
        System.out.println("ERROR::Logger: " + message);
    }
}
