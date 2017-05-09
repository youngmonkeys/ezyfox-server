package com.tvd12.ezyfoxserver.testing.reflect;

import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.reflect.EzyGenerics;
import com.tvd12.test.base.BaseTest;

public class EzyGenerics6Test extends BaseTest {

	@SuppressWarnings("rawtypes")
	@Test
	public void test1() {
		Class[] args = EzyGenerics.getGenericInterfacesArguments(
				InterfaceB.class, InterfaceA.class, 2);
		assertEquals(args, new Class[] {String.class, String.class});
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test2() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceA.class, InterfaceA.class, 2);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test3() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceB.class, InterfaceC.class, 2);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test4() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceB.class, InterfaceD.class, 2);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test5() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceB.class, InterfaceE.class, 2);
	}
	
	@Test
	public void test6() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceB.class, InterfaceF.class, 2);
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test7() {
		EzyGenerics.getGenericInterfacesArguments(
				InterfaceB1.class, InterfaceG.class, 2);
	}
	
	public static interface InterfaceA<T,R> {
	}
	
	public static interface InterfaceC {
	}
	
	public static interface InterfaceD<T> {
		
	}
	
	public static interface InterfaceE<T,R> {
		
	}
	
	public static interface InterfaceF<T,R> extends Map<String, String> {
		
	}
	
	public static interface InterfaceG<T,R> {
		
	}
	
	public static interface MyMap extends Map<String, String> {
		
	}
	
	@SuppressWarnings("rawtypes")
	public static interface InterfaceB extends 
			InterfaceA<String, String>, 
			InterfaceC,
			InterfaceD<String>,
			InterfaceE,
			InterfaceF<Map<?,?>, Map<?,?>> {
		
	}
	
	public static interface InterfaceB1<T,R> extends InterfaceG<T,R> {
		
	}
	
}
