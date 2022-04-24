package com.tvd12.ezyfoxserver.support.test.controller.plugin;

import com.tvd12.ezyfox.core.annotation.EzyDoHandle;
import com.tvd12.ezyfox.core.annotation.EzyRequestController;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.context.EzyPluginContext;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;
import com.tvd12.ezyfoxserver.support.test.exception.RequestException4;

@EzyRequestController("plugin")
public class PluginClientHelloRequestController {

    @EzyDoHandle("c_hello")
    public void handleHello(
            EzyPluginContext context,
            @EzyRequestData Hello data,
            EzyUser user) {
        System.out.println("plugin: c_hello: " + data.getWho());
    }

    @EzyDoHandle("requestException4")
    public void handleRequestException4(
            EzyContext context,
            String cmd,
            @EzyRequestData Hello data) throws Exception {
        throw new RequestException4(getClass().getSimpleName() + ":handleRequestException4, cmd = " + cmd);
    }

    @EzyTryCatch({RequestException4.class})
    public void handleRequestException2(
            RequestException4 e,
            String cmd,
            Hello request,
            EzyUser user, EzySession session, EzyContext context) {
        System.out.println("PluginClientHelloRequestController::handleRequestException2, cmd = " + cmd + ", data = " + request + ", e = " + e);
    }

}
