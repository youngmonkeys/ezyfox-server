package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EzySimpleUserLoginEvent 
        extends EzySimpleSessionEvent 
        implements EzyUserLoginEvent {

	protected EzyArray data;
	@Setter
	protected String username;
	@Setter
	protected String password;
	@Setter
	protected EzyData output;
	
	protected EzySimpleUserLoginEvent(Builder builder) {
	    super(builder);
	    this.data = builder.data;
	    this.output = builder.output;
	    this.username = builder.username;
	    this.password = builder.password;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleSessionEvent.Builder<Builder> {
	    protected EzyArray data;
	    protected EzyData output;
	    protected String username;
	    protected String password;
		
		public Builder data(EzyArray data) {
			this.data = data;
			return this;
		}
		
		public Builder username(String username) {
			this.username = username;
			return this;
		}
		
		public Builder password(String password) {
			this.password = password;
			return this;
		}
		
		@Override
		public EzyUserLoginEvent build() {
		    return new EzySimpleUserLoginEvent(this);
		}
	}
	
}
