package com.tvd12.ezyfoxserver.response;

import java.util.Collection;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzyBytesPackage extends EzyReleasable {

    byte[] getBytes();
    
    EzyConstant getTransportType();
    
    Collection<EzySession> getRecipients(EzyConstant connectionType);
    
}
