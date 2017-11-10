package com.tvd12.ezyfoxserver.binding.testing.scan2;

import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyObjectBinding
public class Scan2A {

	private String a = "1";
	private boolean b = true;
	private byte c = 1;
	private char d = 'a';
	private double e = 2D;
	private float f = 3F;
	private long g = 4L;
	private short h = 5;
	

	public void setObject(Scan2Object object) {
		
	}
	
}
