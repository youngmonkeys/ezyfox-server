package com.tvd12.ezyfoxserver.mongodb.bean;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyGenerics;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyMethods;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;

public abstract class EzySimpleRepositoryImplementer {

	private final EzyClass clazz;
	
	protected static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzySimpleRepositoryImplementer(Class<?> clazz) {
		this(new EzyClass(clazz));
	}
	
	public EzySimpleRepositoryImplementer(EzyClass clazz) {
		this.clazz = clazz;
	}
	
	public Object implement(Object template) {
		try {
			return doimplement(template);
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	protected Object doimplement(Object template) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyClass superClass = new EzyClass(getSuperClass());
		Class[] idAndEntityTypes = getIdAndEntityTypes();
		Class entityType = idAndEntityTypes[1];
		String getEntityTypeMethodContent = makeGetEntityTypeMethodContent(entityType);
		implClass.addMethod(CtNewMethod.make(getEntityTypeMethodContent, implClass));
		implClass.setInterfaces(new CtClass[] { pool.get(clazz.getName()) });
		implClass.setSuperclass(pool.get(superClass.getName()));
		Class answerClass = implClass.toClass();
		implClass.detach();
		Object repo = answerClass.newInstance();
		setRepoComponent(repo, template);
		return repo;
	}
	
	protected abstract void setRepoComponent(Object repo, Object template);
	
	@SuppressWarnings("rawtypes")
	protected String makeGetEntityTypeMethodContent(Class entityType) {
		return new EzyFunction(getEntityTypeMethod())
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.clazz(entityType, true))
					.function()
				.toString();
	}
	
	protected EzyMethod getEntityTypeMethod() {
		Method method = EzyMethods.getMethod(getSuperClass(), "getEntityType");
		return new EzyMethod(method);
	}
	
	protected abstract Class<?> getSuperClass();
	
	protected String getImplClassName() {
		return clazz.getName() + "$EzyMongoRepository$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
	@SuppressWarnings("rawtypes")
	protected Class[] getIdAndEntityTypes() {
		return EzyGenerics.getGenericInterfacesArguments(clazz.getClazz(), EzyMongoRepository.class, 2);
	}
	
}
