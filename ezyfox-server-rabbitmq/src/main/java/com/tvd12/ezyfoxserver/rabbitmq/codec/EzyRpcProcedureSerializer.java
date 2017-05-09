package com.tvd12.ezyfoxserver.rabbitmq.codec;

import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;

public interface EzyRpcProcedureSerializer {

	byte[] serialize(EzyRpcProcedure procedure);
	
}
