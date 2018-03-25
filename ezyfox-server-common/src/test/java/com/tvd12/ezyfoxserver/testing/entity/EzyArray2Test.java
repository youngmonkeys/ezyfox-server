package com.tvd12.ezyfoxserver.testing.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.testing.CommonBaseTest;

import lombok.Setter;

public class EzyArray2Test extends CommonBaseTest {

	@Test
	public void test() {
		EzyArray array = newArrayBuilder()
				.append((Object)null)
				.append("abc")
				.build();
		assert !array.isEmpty();
		System.out.println(array);
		
		XArray xArray = new XArray();
		assert xArray.isEmpty();
		xArray.setSize(100);
		assert !xArray.isEmpty();
		assert !xArray.isNotNullValue(10);
	}
	
	private static class XArray implements EzyArray {
		private static final long serialVersionUID = -5111743233496608397L;
		
		@Setter
		private int size;

		@Override
		public <T> T get(int index) {
			return null;
		}

		@Override
		public <T> T get(int index, Class<T> type) {
			return null;
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public Object getValue(int index, Class type) {
			return null;
		}
		
		@Override
		public boolean isNotNullValue(int index) {
			return false;
		}

		@Override
		public EzyArray sub(int fromIndex, int toIndex) {
			return null;
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public <T> List<T> toList() {
			return null;
		}

		@Override
		public <T> List<T> toList(Class<T> type) {
			return null;
		}

		@Override
		public <T, A> A toArray(Class<T> type) {
			return null;
		}

		@Override
		public void add(Object item) {
		}

		@Override
		public void add(Object... items) {
		}

		@SuppressWarnings("rawtypes")
		@Override
		public void add(Collection items) {
		}

		@Override
		public <T> T set(int index, Object item) {
			return null;
		}

		@Override
		public <T> T remove(int index) {
			return null;
		}

		@Override
		public void forEach(Consumer<Object> action) {
		}

		@Override
		public Iterator<Object> iterator() {
			return null;
		}

		@Override
		public EzyArray duplicate() {
			return null;
		}
		
		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
		
	}
	
	
}
