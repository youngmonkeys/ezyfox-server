package com.tvd12.ezyfoxserver.delegate;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyAppContextAware;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.event.EzyEvent;
import com.tvd12.ezyfoxserver.event.EzySimpleUserRemovedEvent;
import com.tvd12.ezyfoxserver.response.EzyExitedAppParams;
import com.tvd12.ezyfoxserver.response.EzyExitedAppResponse;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

import lombok.Setter;

@Setter
public class EzySimpleAppUserDelegate
        extends EzyLoggable
        implements EzyAppUserDelegate, EzyAppContextAware, EzyDestroyable {

    @Setter
    protected EzyAppContext appContext;
    
    @Override
    public void onUserRemoved(EzyUser user, EzyConstant reason) {
        try {
            handleUserRemovedEvent(user, reason);
        }
        catch (Exception e) {
            logger.warn("handle user {} error", user, e);
        }
        finally {
            responseUserRemoved(user, reason);
        }
    }
    
    protected void handleUserRemovedEvent(EzyUser user, EzyConstant reason) {
        EzyEvent event = new EzySimpleUserRemovedEvent(user, reason); 
        appContext.handleEvent(EzyEventType.USER_REMOVED, event);
    }
    
    protected void responseUserRemoved(EzyUser user, EzyConstant reason) {
        EzyResponse response = newExitedAppResponse(reason);
        EzyZoneContext zoneContext = appContext.getParent();
        zoneContext.send(response, user);
    }
    
    protected EzyResponse newExitedAppResponse(EzyConstant reason) {
        EzyExitedAppParams params = new EzyExitedAppParams();
        EzyAppSetting appSetting = appContext.getApp().getSetting();
        params.setApp(appSetting);
        params.setReason(reason);
        return new EzyExitedAppResponse(params);
    }
    
    @Override
    public void destroy() {
        this.appContext = null;
    }
    
}
