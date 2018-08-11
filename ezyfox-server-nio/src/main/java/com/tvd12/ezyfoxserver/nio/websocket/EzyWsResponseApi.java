package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzyWsResponseApi extends EzyAbstractResponseApi {

	protected final EzyWsDataEncoder encoder;
	
	protected EzyWsResponseApi(Builder builder) {
		this.encoder = new EzySimpleWsDataEncoder(
				(EzyWsObjectToByteEncoder)builder.encoder);
	}
	
	@Override
	protected Object encodeData(EzyArray data) throws Exception {
		return encoder.encode(data, String.class);
	}
	
	@Override
	protected EzyConstant getConnectionType() {
		return EzyConnectionType.WEBSOCKET;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyWsResponseApi> {
		
		private Object encoder;
		
		public Builder encoder(Object encoder) {
			this.encoder = encoder;
			return this;
		}
		
		@Override
		public EzyWsResponseApi build() {
			return new EzyWsResponseApi(this);
		}
		
	}
	
}
