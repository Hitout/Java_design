package com.spring.ioc;

import com.spring.ioc.annotation.AppConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring可以通过两种方式启动ApplicationContext
 * 1. 使用AbstractXmlApplicationContext（的子类）加载xml配置文件
 * 2. 基于注解使用AnnotationConfigApplicationContext类加载
 *
 * @author gxyan
 * @date 2019/11/2
 */
public class App {
    public static void main(String[] args) {
        // 通过加载xml配置文件获取ApplicationContext
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("classpath:application.xml");
        // 从ApplicationContext中获取Bean
        IMessageService messageService = (IMessageService) xmlContext.getBean("messageService");
        System.out.println(messageService.getMessage());

        // 通过加载注解获取ApplicationContext
        // AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext();
        // annotationContext.register(AppConfig.class);
        // annotationContext.refresh();
        ApplicationContext annotationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        // 通过beanName获取Bean
        IMessageService bean = (IMessageService) annotationContext.getBean("messageService");
        System.out.println(bean.getMessage());
    }
}
