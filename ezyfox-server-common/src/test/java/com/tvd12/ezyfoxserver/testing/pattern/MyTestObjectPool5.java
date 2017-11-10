package com.tvd12.ezyfoxserver.testing.pattern;

import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.pattern.EzyObjectPool;

public class MyTestObjectPool5 extends EzyObjectPool<MyTestObject> {

	protected MyTestObjectPool5(Builder builder) {
		super(builder);
		this.initializeObjects();
	}
	
	public MyTestObject borrowOne() {
		return borrowObject();
	}
	
	public void returnOne(MyTestObject object) {
		returnObject(object);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzyObjectPool.Builder<MyTestObject, Builder> {

		@Override
		public MyTestObjectPool5 build() {
			return new MyTestObjectPool5(this);
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
