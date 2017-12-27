package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzySocketRequest extends EzyReleasable {

    EzyArray getData();
    
    long getTimestamp();
    
    EzyCommand getCommand();
    
    EzySession getSession();
    
    boolean isSystemRequest();
    
}
