package com.tvd12.ezyfoxserver.rabbitmq;

import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcResponseEntity;

public interface EzyRpcClient {

	/**
	 * 
	 * sync call
	 * 
	 * @param <T> the value type
	 * @return the returned value
	 */
	EzyRpcResponseEntity sync(EzyRpcProcedure procedure);
	
}
