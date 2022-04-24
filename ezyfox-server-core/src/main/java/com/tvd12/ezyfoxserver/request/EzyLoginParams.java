package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyData;

public interface EzyLoginParams extends EzyRequestParams {

    String getZoneName();

    String getUsername();

    String getPassword();

    EzyData getData();
}
