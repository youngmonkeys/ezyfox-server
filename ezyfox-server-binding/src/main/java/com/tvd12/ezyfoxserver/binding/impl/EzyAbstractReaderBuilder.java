package com.tvd12.ezyfoxserver.binding.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyFunction.EzyBody;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyPostRead;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyReflectElement;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;
import com.tvd12.ezyfoxserver.reflect.EzyTypes;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;

@SuppressWarnings("rawtypes")
public abstract class EzyAbstractReaderBuilder
		extends EzyAbstractBuilder<EzySetterMethod> {
	
	protected final List<EzyMethod> postReadMethods;
	
	public EzyAbstractReaderBuilder(EzyClass clazz) {
		super(clazz);
		this.postReadMethods = getPostReadMethods();
	}
	
	private List<EzyMethod> getPostReadMethods() {
		return clazz.getPublicMethods(m -> m.isAnnotated(EzyPostRead.class));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T build() {
		try {
			return (T)make();
		}
		catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	protected Object make() throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		EzyMethod readMethod = getReadMethod();
		readMethod.setDisplayName(getReadMethodName() + "$impl");
		String implMethodContent = makeImplMethodContent(readMethod);
		readMethod.setDisplayName(getReadMethodName());
		String methodContent = makeMethodContent(readMethod);
		printMethodContent(methodContent);
		printMethodContent(implMethodContent);
		implClass.setInterfaces(new CtClass[] { pool.makeClass(getReaderInterface().getName()) });
		implClass.addMethod(CtNewMethod.make(implMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(methodContent, implClass));
		Class answerClass = implClass.toClass();
		implClass.detach();
		getLogger().debug("class {} has generated", implClassName);
		return answerClass.newInstance();
	}
	
	protected String getReadMethodName() {
		return "read";
	}
	protected Class<?> getReaderInterface() {
		return EzyReader.class;
	}
	
	protected String makeMethodContent(EzyMethod readMethod) {
		String[] paramNames = new String[readMethod.getParameterCount()];
		for(int i = 0 ; i < paramNames.length ; i++)
			paramNames[i] = "arg" + i;
		String paramNamesChain = StringUtils.join(paramNames, ", ");
		return new EzyFunction(readMethod)
				.body()
					.append(new EzyInstruction("\t", "\n", false)
							.append("try {"))
					.append(new EzyInstruction("\t\t", "\n")
							.append("return this." + getReadMethodName() + "$impl( " + paramNamesChain + ")"))
					.append(new EzyInstruction("\t", "\n", false)
							.append("} catch(")
							.clazz(Exception.class)
							.append(" e) {"))
					.append(new EzyInstruction("\t\t", "\n\t}\n")
							.append("throw new ")
							.clazz(IllegalStateException.class)
							.bracketopen()
							.string("can't read value ")
							.append("+ arg1, e")
							.bracketclose())
					.function()
					.toString();
	}
	
	protected EzyInstruction newOutputObjectInstruction() {
		return new EzyInstruction("", "", false)
				.append("new ")
				.constructor(clazz.getClazz());
	}
	
	protected abstract String getImplClassName();
	protected abstract String makeImplMethodContent(EzyMethod readMethod);
	
	protected void addPostReadMethods(EzyBody methodBody, String objectName) {
		for(EzyMethod method : postReadMethods) {
			EzyInstruction instruction = new EzyInstruction("\t", "\n")
					.append(objectName)
					.dot()
					.append(method.getName())
					.brackets("");
			methodBody.append(instruction);
		}
	}
	
	protected EzyMethod getReadMethod() {
		return EzyMethod.builder()
				.clazz(getReaderInterface())
				.methodName(getReadMethodName())
				.parameterTypes(getReaderMethodParameterTypes())
				.build();
	}
	
	protected Class[] getReaderMethodParameterTypes() {
		return new Class[] {EzyUnmarshaller.class, Object.class};
	}
	
	protected Class getReaderImplClass(EzyReflectElement element) {
		com.tvd12.ezyfoxserver.binding.annotation.EzyReader rd =
				getReaderAnnotation(element);
		if(rd != null) return rd.value();
		if(element instanceof EzyField) return null;
		EzyField field = clazz.getField(getFieldName(element));
		return field == null ? null : getReaderImplClass(field);
	}
	
	protected com.tvd12.ezyfoxserver.binding.annotation.EzyReader 
			getReaderAnnotation(EzyReflectElement element) {
		return element.getAnnotation(
				com.tvd12.ezyfoxserver.binding.annotation.EzyReader.class);
	}
	
	protected EzyInstruction wrapUnmarshalInstruction(
			EzyInstruction instruction, Class outType) {
		String value = instruction.toString();
		EzyInstruction answer = new EzyInstruction("", "", false);
		return answer.cast(outType, value);
	}
	
	protected Set<Class> getCommonGenericTypes() {
		return EzyTypes.COMMON_GENERIC_TYPES;
	}
	
	protected void printMethodContent(String methodContent) {
		if(isDebug()) getLogger().debug("reader: method content \n{}", methodContent);
	}
	
	protected abstract boolean isDebug();

}
