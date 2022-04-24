package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyToMap;

import java.util.Set;

public interface EzyLoggerSetting extends EzyToMap {

    EzyIgnoredCommandsSetting getIgnoredCommands();

    interface EzyIgnoredCommandsSetting {

        Set<EzyConstant> getCommands();

    }

}
