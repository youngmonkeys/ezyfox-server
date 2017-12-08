package com.tvd12.ezyfoxserver.identifier.testing.entity;

import com.tvd12.ezyfoxserver.annotation.EzyId;
import com.tvd12.ezyfoxserver.identifier.testing.annotation.HasIdTest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@HasIdTest
public class Message5 {

	@EzyId
	private long id;
	private String name;
	
}
