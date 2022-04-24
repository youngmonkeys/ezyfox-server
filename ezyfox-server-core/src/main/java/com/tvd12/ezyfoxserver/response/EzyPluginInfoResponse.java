package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.constant.EzyCommand;

public class EzyPluginInfoResponse
    extends EzySimpleParamsResponse<EzyPluginInfoParams> {
    private static final long serialVersionUID = 4124109074522892904L;

    public EzyPluginInfoResponse(EzyPluginInfoParams params) {
        super(EzyCommand.PLUGIN_INFO, params);
    }
}
