package com.tvd12.ezyfoxserver.client.controller;

import com.tvd12.ezyfoxserver.client.EzyClientContext;
import com.tvd12.ezyfoxserver.client.request.EzyJoinRoomRequest;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyUser;

public class EzyAccessAppController 
		extends EzyAbstractController 
		implements EzyClientController<EzyUser> {

	@Override
	public void handle(EzyClientContext ctx, EzyUser user, EzyArray data) {
		getLogger().info("access app success, appId = " + data.get(0));
		sendJoinRoomRequest(ctx, user, data.get(0));
		getLogger().info("sended join room request");
	}
	
	protected void sendJoinRoomRequest(EzyClientContext ctx, EzyUser user, int appId) {
		ctx.get(EzySendMessage.class)
			.sender(user)
			.data(serializeToArray(ctx, newJoinRoomRequest(appId)))
			.execute();
	}
	
	protected EzyRequest newJoinRoomRequest(int appId) {
		return EzyJoinRoomRequest.builder()
				.appId(appId)
				.data(newJoinRoomData())
				.build();
	}
	
	protected EzyArray newJoinRoomData() {
		return newArrayBuilder().append(1).append("lobby").build();
	}

}
