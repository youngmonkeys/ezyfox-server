package com.tvd12.ezyfoxserver.testing.statistics;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkBytesFrameSecond;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyNetworkBytesFrameSecondTest extends BaseTest {

    @Test
    public void test() {
        EzyNetworkBytesFrameSecond frame = new EzyNetworkBytesFrameSecond();
        System.out.println(frame.getStartTime());
        assert frame.getEndTime() > frame.getStartTime();
        System.out.println(frame);
    }
}
