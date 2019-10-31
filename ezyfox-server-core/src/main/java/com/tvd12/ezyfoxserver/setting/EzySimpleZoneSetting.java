package com.tvd12.ezyfoxserver.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tvd12.ezyfox.util.EzyEquals;
import com.tvd12.ezyfox.util.EzyHashCodes;
import com.tvd12.ezyfox.util.EzyInitable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "zone")
public class EzySimpleZoneSetting implements EzyZoneSetting, EzyInitable {

    protected final int id = COUNTER.incrementAndGet();
    
    protected String name;
    
    protected String configFile;
    
    @XmlElement(name = "max-users")
    protected int maxUsers = 999999;
    
    @XmlElement(name = "streaming")
    protected EzySimpleStreamingSetting streaming = new EzySimpleStreamingSetting();
	
	@XmlElement(name = "plugins")
    protected EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
    
    @XmlElement(name = "applications")
    protected EzySimpleAppsSetting applications = new EzySimpleAppsSetting();
    
    @XmlElement(name = "user-management")
    protected EzySimpleUserManagementSetting userManagement = new EzySimpleUserManagementSetting();
    
    @XmlElement(name = "event-controllers")
    protected EzySimpleEventControllersSetting eventControllers = new EzySimpleEventControllersSetting();
    
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    
    @Override
    public void init() {
        plugins.setZoneId(id);
        applications.setZoneId(id);
    }
	
	//==================== apps ================//
	public Set<String> getAppNames() {
		return applications.getAppNames();
	}
	
	public Set<Integer> getAppIds() {
		return applications.getAppIds();
	}
	
	public EzySimpleAppSetting getAppByName(String name) {
		return applications.getAppByName(name);
	}
	
	public EzySimpleAppSetting getAppById(Integer id) {
		return applications.getAppById(id);
	}
	//=============================================//
	
	//==================== plugins ================//
	@Override
	public Set<String> getPluginNames() {
		return plugins.getPluginNames();
	}
	
	@Override
	public Set<Integer> getPluginIds() {
		return plugins.getPluginIds();
	}
	
	@Override
	public EzySimplePluginSetting getPluginByName(String name) {
		return plugins.getPluginByName(name);
	}
	
	@Override
	public EzySimplePluginSetting getPluginById(Integer id) {
		return plugins.getPluginById(id);
	}
	//=============================================//
	
	@Override
    public boolean equals(Object obj) {
        return new EzyEquals<EzySimpleZoneSetting>()
                .function(t -> t.id)
                .isEquals(this, obj);
    }
    
    @Override
    public int hashCode() {
        return new EzyHashCodes().append(id).toHashCode();
    }
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("configFile", configFile);
        map.put("maxUsers", maxUsers);
        map.put("streaming", streaming.toMap());
        map.put("userManagement", userManagement.toMap());
        map.put("eventControllers", eventControllers.toMap());
        map.put("plugins", plugins.toMap());
        map.put("applications", applications.toMap());
        return map;
    }
	
}
