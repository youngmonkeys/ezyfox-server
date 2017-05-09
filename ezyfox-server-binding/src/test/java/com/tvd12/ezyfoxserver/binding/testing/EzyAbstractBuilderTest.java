package com.tvd12.ezyfoxserver.binding.testing;

import static org.testng.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.List;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractBuilder;
import com.tvd12.ezyfoxserver.binding.impl.EzyObjectElementsFetcher;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyReflectElement;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;
import com.tvd12.test.base.BaseTest;

public class EzyAbstractBuilderTest extends BaseTest {

	@Test
	public void test() throws Exception {
		Field name = A.class.getDeclaredField("name");
		EzyField field = new EzyField(name);
		EzyElementBuilder builder = new EzyElementBuilder(new EzyClass(A.class));
		assertEquals(builder.getFieldName(field), "name");
		builder.getElementType(null);
	}
	
	public static class A {
		public String name;
	}
	
	@SuppressWarnings("rawtypes")
	public static class EzyElementBuilder extends EzyAbstractBuilder {
		
		public EzyElementBuilder(EzyClass clazz) {
			super(clazz);
		}
		
		@Override
		public Class getElementType(Object element) {
			return super.getElementType(element);
		}
		
		@Override
		protected int getAccessType(EzyClass clazz) {
			return EzyAccessType.ALL;
		}
		
		@Override
		protected EzyObjectElementsFetcher newElementsFetcher() {
			return new EzyObjectElementsFetcher() {

				@Override
				protected List<? extends EzyMethod> getMethods(EzyClass clazz) {
					return clazz.getGetterMethods();
				}

				@Override
				protected List<? extends EzyMethod> getDeclaredMethods(EzyClass clazz) {
					return clazz.getDeclaredGetterMethods();
				}
				
				@Override
				protected EzyMethod newByFieldMethod(EzyMethod method) {
					return new EzySetterMethod(method);
				}
				
				@Override
				protected boolean isValidAnnotatedMethod(EzyMethod method) {
					return method.getParameterCount() == 1;
				}
				
			};
		}
		
		@Override
		public String getFieldName(EzyReflectElement element) {
			return super.getFieldName(element);
		}

	}
	
}
