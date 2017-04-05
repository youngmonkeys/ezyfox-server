package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyHandShakeResponse extends EzyBaseResponse implements EzyResponse {

	private String publicKey;
	private String reconnectToken;
	
	protected EzyHandShakeResponse(Builder builder) {
		this.publicKey = builder.publicKey;
		this.reconnectToken = builder.reconnectToken;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.HAND_SHAKE;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(publicKey)
				.append(reconnectToken)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String publicKey;
		private String reconnectToken;
		
		public Builder publicKey(String publicKey) {
			this.publicKey = publicKey;
			return this;
		}
		
		public Builder reconnectToken(String reconnectToken) {
			this.reconnectToken = reconnectToken;
			return this;
		}
		
		public EzyHandShakeResponse build() {
			return new EzyHandShakeResponse(this);
		}
	}

}
