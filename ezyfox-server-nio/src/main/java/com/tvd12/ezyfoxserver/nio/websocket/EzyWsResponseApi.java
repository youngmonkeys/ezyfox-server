package com.tvd12.ezyfoxserver.nio.websocket;

import com.tvd12.ezyfox.codec.EzyObjectToStringEncoder;
import com.tvd12.ezyfox.codec.EzySimpleStringDataEncoder;
import com.tvd12.ezyfox.codec.EzyStringDataEncoder;
import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfoxserver.api.EzyAbstractResponseApi;
import com.tvd12.ezyfoxserver.constant.EzyConnectionType;

public class EzyWsResponseApi extends EzyAbstractResponseApi {

	protected final EzyStringDataEncoder encoder;
	
	public EzyWsResponseApi(Object encoder) {
		this.encoder = new EzySimpleStringDataEncoder((EzyObjectToStringEncoder)encoder);
	}
	
	@Override
	protected Object encodeData(EzyArray data) throws Exception {
		Object answer = encoder.encode(data, String.class);
		return answer;
	}
	
	@Override
	protected EzyConstant getConnectionType() {
		return EzyConnectionType.WEBSOCKET;
	}
	
}
