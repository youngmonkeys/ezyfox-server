package com.tvd12.ezyfoxserver.binding.impl;

import static com.tvd12.ezyfoxserver.binding.EzyAccessType.NONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfoxserver.binding.annotation.EzyArrayBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyIndex;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.io.EzyMaps;
import com.tvd12.ezyfoxserver.reflect.EzyAnnotatedElement;
import com.tvd12.ezyfoxserver.reflect.EzyByFieldMethod;
import com.tvd12.ezyfoxserver.reflect.EzyClass;
import com.tvd12.ezyfoxserver.reflect.EzyField;
import com.tvd12.ezyfoxserver.reflect.EzyMethod;

//================== fetcher by array ================
public abstract class EzyArrayElementsFetcher extends EzyAbstractElementsFetcher {
	
	private Map<String, ? extends EzyMethod> methodsByFieldName;
	
	@Override
	public final List<Object> getElements(EzyClass clazz, int accessType) {
		getLogger().debug("start scan {}", clazz);
		
		initialize(clazz, accessType);
		
		List<Object> elements = doGetElements(clazz, accessType);
		
		getLogger().debug("finish scan {}", clazz);
		
		return elements;
	}
	
	private void initialize(EzyClass clazz, int accessType) {
		this.methodsByFieldName = getMethodsByFieldName(clazz);
	}
	
	private List<Object> doGetElements(EzyClass clazz, int accessType) {
		String[] indexes = getIndexes(clazz);
		if(indexes.length > 0) {
			return getElementsByCustomIndexes(clazz, indexes);
		}
		else if(accessType == NONE) {
			return getAnnotatedElements(clazz);
		}
		else {
			return getElementsByNativeIndexes(clazz, accessType);
		}
	}
	
	private List<Object> getElementsByNativeIndexes(EzyClass clazz, int accessType) {
		return getElementsByNativeIndexes(
				getFields(clazz, accessType), 
				getMethods(clazz, accessType));
	}
	
	private List<Object> getElementsByCustomIndexes(EzyClass clazz, String[] indexes) {
		return getElementsByCustomIndexes(
				getFieldsByName(clazz),
				indexes);
	}
	
	private List<Object> getAnnotatedElements(EzyClass clazz) {
		return getAnnotatedElements(
				clazz, 
				getAnnotedFields(clazz), 
				getAnnotedMethods(clazz));
	}
	
	private List<Object> getElementsByNativeIndexes(
			List<EzyField> fields, List<? extends EzyMethod> methods) {
		
		List<Object> elements = new ArrayList<>();
		Set<EzyMethod> remainMethods = new HashSet<>(methods);
		
		for(EzyField field : fields) {
			getLogger().debug("scan field {}", field.getName());
			
			EzyMethod method = methodsByFieldName.get(field.getName());
			
			if(method != null) {
				if(isValidGenericMethod(method)) {
					elements.add(method);
				}
				else {
					elements.add(null);
					getLogger().debug("unknown generic type of method {}, ignore it", method.getName());
				}
				remainMethods.remove(method);
			} 
			else if(field.isPublic()) {
				if(isValidGenericField(field)) {
					elements.add(field);
				}
				else {
					getLogger().debug("unknown generic type of field {}, ignore it", field.getName());
					elements.add(null);
				}
			} 
			else {
				getLogger().debug("field {} has not getter/setter, ignore it", field.getName());
			}
		}
		for(EzyMethod method : remainMethods) {
			getLogger().debug("scan method {}", method.getName());
			
			if(isValidGenericMethod(method)) {
				elements.add(method);
			}
			else {
				getLogger().debug("unknown generic type of method {}, ignore it", method.getName());
			}
		}
		return elements;
	}
	
