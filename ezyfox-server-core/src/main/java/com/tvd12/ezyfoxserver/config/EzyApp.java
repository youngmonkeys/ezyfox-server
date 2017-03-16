package com.tvd12.ezyfoxserver.config;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventAppControllersImpl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "application")
@JsonPropertyOrder({"id", "name", "entryLoader"})
public class EzyApp extends EzyAbstractConfig {

	private static final AtomicInteger COUNTER;
	
	static {
		COUNTER = new AtomicInteger(0);
	}
	
	protected EzyEventControllers newEventControllers() {
		return EzyEventAppControllersImpl.builder().build();
	}
	
	@Override
	protected AtomicInteger getIdCounter() {
		return COUNTER;
	}
}
