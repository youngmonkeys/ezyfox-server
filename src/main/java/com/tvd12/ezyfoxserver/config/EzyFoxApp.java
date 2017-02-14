package com.tvd12.ezyfoxserver.config;

import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@XmlRootElement(name = "application")
public class EzyFoxApp {

	private String name;
	private String entry;
	
	private final int id;
	
	private static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzyFoxApp() {
		this.id = COUNT.incrementAndGet();
	}
}
