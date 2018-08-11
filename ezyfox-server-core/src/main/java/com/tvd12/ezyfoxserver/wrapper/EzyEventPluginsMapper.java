package com.tvd12.ezyfoxserver.wrapper;

import java.util.Set;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

public interface EzyEventPluginsMapper {

    Set<EzyPluginSetting> getPlugins(EzyConstant eventType);
    
}
