package com.tvd12.ezyfoxserver.rabbitmq.entity;

public interface EzyRpcValueProcedure extends EzyRpcProcedure {

	/**
	 * @return the return type
	 */
	@SuppressWarnings("rawtypes")
	Class getReturnType();
	
}
