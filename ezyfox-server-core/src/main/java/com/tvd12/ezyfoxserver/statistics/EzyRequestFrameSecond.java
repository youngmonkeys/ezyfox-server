package com.tvd12.ezyfoxserver.statistics;

public class EzyRequestFrameSecond extends EzyRequestFrame {
    private static final long serialVersionUID = 2119544926700098057L;

    public EzyRequestFrameSecond(int maxRequests) {
        super(maxRequests);
    }

    public EzyRequestFrameSecond(int maxRequests, long startTime) {
        super(maxRequests, startTime);
    }

    @Override
    protected long getExistsTime() {
        return 1000;
    }

    @Override
    public EzyRequestFrame nextFrame() {
        return new EzyRequestFrameSecond(maxRequests, endTime);
    }

}
