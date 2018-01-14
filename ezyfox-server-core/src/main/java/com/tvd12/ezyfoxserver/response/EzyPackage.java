package com.tvd12.ezyfoxserver.response;

import java.util.Collection;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.util.EzyReleasable;

public interface EzyPackage extends EzyReleasable {

    EzyArray getData();
    
    EzyConstant getTransportType();
    
    Collection<EzySession> getRecipients(EzyConstant connectionType);
    
}
