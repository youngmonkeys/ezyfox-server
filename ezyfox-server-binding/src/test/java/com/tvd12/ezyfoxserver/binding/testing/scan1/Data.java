package com.tvd12.ezyfoxserver.binding.testing.scan1;

import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

import lombok.ToString;

@ToString
@EzyObjectBinding
@EzyArrayBinding
public class Data {

	public String data1 = "data1";
	public String data2 = "data2";
	public String data3 = "data2";
	
}
