package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySocketDisconnection extends EzyReleasable {

    EzySession getSession();
    
    EzyConstant getDisconnectReason();
    
}
