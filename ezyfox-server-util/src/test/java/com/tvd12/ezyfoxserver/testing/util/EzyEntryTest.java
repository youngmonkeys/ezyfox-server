package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyEntry;
import com.tvd12.test.base.BaseTest;

public class EzyEntryTest extends BaseTest {

	@Test
	public void test() {
		EzyEntry<String, String> ab = EzyEntry.of("a", "b");
		
		assert ab.getKey().equals("a");
		assert ab.getValue().equals("b");
		
		EzyEntry<String, String> bc = new EzyEntry<>();
		bc.setKey("b");
		assert bc.setValue("c") == null;
		assert !ab.equals(bc);
		assert ab.hashCode() != bc.hashCode();
		
		EzyEntry<String, String> abe = EzyEntry.of("a", "b");
		assert ab.equals(abe);
		assert ab.hashCode() == abe.hashCode();
	}
	
}
