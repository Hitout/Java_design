package com.design.single;

import org.junit.Test;

/**
 * @author gxyan
 */
public class SingleTest {

    @Test
    public void LazySingleTest() {
        LazySingle lazySingle1 = LazySingle.instance();
        LazySingle lazySingle2 = LazySingle.instance();
        LazySingle lazySingle3 = LazySingle.instance();
    }

    @Test
    public void HungrySingleTest() {
        HungrySingle hungrySingle1 = HungrySingle.instance();
        HungrySingle hungrySingle2 = HungrySingle.instance();
        HungrySingle hungrySingle3 = HungrySingle.instance();
    }
}
