package com.tvd12.ezyfoxserver.config;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "plugin")
@JsonPropertyOrder({"id", "name", "entryLoader", "priority"})
public class EzyPlugin {

	private final int id;
	
	@XmlElement(name = "name")
	private String name;
	
	@XmlElement(name = "priority")
	private int priority;
	
	@XmlElement(name = "entry-loader")
	private String entryLoader;
	
	private static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyPlugin() {
		this.id = COUNT.incrementAndGet();
	}
	
	
}
