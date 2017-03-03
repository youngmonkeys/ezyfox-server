package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.entity.EzyArray;

public interface EzyByArrayCotroller extends EzyController<EzyArray> {
	
	@Override
	default Class<EzyArray> getDataType() {
		return EzyArray.class;
	}
	
}
