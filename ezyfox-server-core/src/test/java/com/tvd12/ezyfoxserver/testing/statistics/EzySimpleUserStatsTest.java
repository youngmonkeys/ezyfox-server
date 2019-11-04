package com.tvd12.ezyfoxserver.testing.statistics;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.statistics.EzySimpleUserStats;
import com.tvd12.test.base.BaseTest;

public class EzySimpleUserStatsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleUserStats stats = new EzySimpleUserStats();
        assert stats.getMaxUsers() == 0;
        assert stats.getTotalUsers() == 0;
        assert stats.getCurrentUsers() == 0;
        stats.setCurrentUsers(2);
        stats.setCurrentUsers(1);
        assert stats.getMaxUsers() == 2;
        assert stats.getCurrentUsers() == 1;
    }
    
}
