package com.tvd12.ezyfoxserver.wrapper;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyAppUserManager extends EzyUserManager {

    void removeUser(EzyUser user, EzyConstant reason);
    
    String getAppName();
    
}
