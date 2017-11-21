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
	private static final long serialVersionUID = 9176938015407765002L;

	private Long userId;
	private String username;
	
	public ExampleUser(String username) {
		this.username = username;
	}

	@Override
	public String getId() {
		return username;
	}
	
}
