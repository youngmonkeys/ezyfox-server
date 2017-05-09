package com.tvd12.ezyfoxserver.testing.entity;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.testing.CommonBaseTest;
import static org.testng.Assert.*;

public class EzyArray4Test extends CommonBaseTest {

	@Test
	public void test1() {
		EzyArray array = newArrayBuilder()
				.append(new Boolean[][] {{true, false}, {false, true}})
				.build();
		assertEquals(array.toArray(Boolean[].class), new Boolean[][] {{true, false}, {false, true}});
	}
	
	public void test2() {
		EzyArray array = newArrayBuilder()
				.append(new Byte[][] {{1,2},{3,4}})
				.build();
		assertEquals(array.toArray(Byte[].class), new Byte[][] {{1,2},{3,4}});
	}
	
	@Test
	public void test3() {
		EzyArray array = newArrayBuilder()
				.append(new Character[][] {{'a', 'b'}, {'c', 'd'}})
				.build();
		assertEquals(array.toArray(Character[].class), new Character[][] {{'a', 'b'}, {'c', 'd'}});
	}
	
	@Test
	public void test4() {
		EzyArray array = newArrayBuilder()
				.append(new Double[][] {{1D,2D},{3D,4D}})
				.build();
		assertEquals(array.toArray(Double[].class), new Double[][] {{1D,2D},{3D,4D}});
	}
	
	@Test
	public void test5() {
		EzyArray array = newArrayBuilder()
				.append(new Float[][] {{1F,2F}, {3F,4F}})
				.build();
		assertEquals(array.toArray(Float[].class), new Float[][] {{1F,2F}, {3F,4F}});
	}
	
	@Test
	public void test6() {
		EzyArray array = newArrayBuilder()
				.append(new Integer[][] {{1,2}, {3,4}})
				.build();
		assertEquals(array.toArray(Integer[].class), new Integer[][] {{1,2}, {3,4}});
	}
	
	@Test
	public void test7() {
		EzyArray array = newArrayBuilder()
				.append(new Long[][] {{1L,2L}, {3L,4L}})
				.build();
		assertEquals(array.toArray(Long[].class), new Long[][] {{1L,2L}, {3L,4L}});
	}
	
	@Test
	public void test8() {
		EzyArray array = newArrayBuilder()
				.append(new Short[][] {{1,2}, {3,4}})
				.build();
		assertEquals(array.toArray(Short[].class), new Short[][] {{1,2}, {3,4}});
	}
	
	@Test
	public void test9() {
		EzyArray array = newArrayBuilder()
				.append(new String[][] {{"1","2"}, {"3","4"}})
				.build();
		assertEquals(array.toArray(String[].class), new String[][] {{"1","2"}, {"3","4"}});
	}
	
	
}
