package com.tvd12.ezyfoxserver.binding.impl;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import com.tvd12.ezyfoxserver.asm.EzyFunction;
import com.tvd12.ezyfoxserver.asm.EzyInstruction;
import com.tvd12.ezyfoxserver.binding.EzyAccessType;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.reflect.EzyByFieldMethod;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyGenericSetterValidator;
import com.tvd12.ezyfoxserver.reflect.EzyGenerics;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyReflectElement;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;

import lombok.Setter;

@SuppressWarnings("rawtypes")
public class EzyObjectReaderBuilder extends EzyAbstractReaderBuilder {

	@Setter
	protected static boolean debug;
	protected static final AtomicInteger COUNT = new AtomicInteger(0);

	public EzyObjectReaderBuilder(EzyClass clazz) {
		super(clazz);
	}
	
	@Override
	protected int getAccessType(EzyClass clazz) {
		EzyObjectBinding ann = clazz.getAnnotation(EzyObjectBinding.class);
		return ann == null ? EzyAccessType.ALL : ann.accessType();
	}
	
	@Override
	protected EzyElementsFetcher newElementsFetcher() {
		return new EzyObjectReaderElementsFetcher();
	}
	
	@Override
	protected String makeImplMethodContent(EzyMethod readMethod) {
		EzyFunction.EzyBody methodBody = new EzyFunction(readMethod)
				.modifier("protected")
				.body()
					.append(new EzyInstruction("\t", "\n")
							.variable(EzyObject.class, "value")
							.equal()
							.cast(EzyObject.class, "arg1")
							)
					.append(new EzyInstruction("\t", "\n")
							.variable(clazz.getClazz(), "object")
							.equal()
							.append(newOutputObjectInstruction().toString(false))
							);
			
			for(Object element : getElements()) {
				methodBody.append(checkNotNullInstruction((EzyReflectElement) element));
				methodBody.append(newInstructionByElement(element));
			}
			
			addPostReadMethods(methodBody, "object");

			methodBody.append(new EzyInstruction("\t", "\n")
					.answer()
					.append("object"));
			
			EzyFunction method = methodBody.function();
			
			return method.toString();
	}
	
	protected EzyInstruction newInstructionByElement(Object element) {
		if(element instanceof EzyField)
			return newInstructionByField((EzyField)element);
		return newInstructionByMethod((EzyMethod)element);
	}
	
	protected EzyInstruction newInstructionByField(EzyField field) {
		EzyInstruction instruction = new EzyInstruction("\t\t", "\n")
				.append("object")
				.dot()
				.append(field.getName())
				.equal();
		EzyInstruction unmarshalInstruction = newUnmarshalInstruction(field);
		instruction.append(unmarshalInstruction.toString());
		return instruction;
	}
	
	protected EzyInstruction newInstructionByMethod(EzyMethod method) {
		EzyInstruction instruction = new EzyInstruction("\t\t", "\n")
				.append("object")
				.dot()
				.append(method.getName())
				.bracketopen();
		EzyInstruction unmarshalInstruction = newUnmarshalInstruction(method);
		instruction
				.append(unmarshalInstruction.toString())
				.bracketclose();
		return instruction;
	}
	
	protected EzyInstruction checkNotNullInstruction(EzyReflectElement element) {
		return new EzyInstruction("\t", "\n", false)
				.append("if")
				.bracketopen()
				.append("value")
				.dot()
				.append("isNotNullValue")
				.bracketopen()
				.string(getKey(element))
				.bracketclose()
				.bracketclose();
	}
	
	protected EzyInstruction newUnmarshalInstruction(EzyReflectElement element) {
		Set<Class> commonGenericTypes = getCommonGenericTypes();
		Class type = getElementType(element);
		Class readerImpl = getReaderImplClass(element);
		EzyInstruction instruction = null;
		if(readerImpl != null || !commonGenericTypes.contains(type))
			instruction = newUnmarshalNormalInstruction(element, readerImpl);
		else
			instruction = newUnmarshalGenericInstruction(element);

		return wrapUnmarshalInstruction(instruction, type);
	}
	
