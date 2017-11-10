package com.tvd12.ezyfoxserver.client.cmd;

import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.entity.EzySender;

public interface EzySendRequest extends EzyCommand<Boolean> {

	EzySendRequest sender(EzySender sender);
	
	EzySendRequest request(EzyRequest request);
	
}
