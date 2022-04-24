package com.tvd12.ezyfoxserver.event;

import com.tvd12.ezyfox.constant.EzyConstant;

public interface EzyUserRemovedEvent extends EzyUserEvent {

    EzyConstant getReason();

}
