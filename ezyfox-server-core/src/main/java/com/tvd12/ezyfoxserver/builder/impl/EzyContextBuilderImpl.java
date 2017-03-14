package com.tvd12.ezyfoxserver.builder.impl;

import java.util.ArrayList;
import java.util.Collection;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.builder.EzyContextBuilder;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleAppContext;
import com.tvd12.ezyfoxserver.context.EzySimpleContext;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;
import com.tvd12.ezyfoxserver.service.impl.EzyResponseSerializerImpl;

public class EzyContextBuilderImpl implements EzyContextBuilder<EzyContextBuilderImpl> {

	private EzyServer boss;
	
	public static EzyContextBuilderImpl builder() {
		return new EzyContextBuilderImpl();
	}
	
	@Override
	public EzyServerContext build() {
		EzySimpleContext context = new EzySimpleContext();
		context.setBoss(boss);
		context.addAppContexts(newAppContexts(context));
		context.setProperty(EzyResponseSerializer.class, newResponseSerializer());
		return context;
	}
	
	protected Collection<EzyAppContext> newAppContexts(EzyContext parent) {
		Collection<EzyAppContext> contexts = new ArrayList<>();
		for(Integer appId : boss.getAppIds())
			contexts.add(newAppContext(parent, boss.getAppById(appId)));
		return contexts;
	}
	
	protected EzyAppContext newAppContext(EzyContext parent, EzyApp app) {
		EzySimpleAppContext appContext = new EzySimpleAppContext();
		appContext.setApp(app);
		appContext.setParent(parent);
		return appContext;
	}
	
	protected EzyResponseSerializer newResponseSerializer() {
		return EzyResponseSerializerImpl.builder().build();
	}
	
	@Override
	public EzyContextBuilderImpl boss(EzyServer boss) {
		this.boss = boss;
		return this;
	}

}
