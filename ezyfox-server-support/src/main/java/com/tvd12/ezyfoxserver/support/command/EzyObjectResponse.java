package com.tvd12.ezyfoxserver.support.command;

import com.tvd12.ezyfoxserver.support.command.EzyObjectResponse;

public interface EzyObjectResponse extends EzyResponse<EzyObjectResponse> {

    EzyObjectResponse param(Object key, Object value);

    EzyObjectResponse exclude(Object key);

}
