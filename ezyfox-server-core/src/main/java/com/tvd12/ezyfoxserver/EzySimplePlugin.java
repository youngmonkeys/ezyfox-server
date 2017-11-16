package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimplePlugin 
        extends EzyChildComponent 
        implements EzyPlugin, EzyDestroyable {

    protected EzyPluginSetting setting;
    
}
