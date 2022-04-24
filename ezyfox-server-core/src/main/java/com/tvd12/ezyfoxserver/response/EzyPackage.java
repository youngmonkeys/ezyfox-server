package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;

import java.util.Collection;

public interface EzyPackage extends EzyReleasable {

    EzyArray getData();

    boolean isEncrypted();

    EzyConstant getTransportType();

    Collection<EzySession> getRecipients(EzyConstant connectionType);

}
