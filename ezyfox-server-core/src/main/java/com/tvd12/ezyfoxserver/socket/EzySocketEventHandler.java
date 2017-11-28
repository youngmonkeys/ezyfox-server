package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfoxserver.util.EzyDestroyable;

public interface EzySocketEventHandler extends EzyDestroyable {

    void handleEvent();
}
