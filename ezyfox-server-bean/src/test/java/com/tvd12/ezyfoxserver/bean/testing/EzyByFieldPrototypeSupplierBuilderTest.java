package com.tvd12.ezyfoxserver.bean.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.bean.impl.EzyByFieldPrototypeSupplierLoader;
import com.tvd12.ezyfoxserver.bean.impl.EzySimplePrototypeFactory;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyFields;

public class EzyByFieldPrototypeSupplierBuilderTest {

	public ClassA classA;
	
	@Test
	public void test() throws Exception {
		EzyByFieldPrototypeSupplierLoader.setDebug(true);
		
		EzyField field = new EzyField(EzyFields.getField(getClass(), "classA"));
		EzyByFieldPrototypeSupplierLoader builder = new EzyByFieldPrototypeSupplierLoader(
				field, this);
		builder.load(new EzySimplePrototypeFactory());
	}
	
	public static class ClassA {
		
	}
	
}
