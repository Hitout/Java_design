package com.design.builder;

import java.util.Objects;

/**
 * @author gxyan
 * @date 2018/8/26 19:28
 */
public class NyPizza extends Pizza {
    public enum Size { SMALL, MEDIUM, LARGE }
    private final Size size;

    public static class Builder extends Pizza.Builder<Builder> {
        private final Size size;

        public Builder(Size size) {
            this.size = Objects.requireNonNull(size);
        }

        @Override
        public NyPizza build() {
            return new NyPizza(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private NyPizza(Builder builder) {
        super(builder);
        size = builder.size;
    }

    @Override
    public void showPizza() {
        super.showPizza();
        System.out.println("Size: " + size + "");
        System.out.println();
    }
}
