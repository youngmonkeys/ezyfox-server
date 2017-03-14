package com.tvd12.ezyfoxserver.command.impl;

import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzySender;

public class EzySendMessageImpl extends EzyAbstractCommand implements EzySendMessage {

	private EzyData data;
	private EzySender sender;
	
	@Override
	public Boolean execute() {
		try {
			sender.send(data);
			getLogger().debug("send data {} to client", data);
			return Boolean.TRUE;
		}
		catch(Exception e) {
			getLogger().error("error when send data " + data, e);
		}
		return Boolean.FALSE;
	}

	@Override
	public EzySendMessage sender(EzySender sender) {
		this.sender = sender;
		return this;
	}

	@Override
	public EzySendMessage data(EzyData data) {
		this.data = data;
		return this;
	}
	
}
