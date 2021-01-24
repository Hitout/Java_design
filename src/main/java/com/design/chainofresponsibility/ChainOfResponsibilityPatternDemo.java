package com.design.chainofresponsibility;

/**
 * @author gxyan
 * @date 2021/1/22
 */
public class ChainOfResponsibilityPatternDemo {
    public static void main(String[] args) {
        Logger errorLogger = new ErrorLogger();
        Logger infoLogger = new InfoLogger();
        Logger debugLogger = new DebugLogger();
        errorLogger.setNextLogger(infoLogger);
        infoLogger.setNextLogger(debugLogger);

        errorLogger.log("Is Error");
        infoLogger.log("Is Info");
        debugLogger.log("Is Debug");
    }
}
