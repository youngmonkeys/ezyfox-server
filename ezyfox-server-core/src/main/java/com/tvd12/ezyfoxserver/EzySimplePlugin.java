package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimplePlugin implements EzyPlugin {

    protected EzyPluginSetting setting;
    
}
