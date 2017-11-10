package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.util.EzyEnums;
import com.tvd12.test.base.BaseTest;

import lombok.Getter;

public class EzyEnumsTest extends BaseTest {
	
	@Override
	public Class<?> getTestClass() {
		return EzyEnums.class;
	}
	
	@Test
	public void test1() {
		assert EzyEnums.valueOf(ABC.values(), 1) == ABC.A;
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		assert EzyEnums.valueOf(ABC.values(), 10) == ABC.A;
	}

	public static enum ABC implements EzyConstant {
		
		A(1),
		B(2),
		C(3);
		
		@Getter
		private int id;
		
		private ABC(int id) {
			this.id = id;
		}
		
	}
	
}
