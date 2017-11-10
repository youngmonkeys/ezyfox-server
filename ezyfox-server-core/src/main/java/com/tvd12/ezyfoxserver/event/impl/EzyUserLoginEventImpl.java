package com.tvd12.ezyfoxserver.event.impl;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.event.EzyUserLoginEvent;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyUserLoginEventImpl extends EzySimpleSessionEvent implements EzyUserLoginEvent {

	protected EzyArray data;
	protected String username;
	protected String password;
	protected EzyData output;
	
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
		
		public Builder output(EzyData output) {
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
		
		@Override
		public EzyUserLoginEvent build() {
		    return (EzyUserLoginEvent)super.build();
		}
		
		@Override
		protected EzyUserLoginEventImpl newProduct() {
		    EzyUserLoginEventImpl answer = new EzyUserLoginEventImpl();
		    answer.setData(data);
		    answer.setOutput(output);
		    answer.setUsername(username);
		    answer.setPassword(password);
		    return answer;
		}
	}
	
}
