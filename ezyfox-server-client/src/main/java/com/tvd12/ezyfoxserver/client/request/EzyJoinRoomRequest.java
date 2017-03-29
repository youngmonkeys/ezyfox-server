package com.tvd12.ezyfoxserver.client.request;

import com.tvd12.ezyfoxserver.constant.EzyCommand;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyJoinRoomRequest extends EzyFixedCmdAppRequest {

	protected EzyJoinRoomRequest(Builder builder) {
		super(builder);
	}
	
	@Override
	public EzyConstant getCommand() {
		return EzyCommand.REQUEST_APP;
	}
	
	@Override
	public Object getData() {
		return newArrayBuilder()
				.append(getAppId())
				.append(data)
				.build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyFixedCmdAppRequest.Builder<Builder> {
		@Override
		protected EzyFixedCmdRequest newProduct() {
			return new EzyJoinRoomRequest(this);
		}
	}

}
