package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public interface EzyStreamingRequest extends EzyReleasable {

    EzyUser getUser();

    EzySession getSession();

    byte[] getBytes();

}
