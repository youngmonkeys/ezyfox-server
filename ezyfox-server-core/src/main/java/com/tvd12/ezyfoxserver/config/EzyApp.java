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
@XmlRootElement(name = "application")
@JsonPropertyOrder({"id", "name", "entryLoader"})
public class EzyApp {

	protected final int id;
	
	@XmlElement(name = "name")
	protected String name;
	
	@XmlElement(name = "entry-loader")
	protected String entryLoader;
	
	protected int numThreads;
	
	private static final AtomicInteger COUNT = new AtomicInteger(0);
	
	{
		numThreads = 30;
	}
	
	public EzyApp() {
		this.id = COUNT.incrementAndGet();
	}
}
