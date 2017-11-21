package com.tvd12.ezyfoxserver.hazelcast.testing.service;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.util.EzyHasIdEntity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "name")
public class Person implements EzyHasIdEntity<String>, Serializable {
	private static final long serialVersionUID = -3967779291148509736L;
	
	private String name;
	private int age;
	
	@Override
	public String getId() {
		return name;
	}
	
}
