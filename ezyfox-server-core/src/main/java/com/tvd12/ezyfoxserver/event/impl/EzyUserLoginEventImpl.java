package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

import lombok.Getter;
import lombok.Setter;

public class EzyUserLoginEventImpl 
		extends EzyAbstractEvent 
		implements EzyUserLoginEvent {

	@Getter
	private Object data;
	@Getter
	@Setter
	private String username;
	@Setter
	@Getter
	private String password;
	@Setter
	@Getter
	private Object output;
	@Getter
	private EzySession session;
	
	protected EzyUserLoginEventImpl(Builder builder) {
		super(builder);
		this.data = builder.data;
		this.output = builder.output;
		this.session = builder.session;
		this.username = builder.username;
		this.password = builder.password;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyAbstractEvent.Builder<Builder> {
		private Object data;
		private Object output;
		private String username;
		private String password;
		private EzySession session;
		
		public Builder data(Object data) {
			this.data = data;
			return this;
		}
		
		public Builder output(Object output) {
			this.output = output;
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
		
		public Builder session(EzySession session) {
			this.session = session;
			return this;
		}
		
		@Override
		public EzyUserLoginEvent build() {
			return new EzyUserLoginEventImpl(this);
		}
	}
	
}
