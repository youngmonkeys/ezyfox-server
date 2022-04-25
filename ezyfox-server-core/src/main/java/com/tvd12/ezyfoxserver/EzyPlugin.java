package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public interface EzyPlugin extends EzyServerChild {

    EzyPluginSetting getSetting();

    EzyPluginRequestController getRequestController();
}
