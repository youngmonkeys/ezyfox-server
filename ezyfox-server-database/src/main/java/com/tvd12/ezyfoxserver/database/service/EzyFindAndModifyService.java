package com.tvd12.ezyfoxserver.database.service;

public interface EzyFindAndModifyService<I,E> extends 
		EzyFindAndModifyByIdService<I, E>,
		EzyFindAndModifyByFieldService<E> {
}
