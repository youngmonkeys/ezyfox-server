package com.tvd12.ezyfoxserver.netty.handler;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.socket.EzyChannel;
import com.tvd12.ezyfoxserver.socket.EzyImmediateDataSender;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleHandlerGroup 
		extends EzyLoggable 
		implements EzyHandlerGroup, EzyImmediateDataSender {

	protected final EzyChannel channel;
	
	protected EzySimpleHandlerGroup(Builder builder) {
		this.channel = builder.channel;
	}
	
	@Override
	public void fireDataSend(Object data) throws Exception {
		channel.write(data);
	}

	@Override
	public void sendDataNow(Object data, EzyTransportType type) {
		try {
			channel.write(data);
		} catch (Exception e) {
			getLogger().warn("write data {} to channel {} error", data, channel);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzySimpleHandlerGroup> {
		
		protected EzyChannel channel;
		
		public Builder channel(EzyChannel channel) {
			this.channel = channel;
			return this;
		}
		
		@Override
		public EzySimpleHandlerGroup build() {
			return new EzySimpleHandlerGroup(this);
		}
		
	}
	
	
}
