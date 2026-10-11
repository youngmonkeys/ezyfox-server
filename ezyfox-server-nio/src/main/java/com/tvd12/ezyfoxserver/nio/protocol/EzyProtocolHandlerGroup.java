package com.tvd12.ezyfoxserver.nio.protocol;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.nio.handler.EzyAbstractHandlerGroup;
import com.tvd12.ezyfoxserver.nio.handler.EzyHandlerGroup;

public class EzyProtocolHandlerGroup
    extends EzyAbstractHandlerGroup<EzyDestroyable>
    implements EzyHandlerGroup {

    public EzyProtocolHandlerGroup(Builder builder) {
        super(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    protected EzyDestroyable newDecoder(Object decoder) {
        return () -> {};
    }

    public void fireDataReceived(EzyArray data, int dataSize) {
        try {
            handleReceivedData(data, dataSize);
        } catch (Throwable e) {
            fireExceptionCaught(e);
        } finally {
            executeAddReadBytes(dataSize);
        }
    }

    public static class Builder extends EzyAbstractHandlerGroup.Builder {

        @Override
        public EzyHandlerGroup build() {
            return new EzyProtocolHandlerGroup(this);
        }
    }
}
