package com.tvd12.ezyfoxserver.rabbitmq.codec;

import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;

public interface EzyRpcProcedureDeserializer {

	EzyRpcProcedure deserialize(byte[] value);
	
}
