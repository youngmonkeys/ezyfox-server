package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySocketDataHandlerGroupFetcher {

    EzySocketDataHandlerGroup getDataHandlerGroup(EzySession session);
}
