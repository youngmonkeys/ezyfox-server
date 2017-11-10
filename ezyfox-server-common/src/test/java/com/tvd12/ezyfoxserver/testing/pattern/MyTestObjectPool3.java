package com.tvd12.ezyfoxserver.testing.pattern;

import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;

public class MyTestObjectPool3 extends EzyObjectPool<MyTestObject> {

	protected MyTestObjectPool3(Builder builder) {
		super(builder);
	}
	
	public MyTestObject borrowOne() {
		return borrowObject();
	}
	
	public void returnOne(MyTestObject object) {
		returnObject(object);
	}
	
	@Override
	protected boolean isStaleObject(MyTestObject object) {
		return object.getId() % 2 == 0;
	}
	
	@Override
	protected void removeStaleObjects() {
		throw new RuntimeException();
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyObjectPool.Builder<MyTestObject, Builder> {

		@Override
		public MyTestObjectPool3 build() {
			return new MyTestObjectPool3(this);
		}

		@Override
		protected String getProductName() {
			return "test-object";
		}

		@Override
		protected EzyObjectFactory<MyTestObject> newObjectFactory() {
			return new EzyObjectFactory<MyTestObject>() {
				@Override
				public MyTestObject newProduct() {
					return new MyTestObject();
				}
			};
		}
		
	}
	
}
