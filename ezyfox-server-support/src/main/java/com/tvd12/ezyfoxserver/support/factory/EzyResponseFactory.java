package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfoxserver.support.command.EzyArrayResponse;
import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;

public interface EzyResponseFactory {

    EzyArrayResponse newArrayResponse();

    EzyObjectResponse newObjectResponse();

}
