package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.entity.EzyArray;

public class EzyLoginResponse extends EzyBaseResponse implements EzyResponse {

	protected long userId;
	protected Object data;
	protected String username;
	protected EzyArray joinedApps;
	
	protected EzyLoginResponse(Builder builder) {
	    super(builder);
	    this.data = builder.data;
	    this.userId = builder.userId;
	    this.username = builder.username;
	    this.joinedApps = builder.joinedApps;
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.LOGIN;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(userId)
				.append(username)
				.append(joinedApps)
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyBaseResponse.Builder<Builder> {
	    protected long userId;
	    protected Object data;
	    protected String username;
	    protected EzyArray joinedApps;
		
		public Builder userId(long userId) {
			this.userId = userId;
			return this;
		}
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder joinedApps(EzyArray joinedApps) {
            this.joinedApps = joinedApps;
            return this;
        }
		
		@Override
		public EzyResponse build() {
		    return new EzyLoginResponse(this);
		}
		
	}

}
