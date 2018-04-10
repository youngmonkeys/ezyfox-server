package com.tvd12.ezyfoxserver;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;
import com.tvd12.ezyfoxserver.setting.EzyPluginSetting;
import com.tvd12.ezyfoxserver.setting.EzyZoneSetting;
import com.tvd12.ezyfoxserver.util.EzyDestroyable;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.wrapper.EzyEventPluginsMapper;
import com.tvd12.ezyfoxserver.wrapper.EzyZoneUserManager;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzySimpleZone implements EzyZone, EzyDestroyable {

    protected EzyZoneSetting setting;
    protected EzyZoneUserManager userManager;
    protected EzyEventPluginsMapper eventPluginsMapper;
    
    @JsonIgnore
    @Override
    public Set<Integer> getAppIds() {
        return setting.getAppIds();
    }
    
    @Override
    public EzyAppSetting getAppById(Integer id) {
        return setting.getAppById(id);
    }
    
    //==================== plugins ================//
    @JsonIgnore
    @Override
    public Set<Integer> getPluginIds() {
        return setting.getPluginIds();
    }
    
    @Override
    public EzyPluginSetting getPluginById(Integer id) {
        return setting.getPluginById(id);
    }
    //=============================================//
    
    @Override
    public void destroy() {
    }
    
    @Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleZone>()
                .function(t -> t.setting)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(setting).hashCode();
    }

}
