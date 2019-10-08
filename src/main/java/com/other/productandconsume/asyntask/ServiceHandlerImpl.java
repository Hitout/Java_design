package com.other.productandconsume.asyntask;

/**
 * 队列业务处理实现
 * @author gxyan
 * @date 2019/9/30
 */
public class ServiceHandlerImpl implements QueueTaskHandler {
    private String name;

    public ServiceHandlerImpl(String name) {
        this.name = name;
    }

    @Override
    public String processData() {
        // 此处进行业务处理
        return name;
    }
}
