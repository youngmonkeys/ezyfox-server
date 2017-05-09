package com.tvd12.ezyfoxserver.testing.util;

import java.math.BigInteger;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.performance.Performance;

import lombok.Getter;
import lombok.Setter;

public class EzyEqualsAndHashCodeTest extends BaseTest {

	@Test
	public void test() {
		ClassA classA = new ClassA();
		classA.setId(10);
		classA.setName("name");
		assert new ClassA().equals(null) == false;
		assert classA.equals(classA);
		assert classA.equals(new Object()) == false;
		assert new ClassA().equals(new ClassA());
		assert new ClassA().hashCode() == new ClassA().hashCode();
		assert classA.equals(new ClassA()) == false;
		
		ClassA classA1 = new ClassA();
		classA1.setName("name");
		assert classA1.equals(new ClassA()) == false;
		assert new ClassA().equals(classA1) == false;
		
		ClassA classA2 = new ClassA();
		classA2.setName("name");
		classA2.setValue("value");
		classA2.setObject(new BigInteger("123"));
		
		ClassA classA3 = new ClassA();
		classA3.setName("name");
		classA3.setValue("value");
		classA3.setObject(new BigInteger("123"));
		
		assert classA3.equals(classA2);
		
		assert classA2.hashCode() == classA2.hashCode();
		assert classA3.hashCode() == classA2.hashCode();
	}
	
	@Test
	public void testEqualPerformance() {
		ClassA classA2 = new ClassA();
		classA2.setName("name");
		classA2.setValue("value");
		classA2.setObject(new BigInteger("123"));
		
		ClassA classA3 = new ClassA();
		classA3.setName("name");
		classA3.setValue("value");
		classA3.setObject(new BigInteger("123"));
		
		long time = Performance.create()
			.loop(1000000)
			.test(() -> {
				classA2.equals(classA3);
			})
			.getTime();
		System.out.println("equals.time = " + time);
	}
	
	@Test
	public void testHashCodePerformance() {
		ClassA classA2 = new ClassA();
		classA2.setName("name");
		classA2.setValue("value");
		classA2.setObject(new BigInteger("123"));
		
		long time = Performance.create()
			.loop(1000000)
			.test(() -> {
				classA2.hashCode();
			})
			.getTime();
		System.out.println("hashcode.time = " + time);
	}
	
	@Setter
	@Getter
	public static class ClassA {
		private int id;
		private String name;
		private String value;
		private Object object;
		
		@Override
		public boolean equals(Object obj) {
			return new EzyEquals<ClassA>()
					.function(c -> c.id)
					.function(c -> c.name)
					.function(c -> c.value)
					.function(c -> c.object)
					.isEquals(this, obj);
		}
		
		@Override
		public int hashCode() {
			return new EzyHashCodes()
					.initial(17)
					.prime(37)
					.append(id, name)
					.append(value)
					.append(object)
					.toHashCode();
		}
	}
	
}
