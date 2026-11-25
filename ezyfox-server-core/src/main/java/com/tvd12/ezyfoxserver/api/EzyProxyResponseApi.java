package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.codec.EzyCodecFactory;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.response.EzyPackage;

import java.util.HashMap;
import java.util.Map;

public class EzyProxyResponseApi implements EzyResponseApi {

    private final Map<String, EzyResponseApi> responseApiByConnectionType;
    private final EzyProtocolResponseApi protocolResponseApi;
    protected final int compressionThreshold;

    public EzyProxyResponseApi(EzyCodecFactory codecFactory) {
        this(codecFactory, EzySocketResponseApi.DEFAULT_COMPRESSION_THRESHOLD);
    }

    public EzyProxyResponseApi(
        EzyCodecFactory codecFactory,
        int compressionThreshold
    ) {
        this.compressionThreshold = compressionThreshold;
        Object socketEncoder = codecFactory.newEncoder(EzyConnectionType.SOCKET);
        Object wsEncoder = codecFactory.newEncoder(EzyConnectionType.WEBSOCKET);
        this.responseApiByConnectionType = new HashMap<>();
        this.responseApiByConnectionType.put(
            EzyConnectionType.SOCKET.getName(),
            newSocketResponseApi(socketEncoder)
        );
        this.responseApiByConnectionType.put(
            EzyConnectionType.WEBSOCKET.getName(),
            newWebsocketResponseApi(wsEncoder)
        );
        this.protocolResponseApi = new EzyProtocolResponseApi();
    }

    private EzyResponseApi newSocketResponseApi(Object socketEncoder) {
        return socketEncoder != null
            ? createSocketResponseApi(socketEncoder)
            : EzyEmptyResponseApi.getInstance();
    }

    protected EzySocketResponseApi createSocketResponseApi(
        Object socketEncoder
    ) {
        return new EzySocketResponseApi(socketEncoder, compressionThreshold);
    }

    private EzyResponseApi newWebsocketResponseApi(Object wsEncoder) {
        return wsEncoder != null
            ? new EzyWsResponseApi(wsEncoder)
            : EzyEmptyResponseApi.getInstance();
    }

    @Override
    public void response(EzyPackage pack, boolean immediate) throws Exception {
        for (String connectionType : pack.getConnectionTypes()) {
            EzyResponseApi responseApi = responseApiByConnectionType.get(
                connectionType
            );
            if (responseApi != null) {
                responseApi.response(pack, immediate);
            } else {
                protocolResponseApi.response(pack, connectionType, immediate);
            }
        }
    }
}
