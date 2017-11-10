package com.tvd12.ezyfoxserver.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.bean.EzyBeanContext;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeFactory;
import com.tvd12.ezyfoxserver.bean.EzyPrototypeSupplier;
import com.tvd12.ezyfoxserver.io.EzyStrings;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import lombok.Setter;

@SuppressWarnings("rawtypes")
public abstract class EzySimplePrototypeSupplierLoader 
		extends EzySimpleObjectBuilder
		implements EzyPrototypeSupplierLoader {

	@Setter
	private static boolean debug;
	private static final AtomicInteger COUNT = new AtomicInteger(0);
	
	public EzySimplePrototypeSupplierLoader(EzyClass clazz) {
		super(clazz);
	}
	
	@Override
	public final EzyPrototypeSupplier load(EzyPrototypeFactory factory) {
		try {
			return process(factory);
		}
		catch(Exception e) {
			throw new IllegalStateException("can not create prototype supplier of class " + clazz, e);
		}
	}
	
	private Class<?> getPrototypeClass() {
		return clazz.getClazz();
	}
	
	protected String getPrototypeName() {
		return EzyBeanNameParser.getPrototypeName(getPrototypeClass());
	}
	
	protected Map getAnnotationProperties() {
		return EzyKeyValueParser.getPrototypeProperties(getPrototypeClass());
	}
	
	private EzyPrototypeSupplier process(EzyPrototypeFactory factory) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		String implClassName = getImplClassName();
		CtClass implClass = pool.makeClass(implClassName);
		
		EzyMethod supplyMethod = getSupplyMethod();
		supplyMethod.setDisplayName("supply$impl");
		String supplyImplMethodContent = makeSupplyImplMethodContent(supplyMethod);
		supplyMethod.setDisplayName("supply");
		String supplyMethodContent = makeSupplyMethodContent(supplyMethod);
		
		EzyMethod getObjectTypeMethod = getGetObjectTypeMethod();
		String getObjectTypeMethodContent = makeGetObjectTypeMethodContent(getObjectTypeMethod);
		
		printMethodContent(supplyMethodContent);
		printMethodContent(supplyImplMethodContent);
		printMethodContent(getObjectTypeMethodContent);
		
		implClass.setInterfaces(new CtClass[] { pool.makeClass(EzyPrototypeSupplier.class.getName()) });
		implClass.addMethod(CtNewMethod.make(supplyImplMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(supplyMethodContent, implClass));
		implClass.addMethod(CtNewMethod.make(getObjectTypeMethodContent, implClass));
		Class answerClass = implClass.toClass();
		implClass.detach();
		EzyPrototypeSupplier supplier = (EzyPrototypeSupplier)answerClass.newInstance();
		factory.addSupplier(getPrototypeName(), supplier, getAnnotationProperties());
		getLogger().debug("add prototype supplier of " + implClassName);
		return supplier;
	}
	
	private String makeGetObjectTypeMethodContent(EzyMethod method) {
		return new EzyFunction(method)
				.body()
					.append(new EzyInstruction("\t", "\n")
							.answer()
							.clazz(clazz.getClazz(), true))
				.function()
				.toString();
	}
	
	private String makeSupplyMethodContent(EzyMethod method) {
		return new EzyFunction(method)
				.body()
					.append(new EzyInstruction("\t", "\n", false)
							.append("try {"))
					.append(new EzyInstruction("\t\t", "\n")
							.append("return this.supply$impl(arg0)"))
					.append(new EzyInstruction("\t", "\n", false)
							.append("} catch(")
							.clazz(Exception.class)
							.append(" e) {"))
					.append(new EzyInstruction("\t\t", "\n\t}\n")
							.append("throw new ")
							.clazz(IllegalStateException.class)
							.bracketopen()
							.string("can't create bean of " + clazz.getClazz().getTypeName())
							.append(", e")
							.bracketclose())
				.function()
				.toString();
	}
	
	private String makeSupplyImplMethodContent(EzyMethod method) {
		EzyFunction.EzyBody methodBody = new EzyFunction(method)
				.modifier("protected")
				.body();
		addConstructInstruction(methodBody);
		addSetPropertiesInstructions(methodBody);
		addBindingValueInstructions(methodBody);
		addCallPostInitInstructions(methodBody);
		addReturnInstruction(methodBody);
		EzyFunction function = methodBody.function();
		return function.toString();
	}
	
	private void addConstructInstruction(EzyFunction.EzyBody body) {
		List<String> cparams = addAndGetConstructorParamNames(body);
		EzyInstruction instruction = newConstructInstruction(body, cparams);
		body.append(instruction);
	}
	
	private List<String> addAndGetConstructorParamNames(EzyFunction.EzyBody body) {
		int index = 0;
		List<String> cparams = new ArrayList<>();
		String[] argumentNames = getConstructorArgumentNames();
		for(Class type : getConstructorParameterTypes()) {
			String variableName = "cparam" + index;
			String beanName = EzyStrings.getString(argumentNames, index ++, variableName);
			cparams.add(variableName);
			body.append(newVariableInstruction(type, variableName, beanName));
		}
		return cparams;
	}
	
	protected EzyInstruction newConstructInstruction(EzyFunction.EzyBody body, List<String> cparams) {
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.variable(clazz.getClazz(), "object")
				.equal()
				.append("new ")
				.clazz(clazz.getClazz())
				.bracketopen()
				.append(StringUtils.join(cparams, ", "))
				.bracketclose();
		return instruction;
	}
	
	protected final EzyInstruction newVariableInstruction(Class varType, String varName, String beanName) {
		return new EzyInstruction("\t", "\n")
				.variable(varType, varName)
				.equal()
				.bracketopen()
				.clazz(varType)
				.bracketclose()
				.append("arg0.getBean")
				.bracketopen()
				.string(beanName)
				.comma()
				.clazz(varType, true)
				.bracketclose();
	}
	
	private void addReturnInstruction(EzyFunction.EzyBody body) {
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.answer()
				.append("object");
		body.append(instruction);
	}
	
	private void addSetPropertiesInstructions(EzyFunction.EzyBody body) {
		for(EzyField field : propertyFields)
			addSetPropertyInstruction(body, field);
		for(EzySetterMethod method : propertyMethods)
			addSetPropertyInstruction(body, method);
	}
	
	private void addSetPropertyInstruction(EzyFunction.EzyBody body, EzyField field) {
		Class propertyType = field.getType();
		String propertyName = getPropertyName(field);
		body.append(new EzyInstruction("\t", "\n", false)
				.append("if(arg0.containsProperty(\"" + propertyName + "\"))"));
		body.append(new EzyInstruction("\t\t", "\n")
				.append("object.")
				.append(field.getName())
				.equal()
				.brackets(propertyType)
				.append("arg0.getProperty")
				.bracketopen()
				.string(propertyName)
				.comma()
				.clazz(propertyType, true)
				.bracketclose());
	}
	
	private void addSetPropertyInstruction(EzyFunction.EzyBody body, EzySetterMethod method) {
		Class propertyType = method.getType();
		String propertyName = getPropertyName(method);
		body.append(new EzyInstruction("\t", "\n", false)
				.append("if(arg0.containsProperty(\"" + propertyName + "\"))"));
		body.append(new EzyInstruction("\t\t", "\n")
				.append("object.")
				.append(method.getName())
				.bracketopen()
				.brackets(propertyType)
				.append("arg0.getProperty")
				.bracketopen()
				.string(propertyName)
				.comma()
				.clazz(propertyType, true)
				.bracketclose()
				.bracketclose());
	}
	
	private void addBindingValueInstructions(EzyFunction.EzyBody body) {
		for(EzyField field : bindingFields)
			addBindingValueInstruction(body, field);
		for(EzySetterMethod method : bindingMethods)
			addBindingValueInstruction(body, method);
	}
	
	private void addBindingValueInstruction(
			EzyFunction.EzyBody body, EzyField field) {
		String variableName = field.getName() + variableCount.incrementAndGet();
		Class propertyType = field.getType();
		body.append(newVariableInstruction(propertyType, variableName, getBeanName(field)));
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.append("object.")
				.append(field.getName())
				.equal()
				.append(variableName);
		body.append(instruction);
	}
	
	private void addBindingValueInstruction(
			EzyFunction.EzyBody body, EzySetterMethod method) {
		String variableName = method.getFieldName() + variableCount.incrementAndGet();
		Class propertyType = method.getType();
		body.append(newVariableInstruction(propertyType, variableName, getBeanName(method)));
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.append("object.")
				.append(method.getName())
				.brackets(variableName);
		body.append(instruction);
	}
	
	private void addCallPostInitInstructions(EzyFunction.EzyBody body) {
		List<EzyMethod> methods = getPostInitMethods();
		methods.forEach(m -> addCallPostInitInstruction(body, m));
	}
	
	private void addCallPostInitInstruction(EzyFunction.EzyBody body, EzyMethod method) {
		EzyInstruction instruction = new EzyInstruction("\t", "\n")
				.append("object.")
				.append(method.getName())
				.brackets("");
		body.append(instruction);
	}
	
	private String getImplClassName() {
		return clazz.getName() + "$EzyPrototypeSupplier$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
	private EzyMethod getSupplyMethod() {
		return EzyMethod.builder()
				.clazz(EzyPrototypeSupplier.class)
				.methodName("supply")
				.parameterTypes(EzyBeanContext.class)
				.build();
	}
	
	private EzyMethod getGetObjectTypeMethod() {
		return EzyMethod.builder()
				.clazz(EzyPrototypeSupplier.class)
				.methodName("getObjectType")
				.build();
	}
	
	private void printMethodContent(String methodContent) {
		if(debug) getLogger().debug("reader: method content \n{}", methodContent);
	}
	
}
