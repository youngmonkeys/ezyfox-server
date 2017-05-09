package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.context.EzyClientAppContext;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.client.listener.EzyClientAppResponseListener;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;

public class EzyRequestAppController 
		extends EzyAbstractController 
		implements EzyClientController<EzyClientUser> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handle(EzyClientContext ctx, EzyClientUser user, EzyArray data) {
		int appId = data.get(0, int.class);
		EzyData responseData = getResponseData(data);
		Object requestId = getRequestId(responseData);
		EzyData responseParams = getReponseParams(responseData);
		EzyClientAppContext appContext = ctx.getAppContext(appId);
		EzyClientAppResponseListener listener = getAppResponseListener(ctx, requestId);
		if(listener != null)
			listener.execute(appContext, responseParams);
	}
	
	@SuppressWarnings("rawtypes")
	protected EzyClientAppResponseListener 
			getAppResponseListener(EzyClientContext ctx, Object requestId) {
		return ctx.getClient().getAppResponseListener(requestId);
	}
	
	protected EzyData getResponseData(EzyArray data) {
		return data.get(1, EzyArray.class);
	}
	
	protected Object getRequestId(EzyData data) {
		return ((EzyArray)data).get(0, String.class);
	}
	
	protected EzyData getReponseParams(EzyData data) {
		return ((EzyArray)data).get(1);
	}
	
	protected void sendChatMessageRequest(EzyClientAppContext appCtx) {
		EzyArray params = newArrayBuilder().append("nice to meet you!").build();
		appCtx.sendRequest("1", params);
	}
	
}
