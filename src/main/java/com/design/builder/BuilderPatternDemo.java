package com.design.builder;

import static com.design.builder.NyPizza.Size.*;
import static com.design.builder.Pizza.Topping.*;

/**
 * @author gxyan
 * @date 2018/8/26 19:29
 */
public class BuilderPatternDemo {
    public static void main(String[] args) {
        NyPizza pizza = new NyPizza.Builder(SMALL).addTopping(SAUSAGE).addTopping(ONION).build();
        pizza.showPizza();
        Calzone calzone = new Calzone.Builder().addTopping(HAM).sauceInside().build();
        calzone.showPizza();

        NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8)
                .calories(100).sodium(35).carbohydrate(27).build();
        cocaCola.showNutritionFacts();
    }
}
