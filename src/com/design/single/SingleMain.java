package com.design.single;

public class SingleMain {
    public static void main(String[] args) {
        Single single1 = Single.instance();
        Single single2 = Single.instance();
        Single single3 = Single.instance();
    }
}
