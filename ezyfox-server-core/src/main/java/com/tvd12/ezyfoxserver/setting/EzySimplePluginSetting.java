package com.tvd12.ezyfoxserver.setting;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfoxserver.constant.EzyEventType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "plugin")
public class EzySimplePluginSetting extends EzyAbstractSetting implements EzyPluginSetting {

	@XmlElement(name = "priority")
	protected int priority;
	
	@XmlElement(name = "listen-events")
	protected EzySimpleListenEvents listenEvents = new EzySimpleListenEvents();
	
	private static final AtomicInteger COUNTER = new AtomicInteger(0);
	
	@Override
	protected AtomicInteger getIdCounter() {
		return COUNTER;
	}
	
	@Override
	protected String getParentFolder() {
	    return EzyFolderNamesSetting.PLUGINS;
	}
	
	@Override
    public Map<Object, Object> toMap() {
         Map<Object, Object> map = super.toMap();
         map.put("priority", priority);
         map.put("listenEvents", listenEvents.getEvents());
         return map;
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
