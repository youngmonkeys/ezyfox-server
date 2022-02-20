package com.tvd12.ezyfoxserver.testing.statistics;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkBytesFrameSecond;
import com.tvd12.test.base.BaseTest;

public class EzyNetworkBytesFrameSecondTest extends BaseTest {
    
    @Test
    public void test() {
        EzyNetworkBytesFrameSecond frame = new EzyNetworkBytesFrameSecond();
        System.out.println(frame.getStartTime());
        assert frame.getEndTime() > frame.getStartTime();
        System.out.println(frame);
    }

}
