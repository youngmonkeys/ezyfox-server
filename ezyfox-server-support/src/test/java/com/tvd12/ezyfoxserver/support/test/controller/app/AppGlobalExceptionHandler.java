package com.tvd12.ezyfoxserver.support.test.controller.app;

import com.tvd12.ezyfox.core.annotation.EzyExceptionHandler;
import com.tvd12.ezyfox.core.annotation.EzyRequestData;
import com.tvd12.ezyfox.core.annotation.EzyTryCatch;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.event.EzyUserSessionEvent;
import com.tvd12.ezyfoxserver.support.test.controller.Hello;
import com.tvd12.ezyfoxserver.support.test.exception.RequestException;

@EzyExceptionHandler
public class AppGlobalExceptionHandler extends EzyLoggable {

    @EzyTryCatch(RequestException.class)
    public void handleRequestException(RequestException e,
            EzyUserSessionEvent event,
            boolean boolValue,
            int intValue,
            Void voidData,
            String cmd,
            Hello data,
            String stringValue) {
        logger.error("try cath RequestException, data: {}", data, e);
    }

    @EzyTryCatch(IllegalArgumentException.class)
    public void handleIllegalArgumentException(
            IllegalArgumentException e,
            String cmd,
            @EzyRequestData Object data) {
        logger.error("try cath IllegalArgumentException, cmd = {}, data = {}", cmd, data, e);
    }

    @EzyTryCatch(Exception.class)
    public void handleException(
            Exception e,
            String cmd,
            @EzyRequestData Object data) throws Exception {
        throw e;
    }
}
