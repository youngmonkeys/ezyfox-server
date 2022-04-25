package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.util.EzyReleasable;
import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySession;

public interface EzySocketRequest extends EzyReleasable {

    EzyArray getData();

    EzyCommand getCommand();

    EzySession getSession();

    boolean isSystemRequest();
}
