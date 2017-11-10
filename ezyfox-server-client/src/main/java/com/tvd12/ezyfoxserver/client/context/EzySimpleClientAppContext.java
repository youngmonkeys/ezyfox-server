package com.tvd12.ezyfoxserver.client.context;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.cmd.EzySendRequest;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequestAppRequest;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EzySimpleClientAppContext
		extends EzyEntity
		implements EzyClientAppContext {

	protected int appId;
	protected String appName;
	protected EzyClientContext parent;
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Class<T> clazz) {
		Object answer = parent.get(clazz);
		if(answer == null)
			answer = getProperty(clazz);
		return (T)answer;
	}
	
	@Override
	public EzyClientUser getMe() {
		return getParent().getMe();
	}
	
	@Override
	public void sendRequest(Object requestId, EzyData params) {
		get(EzySendRequest.class)
			.sender(getMe())
			.request(newRequest(requestId, params))
			.execute();
	}
	
	@Override
	public void sendPluginRequest(String pluginName, EzyArray data) {
		parent.sendPluginRequest(pluginName, data);
	}
	
	private EzyRequest newRequest(Object requestId, EzyData params) {
		return EzyRequestAppRequest.builder()
			.appId(appId)
			.data(newArrayBuilder()
					.append(requestId)
					.append(params)
					.build())
			.build();
	}
	
	@Override
	public void destroy() {
		this.parent = null;
		this.properties.clear();
	}
	
	protected EzyArrayBuilder newArrayBuilder() {
		return EzyEntityFactory.create(EzyArrayBuilder.class);
	}
}
