package com.tvd12.ezyfoxserver.rabbitmq.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class EzyRpcSimpleProcedure extends EzyRpcAbstractProcedure {

	@SuppressWarnings("rawtypes")
	private Class returnType;
	
}
