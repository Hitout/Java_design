package com.design.builder;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author gxyan
 * @date 2018/8/26 19:27
 */
public abstract class Pizza {
    public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}
    final Set<Topping> toppings;

    abstract static class Builder<T extends Builder<T>> {
        EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T addTopping(Topping topping) {
            toppings.add(Objects.requireNonNull(topping));
            return self();
        }

        abstract Pizza build();

        protected abstract T self();
    }

    Pizza(Builder<?> builder) {
        toppings = builder.toppings.clone();
    }

    public void showPizza() {
        System.out.print("toppings: ");
        for (Topping topping : toppings) {
            System.out.print(topping + " ");
        }
        System.out.println();
    }
}
