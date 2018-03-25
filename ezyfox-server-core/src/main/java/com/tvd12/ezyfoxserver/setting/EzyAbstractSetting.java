package com.tvd12.ezyfoxserver.setting;

import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.controller.EzyEventController;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.NONE)
public abstract class EzyAbstractSetting 
        implements EzyBaseSetting, EzyHomePathAware {

	protected final int id = newId();
	
	@XmlElement(name = "name")
	protected String name;
	
	@XmlElement(name = "entry-loader")
	protected String entryLoader;
	
	@XmlElement(name = "thread-pool-size")
	protected int threadPoolSize = 0;
	
	@XmlElement(name = "config-file")
	protected String configFile = "config.properties";
	
	@JsonIgnore
	protected String homePath = "";
	
	@JsonIgnore
	protected String location = "";
	
	@JsonIgnore
	protected final EzyEventControllers eventControllers = newEventControllers();
	
	protected int newId() {
	    return getIdCounter().incrementAndGet();
	}
	
	@Override
    public String getConfigFile() {
        return Paths.get(getLocation(), configFile).toString();
    }
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addEventController(EzyConstant eventType, EzyEventController ctrl) {
		eventControllers.addController(eventType, ctrl);
	}
	
	protected abstract AtomicInteger getIdCounter();
	protected abstract EzyEventControllers newEventControllers();
	
}
