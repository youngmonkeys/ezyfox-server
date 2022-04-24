package com.tvd12.ezyfoxserver.factory;

import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySessionManagementSetting.EzyMaxRequestPerSecond;
import com.tvd12.ezyfoxserver.socket.EzyNonBlockingPacketQueue;
import com.tvd12.ezyfoxserver.socket.EzyNonBlockingRequestQueue;
import com.tvd12.ezyfoxserver.statistics.EzyRequestFrameSecond;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class EzyAbstractSessionFactory<S extends EzySession>
    implements EzySessionFactory<S> {

    protected final AtomicInteger counter = new AtomicInteger(0);
    @Setter
    protected EzyMaxRequestPerSecond maxRequestPerSecond;

    @Override
    public final S newProduct() {
        S session = newSession();
        initSession((EzyAbstractSession) session);
        return session;
    }

    protected void initSession(EzyAbstractSession session) {
        session.setId(counter.incrementAndGet());
        session.setCreationTime(System.currentTimeMillis());
        session.setPacketQueue(new EzyNonBlockingPacketQueue());
        session.setSystemRequestQueue(new EzyNonBlockingRequestQueue());
        session.setExtensionRequestQueue(new EzyNonBlockingRequestQueue());
        session.setRequestFrameInSecond(new EzyRequestFrameSecond(maxRequestPerSecond.getValue()));
    }

    protected abstract S newSession();

}
