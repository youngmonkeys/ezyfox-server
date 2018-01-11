package com.tvd12.ezyfoxserver.testing.entity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyHashMap;
import com.tvd12.test.base.BaseTest;

public class EzyHashMapTest extends BaseTest {
	
	@Test
	public void test() {
		EzyHashMap map = new ExEzyHashMap();
		map.put("1", "2");
		assert map.isNotNullValue("1");
		assert !map.isNotNullValue("2");
	}
	

	public static class ExEzyHashMap extends EzyHashMap {
		private static final long serialVersionUID = -5733872612446105669L;
		
		@SuppressWarnings("unchecked")
		@Override
		public <V> V put(Object key, Object value) {
			return (V) map.put(key, value);
		}
		
	}
	
}
