package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzySocketUserRemoval extends EzyReleasable {

    EzyUser getUser();
    
    EzyConstant getReason();
    
    EzyZoneContext getZoneContext();
    
}
