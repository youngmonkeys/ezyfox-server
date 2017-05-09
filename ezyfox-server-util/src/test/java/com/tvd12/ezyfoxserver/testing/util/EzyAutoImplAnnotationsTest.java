package com.tvd12.ezyfoxserver.testing.util;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.util.EzyAutoImplAnnotations;
import com.tvd12.test.base.BaseTest;

public class EzyAutoImplAnnotationsTest extends BaseTest {

	@Override
	public Class<?> getTestClass() {
		return EzyAutoImplAnnotations.class;
	}
	
	@Test
	public void test() {
		assert EzyAutoImplAnnotations.getBeanName(ClassA.class).equals("classA");
		assert EzyAutoImplAnnotations.getBeanName(ClassB.class).equals("b");
		assert EzyAutoImplAnnotations.getBeanName(ClassC.class).equals("c");
		assert EzyAutoImplAnnotations.getBeanName(ClassD.class).equals("classD");
		assert EzyAutoImplAnnotations.getBeanName(ClassE.class).equals("classE");
	}
	
	@EzyAutoImpl
	public interface ClassA {
		
	}
	
	@EzyAutoImpl("b")
	public interface ClassB {
		
	}
	
	@EzyAutoImpl(properties = {
			@EzyKeyValue(key = "name", value = "c")
	})
	public interface ClassC {
		
	}
	
	@EzyAutoImpl(properties = {
			@EzyKeyValue(key = "name", value = "")
	})
	public interface ClassD {
		
	}
	
	@EzyAutoImpl(properties = {
			@EzyKeyValue(key = "no no no", value = "e")
	})
	public interface ClassE {
		
	}
	
}
