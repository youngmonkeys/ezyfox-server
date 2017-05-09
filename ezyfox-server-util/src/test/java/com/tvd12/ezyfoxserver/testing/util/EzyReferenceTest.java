package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyReference;
import com.tvd12.test.base.BaseTest;

public class EzyReferenceTest extends BaseTest {

	@Test
	public void test() {
		assert (new EzyReference() {
			
			@Override
			public void retain() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public int getReferenceCount() {
				return 0;
			}
		}.releasable());
		
		assert (new EzyReference() {
			
			@Override
			public void retain() {
			}
			
			@Override
			public void release() {
			}
			
			@Override
			public int getReferenceCount() {
				return 1;
			}
		}.releasable()) == false;
	}
	
}
