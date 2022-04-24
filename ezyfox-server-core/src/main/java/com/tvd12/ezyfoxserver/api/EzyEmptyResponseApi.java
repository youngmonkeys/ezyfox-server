package com.tvd12.ezyfoxserver.api;

import com.tvd12.ezyfoxserver.response.EzyPackage;

public final class EzyEmptyResponseApi implements EzyResponseApi {

    private static final EzyEmptyResponseApi INSTANCE = new EzyEmptyResponseApi();

    private EzyEmptyResponseApi() {}

    public static EzyEmptyResponseApi getInstance() {
        return INSTANCE;
    }

    @Override
    public void response(EzyPackage pack, boolean immediate) throws Exception {}
}
