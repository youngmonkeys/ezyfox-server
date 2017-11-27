package com.tvd12.ezyfoxserver.identifier.testing.entity;

import com.tvd12.ezyfoxserver.annotation.EzyId;
import com.tvd12.ezyfoxserver.identifier.testing.annotation.HasIdTest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@HasIdTest
@AllArgsConstructor
public class Message2 {

	@EzyId
	private Long id;
	private String name;
	
}
