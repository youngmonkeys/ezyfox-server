package com.tvd12.ezyfoxserver.testing.statistics;

import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkStats;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzySocketStatistics;

public class EzySimpleStatisticsTest {

    @Test
    public void test() throws Exception {
        test(3);
    }
    
    public void test(int max) throws Exception {
        EzySimpleStatistics statistics = new EzySimpleStatistics();
        EzySocketStatistics socketStats = statistics.getSocketStats();
        EzyNetworkStats networkStats = (EzyNetworkStats)socketStats.getNetworkStats();
        AtomicInteger count = new AtomicInteger();
        Thread[] threads = new Thread[max];
        for(int i = 0 ; i < threads.length ; i++) {
            threads[i] = new Thread(() -> {
               networkStats.addReadBytes(1 * 50);
               networkStats.addReadPackets(1);
               networkStats.addWrittenBytes(2 * 50);
               networkStats.addWrittenPackets(2);
               count.incrementAndGet();
            });
        }
        for(int i = 0 ; i < threads.length ; i++)
            threads[i].start();
        
        while(count.get() < max)
            Thread.sleep(5L);
        Thread.sleep(100);
        System.out.println("getReadBytes: " + networkStats.getReadBytes());
        System.out.println("getReadPackets: " + networkStats.getReadPackets());
        System.out.println("getWrittenBytes: " + networkStats.getWrittenBytes());
        System.out.println("getWrittenPackets: " + networkStats.getWrittenPackets());
        assert networkStats.getReadBytes() == max * 50;
        assert networkStats.getReadPackets() == max;
        assert networkStats.getWrittenBytes() == max * 2 * 50;
        assert networkStats.getWrittenPackets() == max * 2;
    }
    
}
