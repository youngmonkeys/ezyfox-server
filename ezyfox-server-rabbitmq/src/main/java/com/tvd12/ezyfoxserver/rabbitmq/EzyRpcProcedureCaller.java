package com.tvd12.ezyfoxserver.rabbitmq;

import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.exception.EzyRpcException;

public interface EzyRpcProcedureCaller {

	Object call(EzyRpcProcedure procedure) throws EzyRpcException;
	
}
