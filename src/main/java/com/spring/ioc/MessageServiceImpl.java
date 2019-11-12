package com.spring.ioc;

/**
 * @author gxyan
 * @date 2019/11/2
 */
public class MessageServiceImpl implements IMessageService {
    @Override
    public String getMessage() {
        return "Hello World";
    }
}
