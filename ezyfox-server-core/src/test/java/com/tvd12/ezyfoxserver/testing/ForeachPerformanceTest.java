package com.tvd12.ezyfoxserver.testing;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.collect.Lists;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

public class ForeachPerformanceTest extends BaseTest {

    @SuppressWarnings("unused")
    @Test
    public void test() {
        List<String> list = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        long time1 = Performance.create()
            .test(()-> {
                list.forEach(s -> {String s1 = s + 1;});
            })
            .getTime();
        
        long time2 = Performance.create()
                .test(()-> {
                    for(String s : list) {
                        String s1 = s + 1;
                    }
                })
                .getTime();
        
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);
    }
    
    @Test
    public void test2() {
        List<String> list1 = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        List<String> list2 = Lists.newArrayList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
        long time1 = Performance.create()
            .test(()-> {
                list1.addAll(list2);
            })
            .getTime();
        
        long time2 = Performance.create()
                .test(()-> {
                    list2.forEach(s -> list1.add(s));
                })
                .getTime();
        
        System.out.println("time1 = " + time1);
        System.out.println("time2 = " + time2);
    }
    
}
