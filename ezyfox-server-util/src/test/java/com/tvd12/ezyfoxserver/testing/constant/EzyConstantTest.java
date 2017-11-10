package com.tvd12.ezyfoxserver.testing.constant;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.test.base.BaseTest;

public class EzyConstantTest extends BaseTest {

	@Test
	public void test() {
		EzyConstant a = EzyConstant.one();
		System.err.println("a.id = " + a.getId() + ", a.name = " + a.getName());
		
		EzyConstant b = EzyConstant.one("b");
		System.err.println("b.id = " + b.getId() + ", b.name = " + b.getName());
		
		EzyConstant c = EzyConstant.one("c");
		System.err.println("c.id = " + c.getId() + ", c.name = " + c.getName());
		
		MyConstant d = new MyConstant();
		System.err.println("d.id = " + c.getId() + ", d.name = " + d.getName());
	}
	
	public static class MyConstant implements EzyConstant {

		@Override
		public int getId() {
			return 0;
		}
	}
	
}
