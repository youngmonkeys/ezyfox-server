package com.tvd12.ezyfoxserver.nio.wrapper.impl;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.nio.entity.EzyNioSession;
import com.tvd12.ezyfoxserver.nio.factory.EzyNioSessionFactory;
import com.tvd12.ezyfoxserver.nio.wrapper.EzyNioSessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class EzyNioSessionManagerImpl 
		extends EzySimpleSessionManager<EzyNioSession> 
		implements EzyNioSessionManager {

	protected EzyNioSessionManagerImpl(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzySimpleSessionManager.Builder<EzyNioSession> {
		
		@Override
		public EzyNioSessionManagerImpl build() {
			return new EzyNioSessionManagerImpl(this);
		}

		@Override
		protected EzyObjectFactory<EzyNioSession> newObjectFactory() {
			return new EzyNioSessionFactory();
		}
	}
	
	
}
