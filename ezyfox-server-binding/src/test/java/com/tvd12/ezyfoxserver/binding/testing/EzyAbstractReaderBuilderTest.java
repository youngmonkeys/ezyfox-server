package com.tvd12.ezyfoxserver.binding.testing;

import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractReaderBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyElementsFetcher;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectElementsFetcher;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractReaderBuilderTest extends BaseTest {
	
	@Test(expectedExceptions = {IllegalStateException.class})
	public void test1() {
		new Builder1(new EzyClass(getClass())).build();
	}

	public static class Builder1 extends EzyAbstractReaderBuilder {
		public Builder1(EzyClass clazz) {
			super(clazz);
		}
		
		@Override
		protected int getAccessType(EzyClass clazz) {
			return EzyAccessType.ALL;
		}
		
		@Override
		protected EzyElementsFetcher newElementsFetcher() {
			return new EzyObjectElementsFetcher() {

				@Override
				protected List<? extends EzyMethod> getMethods(EzyClass clazz) {
					return clazz.getSetterMethods();
				}

				@Override
				protected List<? extends EzyMethod> getDeclaredMethods(EzyClass clazz) {
					return clazz.getDeclaredSetterMethods();
				}
				
				@Override
				protected boolean isValidAnnotatedMethod(EzyMethod method) {
					return method.getParameterCount() == 1;
				}
				
				@Override
				protected EzyMethod newByFieldMethod(EzyMethod method) {
					return new EzySetterMethod(method);
				}
			};
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		protected EzyReader make() throws Exception {
			throw new Exception();
		}

		@Override
		protected String getImplClassName() {
			return "";
		}

		@Override
		protected String makeImplMethodContent(EzyMethod readMethod) {
			return "";
		}

		@Override
		protected boolean isDebug() {
			return false;
		}
		
	}
	
}
