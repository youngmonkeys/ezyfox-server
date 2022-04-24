package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.collect.Lists;

import java.util.List;

public class UsernamePatternTest {

    public static void main(String[] args) {
        String pattern = "^[a-z0-9_.]{3,36}$";
        List<String> names = Lists.newArrayList(
            "_tavandung",
            "dungtv-hehe",
            "dung@name",
            "dung123",
            "dung.tvd",
            "dung.tvd.",
            "dung--tvd",
            "dũng"
        );
        for (String name : names) {
            System.out.println("name: " + name + ", valid = " + name.matches(pattern));
        }
    }
}
