package com.tvd12.ezyfoxserver.setting;

import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
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
    
    @XmlElement(name = "max-sessions")
    protected int maxSessions = 999999;
    
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
	
	@XmlElement(name = "session-management")
	protected EzySimpleSessionManagementSetting sessionManagement = new EzySimpleSessionManagementSetting();
	
	@Setter(AccessLevel.NONE)
    protected EzySimpleZonesSetting zones = new EzySimpleZonesSetting();
    
    @XmlElement(name = "zones")
    protected EzySimpleZoneFilesSetting zoneFiles = new EzySimpleZoneFilesSetting();

    //==================== apps ================//
    public void addZone(EzySimpleZoneSetting zone) {
        this.zones.setItem(zone);
    }
    
	@JsonIgnore
	@Override
	public Set<String> getZoneNames() {
		return zones.getZoneNames();
	}
	
	@JsonIgnore
	@Override
	public Set<Integer> getZoneIds() {
		return zones.getZoneIds();
	}
	
	@Override
	public EzySimpleZoneSetting getZoneByName(String name) {
		return zones.getZoneByName(name);
	}
	
	@Override
	public EzySimpleZoneSetting getZoneById(Integer id) {
		return zones.getZoneById(id);
	}
	//=============================================//
	
}
