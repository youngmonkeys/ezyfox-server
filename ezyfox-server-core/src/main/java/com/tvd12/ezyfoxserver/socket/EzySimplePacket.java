package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimplePacket implements EzyPacket {

    @Setter
    private Object data;
    private boolean released;
    private boolean fragmented;
    @Setter
    private boolean binary = true;
    @Setter
    private EzyConstant transportType = EzyTransportType.TCP;

    @Override
    public void replaceData(Object data) {
        this.data = data;
    }

    @Override
    public void setFragment(Object fragment) {
        this.data = fragment;
        this.fragmented = true;
    }

    @Override
    public int getSize() {
        Object currentData = data;
        if (currentData == null) {
            return 0;
        }
        if (currentData instanceof String) {
            return ((String) currentData).length();
        }
        return ((byte[]) currentData).length;
    }

    @Override
    public void release() {
        this.data = null;
        this.released = true;
    }

    @Override
    public String toString() {
        return "(" +
            "transportType: " +
            transportType +
            ", data: " +
            data +
            ")";
    }
}
