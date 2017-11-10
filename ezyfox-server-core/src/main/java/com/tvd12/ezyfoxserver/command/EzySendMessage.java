package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySender;

public interface EzySendMessage extends EzyCommand<Boolean> {

	EzySendMessage data(EzyData data);
	
	EzySendMessage sender(EzySender sender);
	
	default EzySendMessage data(EzyBuilder<? extends EzyData> builder) {
	    return data(builder.build());
	}
	
}
