package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;

public interface EzyRequestAppParams extends EzyRequestParams {

    int getAppId();

    EzyArray getData();

}
