package com.tvd12.ezyfoxserver.nio.websocket;

import org.eclipse.jetty.websocket.api.CloseStatus;

public class EzyWsCloseStatus extends CloseStatus {
    
    public static final EzyWsCloseStatus CLOSE_BY_SERVER = new EzyWsCloseStatus(3000, "Close By Server");

    public EzyWsCloseStatus(int closeCode, String reasonPhrase) {
        super(closeCode, reasonPhrase);
    }

}
