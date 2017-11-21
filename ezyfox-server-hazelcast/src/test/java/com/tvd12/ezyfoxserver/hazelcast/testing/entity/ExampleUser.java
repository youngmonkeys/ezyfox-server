package com.tvd12.ezyfoxserver.hazelcast.testing.entity;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.util.EzyHasIdEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExampleUser implements EzyHasIdEntity<String>, Serializable {
	private static final long serialVersionUID = 975411654175906424L;
	
	private String username;
	
	@Override
	public String getId() {
		return username;
	}

}
