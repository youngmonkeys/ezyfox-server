package com.tvd12.ezyfoxserver.testing.entity;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyEntity;
import com.tvd12.test.base.BaseTest;

public class EzyEntity1Test extends BaseTest {

	@Test
	public void test() {
		EzyEntity entity = new EzyEntity() {
		};
		Map<String, String> strs = new HashMap<>();
		strs.put("1", "a");
		entity.setProperties(strs);
		assert entity.getProperty("1").equals("a");
		assert entity.getProperty("1", String.class).equals("a");
		assert entity.containsKey("1");
		assert entity.getProperties().containsKey("1");
		entity.removeProperty("1");
		assert entity.getProperty("1") == null;
		entity.setProperty(EzyEntity1Test.class, this);
		assert entity.getProperty(EzyEntity1Test.class) == this;
	}
	
}
