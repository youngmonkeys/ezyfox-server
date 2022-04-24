package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class MyTestSession extends EzyAbstractSession {
    private static final long serialVersionUID = -1980181361152627223L;

    @Override
    public SocketAddress getClientAddress() {
        return new InetSocketAddress("123.123.123.123", 8080);
    }

    @Override
    public SocketAddress getServerAddress() {
        return new InetSocketAddress("localhost", 8080);
    }

    @Override
    public void close() {
    }

    @Override
    public void disconnect(EzyConstant disconnectReason) {
    }

    @Override
    public EzySessionDelegate getDelegate() {
        return new EzySessionDelegate() {

            @Override
            public void onSessionLoggedIn(EzyUser user) {

            }
        };
    }
}
