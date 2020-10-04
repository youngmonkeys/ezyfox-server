package com.tvd12.ezyfoxserver.support.test.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EzyObjectBinding(read = false)
public class GreetResponse {

	protected String message;
	
}
