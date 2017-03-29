package com.tvd12.ezyfoxserver.command.impl;

import java.util.Set;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyFireAppEvent;
import com.tvd12.ezyfoxserver.command.EzyFireEvent;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.event.EzyEvent;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EzyFireAppEventImpl extends EzyAbstractCommand implements EzyFireAppEvent {

	private EzyServerContext context;
	
	@Override
	public void fire(EzyConstant type, EzyEvent event) {
		fireAppsEvent(type, event);
	}
	
	protected void fireAppsEvent(EzyConstant type, EzyEvent event) {
		getAppIds().forEach((appId) -> fireAppEvent(appId, type, event));
	}
	
	protected void fireAppEvent(int appId, EzyConstant type, EzyEvent event) {
		fireAppEvent(context.getAppContext(appId), type, event);
	}
	
	protected void fireAppEvent(EzyAppContext ctx, EzyConstant type, EzyEvent event) {
		ctx.get(EzyFireEvent.class).fire(type, event);
	}
	
	protected Set<Integer> getAppIds() {
		return getServer().getAppIds();
	}
	
	protected EzyServer getServer() {
		return context.getBoss();
	}
}
