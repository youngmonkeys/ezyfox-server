package com.tvd12.ezyfoxserver.testing.entity;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyRoObject;
import com.tvd12.test.base.BaseTest;

import lombok.Setter;

public class EzyObject6Test extends BaseTest {

	@Test
	public void test() {
		XObject x = new XObject();
		assert x.isEmpty();
		x.setSize(100);
		assert !x.isEmpty();
		assert !x.isNotNullValue("");
	}
	
	@SuppressWarnings("serial")
	public static class XObject implements EzyRoObject {
		
		@Setter
		private int size;
		
		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}

		@Override
		public XObject duplicate() {
			return null;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public boolean containsKey(Object key) {
			return false;
		}
		
		@Override
		public boolean isNotNullValue(Object key) {
			return false;
		}

		@Override
		public <V> V get(Object key) {
			return null;
		}

		@Override
		public <V> V get(Object key, Class<V> clazz) {
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Object getValue(Object key, Class type) {
			return null;
		}

		@Override
		public Set<Object> keySet() {
			return null;
		}

		@Override
		public Set<Entry<Object, Object>> entrySet() {
			return null;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Map toMap() {
			return null;
		}
		
	}
	
}
