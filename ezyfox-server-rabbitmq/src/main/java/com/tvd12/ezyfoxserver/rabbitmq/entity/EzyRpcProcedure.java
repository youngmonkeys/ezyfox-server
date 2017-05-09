package com.tvd12.ezyfoxserver.rabbitmq.entity;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyRpcProcedure {

	/**
	 * @return the procedure name
	 */
	String getName();
	
	/**
	 * @return the parent class or namespace 
	 */
	String getParent();
	
	/**
	 * @return the arguments
	 */
	EzyArray getArguments();
	
}
