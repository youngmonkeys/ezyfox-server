package com.tvd12.ezyfoxserver.database.service;

public interface EzyUpdateOneService<I,E> extends
		EzyUpdateOneByIdService<I, E>,
		EzyUpdateOneByFieldService<E> {
}
