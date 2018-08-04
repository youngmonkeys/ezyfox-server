package com.tvd12.ezyfoxserver.response;

import java.util.Collection;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyPackage extends EzyReleasable {

    EzyArray getData();
    
    EzyConstant getTransportType();
    
    Collection<EzySession> getRecipients(EzyConstant connectionType);
    
}
