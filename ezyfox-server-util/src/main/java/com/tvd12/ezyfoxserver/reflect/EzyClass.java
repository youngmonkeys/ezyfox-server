package com.tvd12.ezyfoxserver.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.util.EzyReturner;

import lombok.Getter;

@SuppressWarnings("rawtypes")
@Getter
public class EzyClass implements EzyReflectElement {

	protected final Class clazz;
	protected final List<EzyField> fields;
	protected final List<EzyMethod> methods;
	protected final List<EzyField> declaredFields;
	protected final List<EzyMethod> declaredMethods;
	protected final Map<String, EzyField> fieldsByName;
	protected final Map<String, EzyMethod> methodsByName;
	
	public EzyClass(Class clazz) {
		this.clazz = clazz;
		this.methods = newMethods(clazz);
		this.fields = newFields(clazz);
		this.declaredFields = newDeclaredFields(clazz);
		this.declaredMethods = newDeclaredMethods(clazz);
		this.fieldsByName = mapFieldsByName();
		this.methodsByName = mapMethodsByName();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T newInstance() {
		return (T) EzyClasses.newInstance(clazz);
	}
	
	@Override
	public String getName() {
		return clazz.getName();
	}
	
	public int getModifiers() {
		return clazz.getModifiers();
	}
	
	public EzyField getField(String name) {
		return fieldsByName.get(name);
	}
	
	public EzyMethod getMethod(String name) {
		return methodsByName.get(name);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isAnnotated(Class<? extends Annotation> annClass) {
		return clazz.isAnnotationPresent(annClass);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends Annotation> T getAnnotation(Class<T> annClass) {
		return (T) clazz.getAnnotation(annClass);
	}
	
	public List<Constructor> getDeclaredConstructors() {
		return Lists.newArrayList(clazz.getDeclaredConstructors());
	}
	
	@SuppressWarnings("unchecked")
	public Constructor getDeclaredConstructor(Class... parameterTypes) {
		return EzyReturner.returnWithIllegalArgumentException(
				() -> clazz.getDeclaredConstructor(parameterTypes)); 
	}

	public List<EzyMethod> getPublicMethods() {
		return getMethods(m -> m.isPublic());
	}
	
	public List<EzySetterMethod> getSetterMethods() {
		return getMethods(m -> m.isSetter(), EzySetterMethod::new);
	}
	
	public List<EzyGetterMethod> getGetterMethods() {
		return getMethods(m -> m.isGetter(), EzyGetterMethod::new);
	}
	
	public EzyMethod getSetterMethod(String methodName) {
		Optional<EzyMethod> optional = 
				getSetterMethod(m -> m.getName().equals(methodName));
		return optional.isPresent() ? optional.get() : null;
	}
	
	public EzyMethod getGetterMethod(String methodName) {
		Optional<EzyMethod> optional = 
				getGetterMethod(m -> m.getName().equals(methodName));
		return optional.isPresent() ? optional.get() : null;
	}
	
	public Optional<EzyMethod> getPublicMethod(Predicate<EzyMethod> predicate) {
		return methods.stream()
				.filter(m -> m.isPublic() && predicate.test(m)).findFirst();
	}
	
	public Optional<EzyMethod> getGetterMethod(Predicate<EzyMethod> predicate) {
		return methods.stream()
				.filter(m -> m.isGetter() && predicate.test(m)).findFirst();
	}
	
	public Optional<EzyMethod> getSetterMethod(Predicate<EzyMethod> predicate) {
		return methods.stream()
				.filter(m -> m.isSetter() && predicate.test(m)).findFirst();
	}
	
	public List<EzyMethod> getPublicMethods(Predicate<EzyMethod> predicate) {
		return getMethods(m -> m.isPublic() && predicate.test(m));
	}
	
	public List<EzySetterMethod> getSetterMethods(Predicate<EzySetterMethod> predicate) {
		return getSetterMethods().stream()
				.filter(predicate).collect(Collectors.toList());
	}
	
	public List<EzyGetterMethod> getGetterMethods(Predicate<EzyGetterMethod> predicate) {
		return getGetterMethods().stream()
				.filter(predicate).collect(Collectors.toList());
	}
	
	public List<EzyMethod> getMethods(Predicate<EzyMethod> predicate) {
		return methods.stream().filter(predicate).collect(Collectors.toList());
	}
	
	public List<EzyField> getWritableFields() {
		return getFields(f -> f.isWritable());
	}
	
	public List<EzyField> getPublicFields() {
		return getFields(f -> f.isPublic());
	}
	
	public List<EzyField> getPublicFields(Predicate<EzyField> predicate) {
		return getFields(f -> f.isPublic() && predicate.test(f));
	}
	
	public Optional<EzyField> getField(Predicate<EzyField> predicate) {
		return fields.stream().filter(predicate).findFirst();
	}
	
	public List<EzyField> getFields(Predicate<EzyField> predicate) {
		return fields.stream().filter(predicate).collect(Collectors.toList());
	}
	
	public Optional<EzyMethod> getMethod(Predicate<EzyMethod> predicate) {
		return methods.stream().filter(predicate).findFirst();
	}
	
	public <T extends EzyMethod> List<T> getMethods(
			Predicate<EzyMethod> predicate, Function<EzyMethod, T> creator) {
		return methods.stream()
				.filter(predicate)
				.flatMap(m -> Stream.of(creator.apply(m)))
				.collect(Collectors.toList());
	}
	
	public List<EzySetterMethod> getDeclaredSetterMethods() {
		return getDeclaredMethods(m -> m.isSetter(), EzySetterMethod::new);
	}
	
	public List<EzyGetterMethod> getDeclaredGetterMethods() {
		return getDeclaredMethods(m -> m.isGetter(), EzyGetterMethod::new);
	}
	
	public <T extends EzyMethod> List<T> getDeclaredMethods(
			Predicate<EzyMethod> predicate, Function<EzyMethod, T> creator) {
		return declaredMethods.stream()
				.filter(predicate)
				.flatMap(m -> Stream.of(creator.apply(m)))
				.collect(Collectors.toList());
	}
	
	public List<EzyMethod> getDeclaredMethods(Predicate<EzyMethod> predicate) {
		return declaredMethods.stream().filter(predicate).collect(Collectors.toList());
	}
	
	private List<EzyField> newFields(Class clazz) {
		List<EzyField> answer = new ArrayList<>();
		EzyFields.getFields(clazz).forEach(f -> answer.add(new EzyField(f)));
		return answer;
	}
	
	private List<EzyMethod> newMethods(Class clazz) {
		List<EzyMethod> answer = new ArrayList<>();
		EzyMethods.getMethods(clazz).forEach(m -> answer.add(new EzyMethod(m)));
		return answer;
	}
	
	private List<EzyField> newDeclaredFields(Class clazz) {
		List<EzyField> answer = new ArrayList<>();
		EzyFields.getDeclaredFields(clazz).forEach(f -> answer.add(new EzyField(f)));
		return answer;
	}
	
	private List<EzyMethod> newDeclaredMethods(Class clazz) {
		List<EzyMethod> answer = new ArrayList<>();
		EzyMethods.getDeclaredMethods(clazz).forEach(m -> answer.add(new EzyMethod(m)));
		return answer;
	}
	
	private Map<String, EzyField> mapFieldsByName() {
		Map<String, EzyField> map = new HashMap<>();
		fields.forEach(f -> map.put(f.getName(), f));
		return map;
	}
	
	private Map<String, EzyMethod> mapMethodsByName() {
		Map<String, EzyMethod> map = new HashMap<>();
		methods.forEach(m -> map.put(m.getName(), m));
		return map;
	}
	
	@Override
	public String toString() {
		return clazz.toString();
	}
	
}
