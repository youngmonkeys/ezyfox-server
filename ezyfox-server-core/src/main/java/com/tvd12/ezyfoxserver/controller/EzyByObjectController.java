package com.tvd12.ezyfoxserver.controller;

import com.tvd12.ezyfoxserver.entity.EzyObject;

public interface EzyByObjectController extends EzyController<EzyObject> {
	
	@Override
	default Class<EzyObject> getDataType() {
		return EzyObject.class;
	}
	
}
