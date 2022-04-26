package com.tvd12.ezyfoxserver.testing;

import com.tvd12.test.base.BaseTest;

public class SystemPropertiesTest extends BaseTest {

    public static void main(String[] args) {
        System.setProperty("hello", "world");
        System.out.println(System.getProperty("hello"));
    }
}
