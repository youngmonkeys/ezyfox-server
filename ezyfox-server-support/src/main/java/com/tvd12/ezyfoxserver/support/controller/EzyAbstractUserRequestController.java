package com.tvd12.ezyfoxserver.support.controller;

import static com.tvd12.ezyfox.util.EzyEntityArrays.newArray;

import com.tvd12.ezyfox.core.constant.EzyResponseCommands;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyLoggable;

public class EzyAbstractUserRequestController extends EzyLoggable {
	
	protected EzyData newErrorData(EzyBadRequestException e) {
		EzyData errorData = newArray(e.getCode(), e.getReason());
		EzyData data = newArray(EzyResponseCommands.ERROR, errorData);
		return data;
	}
	
}
