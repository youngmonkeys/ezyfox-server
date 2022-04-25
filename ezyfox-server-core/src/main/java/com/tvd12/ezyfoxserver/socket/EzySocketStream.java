package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySocketStream extends EzyReleasable {

    byte[] getBytes();

    long getTimestamp();

    EzySession getSession();
}
