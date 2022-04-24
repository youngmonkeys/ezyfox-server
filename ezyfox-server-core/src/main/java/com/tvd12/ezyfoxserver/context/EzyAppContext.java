package com.tvd12.ezyfoxserver.context;

import com.tvd12.ezyfoxserver.EzyApplication;

public interface EzyAppContext extends EzyZoneChildContext {

    EzyApplication getApp();
}
