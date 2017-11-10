package com.tvd12.ezyfoxserver.testing.util;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.util.EzyKeyValueAnnotations;
import com.tvd12.test.base.BaseTest;

public class EzyKeyValueAnnotationsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyKeyValueAnnotations.class;
	}
	
	@Test
	public void test() {
		Map<String, String> map = EzyKeyValueAnnotations
				.getProperties(ClassE.class.getAnnotation(EzyAutoImpl.class)
						.properties());
		assert map.get("foo").equals("bar");
	}
	
	@EzyAutoImpl(properties = {
			@EzyKeyValue(key = "foo", value = "bar")
	})
	public interface ClassE {
		
	}
	
}
