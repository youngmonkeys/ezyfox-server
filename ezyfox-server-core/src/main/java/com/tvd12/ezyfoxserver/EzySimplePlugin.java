package com.tvd12.ezyfoxserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.plugin.EzyPluginRequestController;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimplePlugin 
        extends EzyChildComponent 
        implements EzyPlugin, EzyDestroyable {

    protected EzyPluginSetting setting;
    @JsonIgnore
    protected EzyPluginRequestController requestController;
    
    public EzySimplePlugin() {
        this.requestController = EzyPluginRequestController.DEFAULT;
    }
    
    @Override
    public void destroy() {
        super.destroy();
        this.setting = null;
        this.requestController = null;
    }
    
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
