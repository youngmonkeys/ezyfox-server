package com.tvd12.ezyfoxserver.setting;

import static com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting.*;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventPluginControllersImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "plugin")
@JsonIgnoreProperties(value = {"zoneId"}, ignoreUnknown = true)
@JsonPropertyOrder({"id", "name", "entryLoader", "priority", "listenEvents"})
public class EzySimplePluginSetting extends EzyAbstractSetting implements EzyPluginSetting {

	@XmlElement(name = "priority")
	protected int priority;
	
	@XmlElement(name = "listen-events")
	protected EzySimpleListenEvents listenEvents = new EzySimpleListenEvents();
	
	private static final AtomicInteger COUNTER = new AtomicInteger(0);
	
	@Override
	protected EzyEventControllers newEventControllers() {
		return EzyEventPluginControllersImpl.builder().build();
	}
	
	@Override
	protected AtomicInteger getIdCounter() {
		return COUNTER;
	}
	
	@Override
    public String getLocation() {
        return Paths.get(homePath, PLUGINS, name).toString();
    }
    
	@Getter
	@ToString
	@XmlAccessorType(XmlAccessType.NONE)
	@XmlRootElement(name = "listen-events")
	public static class EzySimpleListenEvents implements EzyListenEvents {
	    protected Set<EzyConstant> events = new HashSet<>();
	    
	    @XmlElement(name = "event")
	    public void setEvent(String string) {
	        events.add(EzyEventType.valueOf(string));
	    }
	    
	}
	
}
