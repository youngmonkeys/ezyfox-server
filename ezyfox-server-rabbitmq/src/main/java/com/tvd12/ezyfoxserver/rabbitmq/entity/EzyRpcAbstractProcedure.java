package com.tvd12.ezyfoxserver.rabbitmq.entity;

import com.tvd12.ezyfoxserver.entity.EzyArray;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class EzyRpcAbstractProcedure implements EzyRpcProcedure {

	protected String name;
	protected String parent;
	protected EzyArray arguments;

}
