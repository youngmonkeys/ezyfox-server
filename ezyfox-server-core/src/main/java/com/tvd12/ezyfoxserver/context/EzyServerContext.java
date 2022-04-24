package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.response.EzyResponse;

import java.util.List;

public interface EzyServerContext extends EzyComplexContext {

    void sendNow(EzyResponse response, EzySession recipient);

    EzyServer getServer();

    EzyZoneContext getZoneContext(int zoneId);

    EzyZoneContext getZoneContext(String zoneName);

    List<EzyZoneContext> getZoneContexts();

}
