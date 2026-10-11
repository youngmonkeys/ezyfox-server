package com.tvd12.ezyfoxserver.protocol;

import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.setting.EzyProtocolSetting;
import com.tvd12.ezyfoxserver.ssl.EzySslContextProxy;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzyProtocolServerContext {

    private final EzyProtocolSetting setting;
    private final EzyProtocolGateway gateway;
    private final EzyServerContext serverContext;
    private final EzySslContextProxy sslContextProxy;
}
