package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyBaseSocketSetting extends EzyToMap {

    int getPort();

    String getAddress();

    boolean isActive();

    boolean isSslActive();

    String getCodecCreator();

}
