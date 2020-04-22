package com.spring.ioc.annotation;

import com.spring.ioc.IMessageService;
import com.spring.ioc.MessageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author gxyan
 * @date 2019/11/12
 */
@Configuration
public class AppConfig {
    /**
     * 注解@Bean在返回实例的方法上使用，如果未通过@Bean指定bean的名称，则默认与标注的方法名相同
     * 默认作用域为singleton单例作用域，通过@Scope设置作用域
     */
    @Bean
    public IMessageService messageService() {
        return new MessageServiceImpl();
    }
}
