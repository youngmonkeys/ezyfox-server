package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySessionDisconnectionManager<S extends EzySession> {

    void addDisconnectedSession(S session, EzyConstant reason);
    
}
