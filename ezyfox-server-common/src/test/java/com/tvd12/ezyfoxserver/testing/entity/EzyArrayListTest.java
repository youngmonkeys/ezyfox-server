package com.tvd12.ezyfoxserver.testing.entity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArrayList;
import com.tvd12.test.base.BaseTest;

public class EzyArrayListTest extends BaseTest {
	
	@Test
	public void test() {
		EzyArrayList arr = new EzyEzyArrayList();
		arr.add((Object)null);
		arr.add("a");
		assert arr.isNotNullValue(1);
		assert !arr.isNotNullValue(0);
		assert !arr.isNotNullValue(100);
	}

	public static class EzyEzyArrayList extends EzyArrayList {
		private static final long serialVersionUID = 1L;
		
		@Override
		public void add(Object item) {
			list.add(item);
		}
		
	}
	
}
