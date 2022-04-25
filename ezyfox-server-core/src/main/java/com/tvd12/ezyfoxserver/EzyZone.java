package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

public interface EzyZone extends EzyEventComponent {

    EzyZoneSetting getSetting();

    EzyZoneUserManager getUserManager();
}
