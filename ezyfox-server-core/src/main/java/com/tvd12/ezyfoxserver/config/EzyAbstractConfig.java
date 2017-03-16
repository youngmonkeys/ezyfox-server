package com.tvd12.ezyfoxserver.config;

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
public abstract class EzyAbstractConfig implements EzyEventControllerAdder {

	protected final int id;
	
	@XmlElement(name = "name")
	protected String name;
	
	@XmlElement(name = "entry-loader")
	protected String entryLoader;
	
	protected int numThreads;
	
	@JsonIgnore
	protected transient EzyEventControllers eventControllers;
	
	{
		numThreads = 30;
		eventControllers = newEventControllers();
	}
	
	public EzyAbstractConfig() {
		this.id = getIdCounter().incrementAndGet();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void addEventController(EzyConstant eventType, EzyEventController ctrl) {
		eventControllers.addController(eventType, ctrl);
	}
	
	protected abstract AtomicInteger getIdCounter();
	protected abstract EzyEventControllers newEventControllers();
	
}
