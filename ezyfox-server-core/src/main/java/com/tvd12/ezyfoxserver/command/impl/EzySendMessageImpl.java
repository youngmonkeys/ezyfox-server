package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzySendMessageImpl 
        extends EzyHasSenderCommand<EzySendMessageImpl> 
        implements EzySendMessage {

	protected EzyData data;
	
	@Override
	public Boolean execute() {
		try {
			sender.send(data);
			getLogger().debug("send to {} data {}", getSenderName(), data);
			return Boolean.TRUE;
		}
		catch(Exception e) {
			getLogger().error("error when send data " + data, e);
		}
		return Boolean.FALSE;
	}

	@Override
	public EzySendMessage data(EzyData data) {
		this.data = data;
		return this;
	}
	
}
