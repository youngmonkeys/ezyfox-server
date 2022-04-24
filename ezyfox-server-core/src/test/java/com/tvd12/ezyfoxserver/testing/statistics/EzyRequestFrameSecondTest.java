package com.tvd12.ezyfoxserver.testing.statistics;

import com.tvd12.ezyfoxserver.statistics.EzyRequestFrameSecond;
import com.tvd12.test.base.BaseTest;
import org.testng.annotations.Test;

public class EzyRequestFrameSecondTest extends BaseTest {

    @Test
    public void test1() {
        EzyRequestFrameSecond frame = new EzyRequestFrameSecond(20);
        assert !frame.isInvalid();
        assert !frame.isExpired();
        assert frame.nextFrame() != frame;
        System.out.println(frame.getStartTime());
        assert frame.getStartTime() < frame.getEndTime();
        assert frame.getMaxRequests() == 20;
        assert frame.getRequests() == 0;
        System.out.println(frame);
    }

    @Test
    public void test2() {
        EzyRequestFrameSecond frame = new EzyRequestFrameSecond(20);
        frame.addRequests(18);
        assert !frame.isInvalid();
        frame.addRequests(2);
        assert !frame.isInvalid();
        frame.addRequests(1);
        assert frame.isInvalid();
    }

    @Test
    public void test3() {
        EzyRequestFrameSecond frame = new EzyRequestFrameSecond(20, System.currentTimeMillis() - 10000);
        assert frame.isExpired();
    }

}
