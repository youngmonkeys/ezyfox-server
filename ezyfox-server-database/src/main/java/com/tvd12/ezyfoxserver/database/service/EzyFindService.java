package com.tvd12.ezyfoxserver.database.service;

public interface EzyFindService<I,E> extends 
		EzyFindById<I, E>, 
		EzyFindListByIds<I, E>,
		EzyFindByField<E>,
		EzyFindListByField<E>,
		EzyFindAllService<E> {

}
