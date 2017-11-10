package com.tvd12.ezyfoxserver.database.service;

public interface EzyUpdateService<I,E> extends 
		EzyUpdateOneService<I, E>,
		EzyUpdateManyService<E> {
}
