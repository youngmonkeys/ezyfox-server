package com.tvd12.ezyfoxserver.testing.statistics;

import com.tvd12.ezyfoxserver.statistics.EzyNetworkBytesFrameHour;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkBytesFrameMinute;
import com.tvd12.ezyfoxserver.statistics.EzyNetworkBytesFrameSecond;
import com.tvd12.ezyfoxserver.statistics.EzySimpleNetworkStats;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.FieldUtil;
import org.testng.annotations.Test;

public class EzySimpleNetworkStatsTest extends BaseTest {

    @Test
    public void test() {
        EzySimpleNetworkStats stats = new EzySimpleNetworkStats();
        assert stats.getDroppedInBytes() == 0;
        assert stats.getDroppedOutBytes() == 0;
        assert stats.getWriteErrorBytes() == 0;
        assert stats.getDroppedInPackets() == 0;
        assert stats.getDroppedOutPackets() == 0;
        assert stats.getWriteErrorPackets() == 0;

        assert stats.getFrameHour() != null;
        assert stats.getFrameMinute() != null;
        assert stats.getFrameSecond() != null;

        stats.addDroppedInBytes(1);
        assert stats.getDroppedInBytes() == 1;
        stats.addDroppedOutBytes(2);
        assert stats.getDroppedOutBytes() == 2;
        stats.addWriteErrorBytes(3);
        assert stats.getWriteErrorBytes() == 3;
        assert stats.getReadBytesPerHour() == 0;
        assert stats.getReadBytesPerMinute() == 0;
        assert stats.getReadBytesPerSecond() == 0;
        assert stats.getWrittenBytesPerHour() == 0;
        assert stats.getWrittenBytesPerMinute() == 0;
        assert stats.getWrittenBytesPerSecond() == 0;
        stats.addDroppedInPackets(4);
        assert stats.getDroppedInPackets() == 4;
        stats.addDroppedOutPackets(5);
        assert stats.getDroppedOutPackets() == 5;
        stats.addWriteErrorPackets(6);
        assert stats.getWriteErrorPackets() == 6;

        EzyNetworkBytesFrameHour frameHour = new EzyNetworkBytesFrameHour(System.currentTimeMillis() - 2 * 60 * 60 * 1000);
        EzyNetworkBytesFrameMinute frameMinute = new EzyNetworkBytesFrameMinute(System.currentTimeMillis() - 2 * 60 * 1000);
        EzyNetworkBytesFrameSecond frameSecond = new EzyNetworkBytesFrameSecond(System.currentTimeMillis() - 2 * 1000);

        FieldUtil.setFieldValue(stats, "frameHour", frameHour);
        FieldUtil.setFieldValue(stats, "frameMinute", frameMinute);
        FieldUtil.setFieldValue(stats, "frameSecond", frameSecond);

        stats.addReadBytes(1);

        frameHour = new EzyNetworkBytesFrameHour(System.currentTimeMillis() - 2 * 60 * 60 * 1000);
        frameMinute = new EzyNetworkBytesFrameMinute(System.currentTimeMillis() - 2 * 60 * 1000);
        frameSecond = new EzyNetworkBytesFrameSecond(System.currentTimeMillis() - 2 * 1000);

        FieldUtil.setFieldValue(stats, "frameHour", frameHour);
        FieldUtil.setFieldValue(stats, "frameMinute", frameMinute);
        FieldUtil.setFieldValue(stats, "frameSecond", frameSecond);
        stats.addWrittenBytes(1);
    }
}
