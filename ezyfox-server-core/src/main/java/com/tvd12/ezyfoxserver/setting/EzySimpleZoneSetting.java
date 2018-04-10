package com.tvd12.ezyfoxserver.setting;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.ezyfoxserver.util.EzyInitable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "zone")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzySimpleZoneSetting implements EzyZoneSetting, EzyInitable {

    protected int id = COUNTER.incrementAndGet();
    
    protected String name;
    
    protected String configFile;
    
    @XmlElement(name = "max-users")
    protected int maxUsers = 999999;
	
	@XmlElement(name = "user-management")
	protected EzySimpleUserManagementSetting userManagement = new EzySimpleUserManagementSetting();
	
	@XmlElement(name = "plugins")
    protected EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
    
    @XmlElement(name = "applications")
    protected EzySimpleAppsSetting applications = new EzySimpleAppsSetting();
    
    private static final AtomicInteger COUNTER = new AtomicInteger(0);
    
    @Override
    public void init() {
        plugins.setZoneId(id);
        applications.setZoneId(id);
    }
	
	//==================== apps ================//
	@JsonIgnore
	public Set<String> getAppNames() {
		return applications.getAppNames();
	}
	
	@JsonIgnore
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
	@JsonIgnore
	@Override
	public Set<String> getPluginNames() {
		return plugins.getPluginNames();
	}
	
	@JsonIgnore
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
	
}
