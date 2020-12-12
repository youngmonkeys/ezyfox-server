package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.util.EzyDestroyable;
import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleZone extends EzyComponent implements EzyZone, EzyDestroyable {

    protected EzyZoneSetting setting;
    protected EzyZoneUserManager userManager;
    
    @Override
    public void destroy() {
        super.destroy();
        this.userManager.clear();
        this.userManager = null;
        this.setting = null;
    }
    
    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleZone>()
                .function(t -> t.setting)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(setting).toHashCode();
    }
    
    @Override
    public String toString() {
        return setting.getName() + "(id = " + setting.getId() + ")";
    }

}
