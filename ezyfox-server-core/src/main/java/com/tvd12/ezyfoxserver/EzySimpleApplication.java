package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyAppUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventAppControllersImpl;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleApplication 
        extends EzyChildComponent 
        implements EzyApplication, EzyDestroyable {

    protected EzyAppSetting setting;
    protected EzyAppUserManager userManager;
    
    @Override
    protected EzyEventControllers newEventControllers() {
        return EzyEventAppControllersImpl.builder().build();
    }
    
    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleApplication>()
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
