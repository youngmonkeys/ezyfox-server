package com.tvd12.ezyfoxserver.hazelcast.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.concurrent.atomic.AtomicInteger;

import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.hazelcast.service.EzySimpleHazelcastMapService;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyMethods;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtNewMethod;

public class EzySimpleServiceImplementer {

	protected final EzyClass clazz;
	
	protected static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzySimpleServiceImplementer(EzyClass clazz) {
		this.clazz = clazz;
		this.checkInterface(clazz);
	}
	
	public Object implement(HazelcastInstance hzInstance) {
		return implement(hzInstance, getMapName());
	}
	
	public Object implement(HazelcastInstance hzInstance, String mapName) {
		try {
			return doimplement(hzInstance, mapName);
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private void checkInterface(EzyClass clazz) {
		if(!Modifier.isInterface(clazz.getModifiers()))
			throw new IllegalArgumentException(clazz + " is not an interface");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object doimplement(HazelcastInstance hzInstance, String mapName) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = getSuperClass();
		String getMapNameMethodContent = makeGetMapNameMethodContent(mapName);
		implClass.setInterfaces(new CtClass[] { pool.get(clazz.getName()) });
		implClass.setSuperclass(pool.get(superClass.getName()));
		CtConstructor constructorMethod = makeContructorMethod(pool, implClass);
		implClass.addConstructor(constructorMethod);
		implClass.addMethod(CtNewMethod.make(getMapNameMethodContent, implClass));
		Class answerClass = implClass.toClass();
		implClass.detach();
		Constructor constructor = answerClass.getConstructor(HazelcastInstance.class);
		Object service = constructor.newInstance(hzInstance);
		return service;
	}
	
	protected CtConstructor makeContructorMethod(
			ClassPool pool, CtClass implClass) throws Exception{
		CtConstructor constructor = new CtConstructor(
			new CtClass[] {
				pool.get(HazelcastInstance.class.getName())
			}, 
			implClass
		);
		constructor.setModifiers(Modifier.PUBLIC);
		constructor.setBody("super($1);");
		return constructor;
	}
	
	protected String makeGetMapNameMethodContent(String mapName) {
		return new EzyFunction(getMapNameMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.string(mapName))
					.function()
				.toString();
	}
	
	private String getMapName() {
		return new EzySimpleMapNameFetcher().getMapName(clazz.getClazz());
	}
	
	protected EzyMethod getMapNameMethod() {
		Method method = EzyMethods.getMethod(EzySimpleHazelcastMapService.class, "getMapName");
		return new EzyMethod(method);
	}
	
	protected EzyClass getSuperClass() {
		return new EzyClass(EzySimpleHazelcastMapService.class);
	}
	
	protected String getImplClassName() {
		return clazz.getName() + "$EzyHazelcastService$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
}
