package com.tvd12.ezyfoxserver.command;

import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySender;

public interface EzySendMessage extends EzyCommand<Boolean> {

	EzySendMessage sender(EzySender sender);
	
	EzySendMessage data(EzyData data);
	
}
