package com.tvd12.ezyfoxserver.statistics;

import com.tvd12.ezyfox.util.EzyTimes;

public class EzyNetworkBytesFrameMinute extends EzyNetworkBytesFrame {
    private static final long serialVersionUID = 540300681186980725L;

    public EzyNetworkBytesFrameMinute() {
        super();
    }

    public EzyNetworkBytesFrameMinute(long startTime) {
        super(startTime);
    }

    @Override
    protected long getExistsTime() {
        return EzyTimes.MILLIS_OF_MINUTE;
    }

    public EzyNetworkBytesFrame nextFrame() {
        return new EzyNetworkBytesFrameMinute(endTime);
    }
}
