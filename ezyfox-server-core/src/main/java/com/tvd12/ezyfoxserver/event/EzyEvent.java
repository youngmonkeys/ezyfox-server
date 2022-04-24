package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.util.EzyReleasable;

public interface EzyEvent extends EzyReleasable {

    @Override
    default void release() {}
}
