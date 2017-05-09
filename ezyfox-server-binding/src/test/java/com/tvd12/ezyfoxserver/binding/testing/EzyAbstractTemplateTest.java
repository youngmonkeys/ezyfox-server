package com.tvd12.ezyfoxserver.binding.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractTemplate;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractTemplateTest extends BaseTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test() {
		new EzyAbstractTemplate() {
			@Override
			public Object read(EzyUnmarshaller unmarshaller, Object value) {
				return null;
			}
			
			@Override
			public Object write(EzyMarshaller marshaller, Object object) {
				return null;
			}
		};
	}
	
}
