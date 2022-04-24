package com.tvd12.ezyfoxserver.statistics;

import com.tvd12.ezyfox.util.EzyTimes;

public class EzyNetworkBytesFrameHour extends EzyNetworkBytesFrame {
    private static final long serialVersionUID = -8294006426138093426L;

    public EzyNetworkBytesFrameHour() {
        super();
    }

    public EzyNetworkBytesFrameHour(long startTime) {
        super(startTime);
    }

    @Override
    protected long getExistsTime() {
        return EzyTimes.MILLIS_OF_HOUR;
    }

    public EzyNetworkBytesFrame nextFrame() {
        return new EzyNetworkBytesFrameHour(endTime);
    }
}
