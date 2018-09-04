package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.app.EzyAppRequestController;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;

public interface EzyApplication extends EzyServerChild {

    EzyAppSetting getSetting();
    
    EzyAppUserManager getUserManager();
    
    EzyAppRequestController getRequestController();
}
