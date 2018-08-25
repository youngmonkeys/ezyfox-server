package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfoxserver.support.command.ArrayResponse;
import com.tvd12.ezyfoxserver.support.command.ObjectResponse;

public interface ResponseFactory {

	ArrayResponse newArrayResponse();
	
	ObjectResponse newObjectResponse();
	
}
