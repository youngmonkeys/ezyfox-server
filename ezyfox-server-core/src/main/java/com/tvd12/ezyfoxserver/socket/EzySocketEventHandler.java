package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.util.EzyDestroyable;

public interface EzySocketEventHandler extends EzyDestroyable {

    void handleEvent();
}
