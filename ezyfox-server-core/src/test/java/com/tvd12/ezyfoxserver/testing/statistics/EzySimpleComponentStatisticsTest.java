package com.tvd12.ezyfoxserver.testing.statistics;

import com.tvd12.ezyfoxserver.statistics.EzySimpleComponentStatistics;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzySimpleComponentStatisticsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleComponentStatistics statistics = new EzySimpleComponentStatistics();
        assert statistics.getSessionStats() != null;
        assert statistics.getNetworkStats() != null;
    }
}
