package com.tvd12.ezyfoxserver.bean.testing.prototype;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.bean.impl.EzyByConstructorPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.bean.impl.EzySimplePrototypeFactory;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.test.base.BaseTest;

public class EzySimplePrototypeSupplierBuilderTest extends BaseTest {

	@Test(expectedExceptions = {IllegalStateException.class})
	public void test() {
		new ExSimplePrototypeSupplierBuilder(
				new EzyClass(ClassA.class)).load(new EzySimplePrototypeFactory());
	}
	
	public static class ExSimplePrototypeSupplierBuilder extends EzyByConstructorPrototypeSupplierLoader {

		public ExSimplePrototypeSupplierBuilder(EzyClass clazz) {
			super(clazz);
		}
		
		@Override
		protected EzyInstruction newConstructInstruction(EzyBody body, List<String> cparams) {
			throw new RuntimeException();
		}
		
	}
	
}
