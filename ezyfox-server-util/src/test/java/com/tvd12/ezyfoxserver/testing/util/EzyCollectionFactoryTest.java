package com.tvd12.ezyfoxserver.testing.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.util.EzyCollectionFactory;
import com.tvd12.test.base.BaseTest;

public class EzyCollectionFactoryTest extends BaseTest {

	@SuppressWarnings({ "unused", "rawtypes" })
	@Test
	public void test() {
		EzyCollectionFactory factory = new EzyCollectionFactory();
		Collection coll = factory.newCollection(Collection.class);
		List list = factory.newCollection(List.class);
		ArrayList arrayList = factory.newCollection(ArrayList.class);
		LinkedList linkedList = factory.newCollection(LinkedList.class);
		CopyOnWriteArrayList copyOnWriteArrayList = factory.newCollection(CopyOnWriteArrayList.class);
		Set set = factory.newCollection(Set.class);
		HashSet hashSet = factory.newCollection(HashSet.class);
		LinkedHashSet linkedHashSet = factory.newCollection(LinkedHashSet.class);
		CopyOnWriteArraySet copyOnWriteArraySet = factory.newCollection(CopyOnWriteArraySet.class);
		Vector vector = factory.newCollection(Vector.class);
		Queue queue = factory.newCollection(Queue.class);
		Stack stack = factory.newCollection(Stack.class);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		EzyCollectionFactory factory = new EzyCollectionFactory();
		factory.newCollection(Integer.class);
	}
	
}
