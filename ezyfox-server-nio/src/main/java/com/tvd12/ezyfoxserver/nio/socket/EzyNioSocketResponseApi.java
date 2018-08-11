package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioDataEncoder;
import com.tvd12.ezyfoxserver.nio.handler.EzyNioObjectToByteEncoder;
import com.tvd12.ezyfoxserver.nio.handler.EzySimpleNioDataEncoder;

public class EzyNioSocketResponseApi extends EzyAbstractResponseApi {

	protected final EzyNioDataEncoder encoder;
	
	protected EzyNioSocketResponseApi(Builder builder) {
		this.encoder = new EzySimpleNioDataEncoder(
				(EzyNioObjectToByteEncoder)builder.encoder);
	}
	
	@Override
	protected Object encodeData(EzyArray data) throws Exception {
		return encoder.encode(data);
	}
	
	@Override
	protected EzyConstant getConnectionType() {
		return EzyConnectionType.SOCKET;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyNioSocketResponseApi> {
		
		private Object encoder;
		
		public Builder encoder(Object encoder) {
			this.encoder = encoder;
			return this;
		}
		
		@Override
		public EzyNioSocketResponseApi build() {
			return new EzyNioSocketResponseApi(this);
		}
		
	}
	
}
