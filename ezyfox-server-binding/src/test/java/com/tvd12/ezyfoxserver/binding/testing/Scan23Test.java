package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.tvd12.ezyfoxserver.binding.impl.EzyObjectWriterBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzySimpleBindingContext;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2Object;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2ObjectReader;
import com.tvd12.ezyfoxserver.binding.testing.scan2.Scan2ObjectWriter;
import com.tvd12.ezyfoxserver.binding.testing.scan3.Scan3Array;
import com.tvd12.test.base.BaseTest;

public class Scan23Test extends BaseTest {

	@Test
	public void test() {
		EzyObjectWriterBuilder.setDebug(true);
		EzySimpleBindingContext.builder()
			.scan("com.tvd12.ezyfoxserver.binding.testing.scan2", "com.tvd12.ezyfoxserver.binding.testing.scan3")
			.addClasses(Scan2Object.class, Scan3Array.class)
			.addObjectBindingClass(Scan2Object.class)
			.addArrayBindingClass(Scan3Array.class)
			.addTemplateClass(Integer.class)
			.addTemplateClasses(Lists.newArrayList(Scan2ObjectReader.class, Scan2ObjectWriter.class))
			.build();
	}
	
}
