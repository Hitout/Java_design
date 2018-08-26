package com.design.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * 享元工厂类
 *
 * 享元工厂类的作用在于提供一个用于存储享元对象的享元池，
 * 用户需要对象时，首先从享元池中获取，
 * 如果享元池中不存在，则创建一个新的享元对象返回给用户，并在享元池中保存该新增对象
 *
 * @author gxyan
 * @Date: 2018/8/26 14:04
 */
public class FlyweightFactory {
    static Map<String, Shape> shapeMap = new HashMap <>();

    public static Shape getShape(String color) {
        Shape shape = shapeMap.get(color);

        // 如果shape==null，则新建，并且保持到共享池中
        if(shape == null){
            shape = new Circle(color);
            shapeMap.put(color, shape);
        }
        return shape;
    }

    public static int getSum() {
        return shapeMap.size();
    }
}
