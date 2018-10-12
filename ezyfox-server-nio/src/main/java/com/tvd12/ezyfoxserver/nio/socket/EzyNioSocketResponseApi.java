package com.tvd12.ezyfoxserver.nio.socket;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.codec.EzyMessageDataEncoder;
import com.tvd12.ezyfox.codec.EzyObjectToByteEncoder;
import com.tvd12.ezyfox.codec.EzySimpleMessageDataEncoder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzyNioSocketResponseApi extends EzyAbstractResponseApi {

	protected final EzyMessageDataEncoder encoder;
	
	protected EzyNioSocketResponseApi(Builder builder) {
		this.encoder = new EzySimpleMessageDataEncoder(
				(EzyObjectToByteEncoder)builder.encoder);
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
