package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Person implements Serializable {
	private static final long serialVersionUID = -6731210894904620373L;
	
	private String name;
	private int age;

}
