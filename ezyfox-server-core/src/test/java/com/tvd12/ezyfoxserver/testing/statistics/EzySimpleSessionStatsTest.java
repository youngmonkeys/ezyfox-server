package com.tvd12.ezyfoxserver.testing.statistics;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.statistics.EzySimpleSessionStats;
import com.tvd12.test.base.BaseTest;

public class EzySimpleSessionStatsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleSessionStats stats = new EzySimpleSessionStats();
        assert stats.getMaxSessions() == 0;
        assert stats.getTotalSessions() == 0;
        assert stats.getCurrentSessions() == 0;
        stats.addSessions(1);
        assert stats.getTotalSessions() == 1;
        stats.setCurrentSessions(2);
        stats.setCurrentSessions(1);
        assert stats.getMaxSessions() == 2;
        assert stats.getCurrentSessions() == 1;
    }
    
}
