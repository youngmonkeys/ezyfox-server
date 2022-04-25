package com.tvd12.ezyfoxserver.socket;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyReleasable;

public interface EzyPacket extends EzyReleasable {

    Object getData();

    boolean isBinary();

    boolean isReleased();

    boolean isFragmented();

    void setFragment(Object fragment);

    EzyConstant getTransportType();

    int getSize();
}
