package com.tvd12.ezyfoxserver.config;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
@JsonPropertyOrder({"id", "name", "entryLoader", "priority"})
public class EzyPlugin extends EzyAbstractConfig {

	@XmlElement(name = "priority")
	protected int priority;
	
	private static final AtomicInteger COUNTER;
	
	static {
		COUNTER = new AtomicInteger(0);
	}
	
	@Override
	protected EzyEventControllers newEventControllers() {
		return EzyEventPluginControllersImpl.builder().build();
	}
	
	@Override
	protected AtomicInteger getIdCounter() {
		return COUNTER;
	}
	
}
