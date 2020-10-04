package com.tvd12.ezyfoxserver.support.test.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EzyObjectBinding
@NoArgsConstructor
@AllArgsConstructor
public class GreetRequest {

	protected String who;
	
}
