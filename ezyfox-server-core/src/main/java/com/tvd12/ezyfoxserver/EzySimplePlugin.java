package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimplePlugin 
        extends EzyChildComponent 
        implements EzyPlugin, EzyDestroyable {

    protected EzyPluginSetting setting;
    
    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimplePlugin>()
                .function(t -> t.setting)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes()
                .append(setting)
                .hashCode();
    }
    
}
