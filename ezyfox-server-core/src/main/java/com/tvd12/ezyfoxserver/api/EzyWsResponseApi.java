package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfox.codec.EzyObjectToStringEncoder;
import com.tvd12.ezyfox.codec.EzySimpleStringDataEncoder;
import com.tvd12.ezyfox.codec.EzyStringDataEncoder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;
import com.tvd12.ezyfoxserver.socket.EzySimplePacket;

public class EzyWsResponseApi extends EzyAbstractResponseApi {

    protected final EzyStringDataEncoder encoder;

    public EzyWsResponseApi(Object encoder) {
        this.encoder = new EzySimpleStringDataEncoder((EzyObjectToStringEncoder) encoder);
    }

    @Override
    public void response(EzyPackage pack, boolean immediate) throws Exception {
        normalResponse(pack, immediate);
    }

    @Override
    protected EzySimplePacket createPacket(EzyConstant transportType, Object bytes) {
        EzySimplePacket packet = super.createPacket(transportType, bytes);
        packet.setBinary(false);
        return packet;
    }

    @Override
    protected Object encodeData(EzyArray data) throws Exception {
        return encoder.encode(data, String.class);
    }

    @Override
    protected EzyConstant getConnectionType() {
        return EzyConnectionType.WEBSOCKET;
    }
}
