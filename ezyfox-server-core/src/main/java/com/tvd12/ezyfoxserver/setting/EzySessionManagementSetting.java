package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;
import com.tvd12.ezyfoxserver.constant.EzyMaxRequestPerSecondAction;

public interface EzySessionManagementSetting extends EzyToMap {

    long getSessionMaxIdleTime();

    long getSessionMaxWaitingTime();

    EzyMaxRequestPerSecond getSessionMaxRequestPerSecond();

    interface EzyMaxRequestPerSecond extends EzyToMap {

        int getValue();

        EzyMaxRequestPerSecondAction getAction();
    }

}