	private List<Object> getElementsByCustomIndexes(Map<String, EzyField> fieldsByName, String[] indexes) {
		List<Object> elements = new ArrayList<>();
		
		for(String property : indexes) {
			getLogger().debug("scan property {}", property);
			
			EzyMethod method = methodsByFieldName.get(property);
			
			if(method != null) {
				if(!isValidGenericMethod(method)) {
					getLogger().debug("unknown generic type of method {}, ignore it", method.getName());
				}
				else {
					elements.add(method); continue;
				}
			}
			else {
				getLogger().debug("has no getter/setter method map to property {}", property);
			}
			
			EzyField field = fieldsByName.get(property);
			
			if(field != null) {
				if(!field.isPublic()) {
					getLogger().debug("has no public field map to property {}", property);
				}
				else if(isValidGenericField(field)) {
					elements.add(field);
				}
				else {
					getLogger().debug("unknown generic type of field {}, ignore it", field.getName());
				}
			}
			else {
				elements.add(null);
				getLogger().debug("nothing map to property {}, ignore it", property);
			}
			
		}
		return elements;
	}
	
	private List<Object> getAnnotatedElements(
			EzyClass clazz, List<EzyField> fields, List<? extends EzyMethod> methods) {
		
		List<Object> elements = new ArrayList<>();
		
		for(EzyField field : fields) {
			getLogger().debug("scan field {}", field.getName());
			
			EzyMethod method = methodsByFieldName.get(field.getName());
			
			if(method != null) {
				if(!isValidGenericMethod(method)) {
					getLogger().debug("unknown generic type of method {}, ignore it", method.getName());
				}
				else {
					elements.add(method);
					methods.remove(method);
				}
			}
			else if(field.isPublic()) {
				if(!isValidGenericField(field)) {
					getLogger().debug("unknown generic type of field {}, ignore it", field.getName());
				}
				else {
					elements.add(field);
				}
			}
			else {
				getLogger().debug("field {} has not getter/setter, ignore it", field.getName());
			}
		}
		for(EzyMethod method : methods) {
			if(isValidGenericMethod(method)) {
				elements.add(method);
			}
			else {
				getLogger().debug("unknown generic type of method {}, ignore it", method.getName());
			}
		}
		
		Object max = Collections.max(elements, (e1, e2) -> 
			getIndex(clazz, e1) - getIndex(clazz, e2)
		);
		
		int maxIndex = getIndex(clazz, max);
		
		Map<Integer, Object> elementsByIndex = EzyMaps.newHashMap(elements, e -> getIndex(clazz, e));
		List<Object> answer = new ArrayList<>();
		
		for(int index = 0 ; index <= maxIndex ; index ++) {
			if(elementsByIndex.containsKey(index)) {
				answer.add(elementsByIndex.get(index));
			}
			else { 
				answer.add(null);
				getLogger().debug("has not property at index {}, so at this index value is null", index);
			}
		}
		return answer;
	}
	
	private Map<String, EzyField> getFieldsByName(EzyClass clazz) {
		return EzyMaps.newHashMap(clazz.getFields(), f -> f.getName());
	}
	
	private List<EzyField> getAnnotedFields(EzyClass clazz) {
		return clazz.getFields(f -> f.isAnnotated(EzyIndex.class));
	}
	
	private List<? extends EzyMethod> getAnnotedMethods(EzyClass clazz) {
		List<EzyMethod> methods = EzyLists.filter(clazz.getMethods(), this::shouldAddAnnotatedMethod);
		return EzyLists.newArrayList(methods, this::newByFieldMethod);
	}
	
	private boolean shouldAddAnnotatedMethod(EzyMethod method) {
		return  method.isPublic() &&
				method.isAnnotated(EzyIndex.class) && 
				isValidAnnotatedMethod(method);
	}
	
	protected abstract EzyMethod newByFieldMethod(EzyMethod method);
	protected abstract boolean isValidAnnotatedMethod(EzyMethod method);
	
	private String[] getIndexes(EzyClass clazz) {
		EzyArrayBinding ann = clazz.getAnnotation(EzyArrayBinding.class);
		return ann != null ? ann.indexes() : new String[0];
	}
	
	private int getIndex(EzyClass clazz, Object element) {
		EzyIndex index = ((EzyAnnotatedElement)element).getAnnotation(EzyIndex.class);
		if(index != null) return index.value();
		EzyByFieldMethod method = (EzyByFieldMethod)element;
		return getIndex(clazz, clazz.getField(method.getFieldName()));
	}
	
}