	protected EzyInstruction newUnmarshalGenericInstruction(EzyReflectElement element) {
		Class genericType = getElementType(element);
		if(Map.class.isAssignableFrom(genericType))
			return newUnmarshalMapInstruction(element);
		return newUnmarshalCollectionInstruction(element);
	}
	
	protected EzyInstruction newUnmarshalMapInstruction(EzyReflectElement element) {
		Class mapType = getElementType(element);
		Type genericType = getElementGenericType(element);
		Class[] keyValueTypes = EzyGenerics.getTwoGenericClassArguments(genericType);
		EzyInstruction instruction = new EzyInstruction("", "", false)
				.append("arg0.unmarshalMap")
				.bracketopen()
				.bracketopen()
				.clazz(Object.class)
				.bracketclose()
				.append("value.getValue(")
				.string(getKey(element))
				.comma()
				.clazz(mapType, true)
				.bracketclose()
				.comma()
				.clazz(mapType, true)
				.comma()
				.clazz(keyValueTypes[0], true)
				.comma()
				.clazz(keyValueTypes[1], true)
				.bracketclose();
		return instruction;
	}
	
	protected EzyInstruction newUnmarshalCollectionInstruction(EzyReflectElement element) {
		Class collectionType = getElementType(element);
		Type genericType = getElementGenericType(element);
		Class itemType = EzyGenerics.getOneGenericClassArgument(genericType);
		EzyInstruction instruction = new EzyInstruction("", "", false)
				.append("arg0.unmarshalCollection")
				.bracketopen()
				.bracketopen()
				.clazz(Object.class)
				.bracketclose()
				.append("value.getValue(")
				.string(getKey(element))
				.comma()
				.clazz(collectionType, true)
				.bracketclose()
				.comma()
				.clazz(collectionType, true)
				.comma()
				.clazz(itemType, true)
				.bracketclose();
		return instruction;
	}
	
	protected EzyInstruction newUnmarshalNormalInstruction(EzyReflectElement element, Class readerImpl) {
		EzyInstruction instruction = new EzyInstruction("", "", false)
				.append("arg0.unmarshal")
				.bracketopen();
		if(readerImpl != null) {
			instruction
				.clazz(readerImpl, true)
				.comma();
		}
		Class type = getElementType(element);
		instruction
				.bracketopen()
				.clazz(Object.class)
				.bracketclose()
				.append("value.getValue(")
				.string(getKey(element))
				.comma()
				.clazz(type, true)
				.bracketclose();
		if(readerImpl == null) {
			instruction
				.comma()
				.clazz(type, true);
		}
		instruction.bracketclose();
		return instruction;
	}
	
	@Override
	protected String getImplClassName() {
		return clazz.getName() + "$EzyObjectReader$EzyAutoImpl$" + COUNT.incrementAndGet();
	}
	
	@Override
	protected boolean isDebug() {
		return debug;
	}
}

class EzyObjectReaderElementsFetcher extends EzyObjectElementsFetcher {

	private EzyGenericSetterValidator setterValidator = new EzyGenericSetterValidator();
	
	@Override
	protected boolean isValidGenericField(EzyField field) {
		return setterValidator.validate(field.getGenericType());
	}
	
	@Override
	protected boolean isValidGenericMethod(EzyMethod method) {
		return setterValidator.validate(((EzyByFieldMethod)method).getGenericType());
	}
	
	@Override
	protected List<? extends EzyMethod> getMethods(EzyClass clazz) {
		return clazz.getSetterMethods();
	}
	
	@Override
	protected List<? extends EzyMethod> getDeclaredMethods(EzyClass clazz) {
		return clazz.getDeclaredSetterMethods();
	}
	
	@Override
	protected EzyMethod newByFieldMethod(EzyMethod method) {
		return new EzySetterMethod(method);
	}

	@Override
	protected boolean isValidAnnotatedMethod(EzyMethod method) {
		return method.getParameterCount() == 1;
	}
}