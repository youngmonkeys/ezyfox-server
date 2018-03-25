package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "settings")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EzySimpleSettings implements EzySettings {

    @XmlElement(name = "debug")
    protected boolean debug;
    
    @XmlElement(name = "max-users")
    protected int maxUsers = 999999;
    
    @XmlElement(name = "max-sessions")
    protected int maxSessions = 999999;
    
    @XmlElement(name = "thread-pool-size")
	protected int threadPoolSize = 0;
	
	@XmlElement(name = "http")
	protected EzySimpleHttpSetting http = new EzySimpleHttpSetting();
	
	@XmlElement(name = "socket")
	protected EzySimpleSocketSetting socket = new EzySimpleSocketSetting();
	
	@XmlElement(name = "administrators")
	protected EzySimpleAdminsSetting admins = new EzySimpleAdminsSetting();
	
	@XmlElement(name = "logger")
	protected EzySimpleLoggerSetting logger = new EzySimpleLoggerSetting();
	
	@XmlElement(name = "web-socket")
	protected EzySimpleWebSocketSetting websocket = new EzySimpleWebSocketSetting();
	
	@XmlElement(name = "user-management")
	protected EzySimpleUserManagementSetting userManagement = new EzySimpleUserManagementSetting();
	
	@XmlElement(name = "session-management")
	protected EzySimpleSessionManagementSetting sessionManagement = new EzySimpleSessionManagementSetting();
	
	@XmlElement(name = "plugins")
    protected EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
    
    @XmlElement(name = "applications")
    protected EzySimpleAppsSetting applications = new EzySimpleAppsSetting();
	
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
	public Set<String> getPluginNames() {
		return plugins.getPluginNames();
	}
	
	@JsonIgnore
	public Set<Integer> getPluginIds() {
		return plugins.getPluginIds();
	}
	
	public EzySimplePluginSetting getPluginByName(String name) {
		return plugins.getPluginByName(name);
	}
	
	public EzySimplePluginSetting getPluginById(Integer id) {
		return plugins.getPluginById(id);
	}
	//=============================================//
	
}
