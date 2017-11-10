package com.tvd12.ezyfoxserver.nio.codec;

import java.nio.ByteBuffer;

import com.tvd12.ezyfoxserver.entity.EzyData;

public interface EzyDataToBytes {

	ByteBuffer convert(EzyData data);
	
}
