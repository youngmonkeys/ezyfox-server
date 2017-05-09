package com.tvd12.ezyfoxserver.bean.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

import com.tvd12.ezyfoxserver.annotation.EzyProperty;
import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzyPostInit;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyClasses;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;
import com.tvd12.ezyfoxserver.reflect.EzyReflectElement;
import com.tvd12.ezyfoxserver.reflect.EzySetterMethod;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyPropertyAnnotations;

@SuppressWarnings("rawtypes")
public abstract class EzySimpleObjectBuilder extends EzyLoggable {

	protected final EzyClass clazz;
	protected final AtomicInteger variableCount;
	protected final List<EzyField> bindingFields;
	protected final List<EzySetterMethod> bindingMethods;
	protected final List<EzyField> propertyFields;
	protected final List<EzySetterMethod> propertyMethods;
	
	public EzySimpleObjectBuilder(EzyClass clazz) {
		this.clazz = clazz;
		this.variableCount = new AtomicInteger(0);
		this.bindingFields = getBindingFields(clazz);
		this.bindingMethods = getBindingMethods(clazz);
		this.propertyFields = getPropertyFields(clazz);
		this.propertyMethods = getPropertyMethods(clazz);
	}
	
	protected Constructor getConstructor(EzyClass clazz) {
		List<Constructor> constructors = clazz.getDeclaredConstructors();
		for(Constructor con : constructors)
			if(con.isAnnotationPresent(EzyAutoBind.class))
				return con;
		return clazz.getDeclaredConstructor();
	}
	
	protected abstract Class<?>[] getConstructorParameterTypes();
	
	protected String[] getConstructorArgumentNames() {
		return getArgumentNames(getConstructorParameterTypes());
	}
	
	protected final String[] getArgumentNames(Class<?>[] parameterTypes) {
		String[] names = new String[parameterTypes.length];
		for(int i = 0 ; i < parameterTypes.length ; i++)
			names[i] = EzyClasses.getVariableName(parameterTypes[i]);
		return names;
	}
	
	protected final String[] getConstructorArgumentNames(EzyAutoBind annotation) {
		Class<?>[] parameterTypes = getConstructorParameterTypes();
		String[] names = getArgumentNames(parameterTypes);
		if(annotation == null) return names;
		String[] fixNames = annotation.value();
		for(int i = 0 ; i < fixNames.length ; i++)
			if(i < names.length) names[i] = fixNames[i];
		return names;
	}
	
	protected final String[] getConstructorArgumentNames(Constructor<?> constructor) {
		return getConstructorArgumentNames(
				constructor.getAnnotation(EzyAutoBind.class));
	}
	
	protected final List<EzyField> getBindingFields(EzyClass clazz) {
		return getValidFields(clazz, EzyAutoBind.class);
	}
	
	protected final List<EzySetterMethod> getBindingMethods(EzyClass clazz) {
		return getValidMethods(clazz, this::isBindingMethod);
	}
	
	protected final List<EzyField> getPropertyFields(EzyClass clazz) {
		return getValidFields(clazz, EzyProperty.class);
	}
	
	protected final List<EzySetterMethod> getPropertyMethods(EzyClass clazz) {
		return getValidMethods(clazz, this::isPropertyMethod);
	}
	
	protected final List<EzyMethod> getPostInitMethods() {
		return clazz.getPublicMethods(m ->
			m.isAnnotated(EzyPostInit.class) && 
			m.getParameterCount() == 0
		);
	}
	
	private boolean isBindingMethod(EzyMethod method) {
		return isValidMethod(method, EzyAutoBind.class);
	}
	
	private boolean isPropertyMethod(EzyMethod method) {
		return isValidMethod(method, EzyProperty.class);
	}
	
	private List<EzyField> getValidFields(
			EzyClass clazz, Class<? extends Annotation> ann) {
		return clazz.getFields(f -> f.isPublic() && f.isAnnotated(ann));
	}
	
	private List<EzySetterMethod> 
			getValidMethods(EzyClass clazz, Predicate<EzyMethod> predicate) {
		List<EzyMethod> methods = clazz.getMethods();
		List<EzyMethod> valid = EzyLists.filter(methods, predicate);
		return EzyLists.newArrayList(valid, EzySetterMethod::new);
	}
	
	private boolean isValidMethod(EzyMethod method, Class<? extends Annotation> ann) {
		if(!method.isPublic())
			return false;
		if(method.getParameterCount() != 1)
			return false;
		EzyField field = clazz.getField(method.getFieldName());
		boolean answer = 
				field != null && 
				!field.isPublic() && 
				field.isAnnotated(ann);
		return answer ? answer : method.isAnnotated(ann);
	}
	
	protected final boolean isAbstractClass(Class<?> clazz) {
		return Modifier.isAbstract(clazz.getModifiers());
	}
	
	protected final String getBeanName(EzyReflectElement element) {
		if(element instanceof EzyField)
			return getBeanName((EzyField)element);
		return getBeanName((EzyMethod)element);
	}
	
	private String getBeanName(EzyField field) {
		EzyAutoBind annotation = field.getAnnotation(EzyAutoBind.class);
		if(annotation == null)
			return field.getName();
		if(annotation.value().length > 0 && annotation.value()[0].length() > 0)
			return annotation.value()[0];
		return field.getName();
	}
	
	private final String getBeanName(EzyMethod method) {
		EzyAutoBind annotation = method.getAnnotation(EzyAutoBind.class);
		if(annotation != null && annotation.value().length > 0) 
			return annotation.value()[0];
		String fieldName = method.getFieldName();
		EzyField field = clazz.getField(fieldName);
		return field != null ? getBeanName(field) : fieldName;
	}
	
	protected final String getPropertyName(EzyReflectElement element) {
		return EzyPropertyAnnotations.getPropertyName(clazz, element);
	}
	
}
