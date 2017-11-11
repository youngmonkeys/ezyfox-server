package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyDisconnectResponse extends EzyBaseResponse implements EzyResponse {

	protected EzyConstant reason;
	
	protected EzyDisconnectResponse(Builder builder) {
	    super(builder);
	    this.reason = builder.reason;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.DISCONNECT;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(reason.getId())
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyBaseResponse.Builder<Builder> {
	    protected EzyConstant reason;
		
		public Builder reason(EzyConstant reason) {
			this.reason = reason;
			return this;
		}
		
		public EzyDisconnectResponse build() {
		    return new EzyDisconnectResponse(this);
		}
	}

}
