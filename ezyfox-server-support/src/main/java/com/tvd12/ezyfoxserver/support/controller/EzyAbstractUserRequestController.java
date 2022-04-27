package com.tvd12.ezyfoxserver.support.controller;

import com.tvd12.ezyfox.core.constant.EzyResponseCommands;
import com.tvd12.ezyfox.core.exception.EzyBadRequestException;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.util.EzyLoggable;

import static com.tvd12.ezyfox.util.EzyEntityArrays.newArray;

public abstract class EzyAbstractUserRequestController
    extends EzyLoggable
    implements EzyCommandsAware {

    protected EzyData newErrorData(EzyBadRequestException e) {
        EzyData errorData = newArray(e.getCode(), e.getReason());
        EzyData data = newArray(EzyResponseCommands.ERROR, errorData);
        return data;
    }
}
