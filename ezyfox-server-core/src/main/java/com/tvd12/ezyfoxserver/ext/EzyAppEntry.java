package com.tvd12.ezyfoxserver.ext;

import com.tvd12.ezyfoxserver.context.EzyAppContext;

public interface EzyAppEntry extends EzyEntry {

    default void config(EzyAppContext ctx) {}
}
