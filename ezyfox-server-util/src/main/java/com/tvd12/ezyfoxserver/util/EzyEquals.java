package com.tvd12.ezyfoxserver.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EzyEquals<T> {

	protected List<Function<T, Object>> functions = new ArrayList<>();
	
	@SuppressWarnings("unchecked")
	public boolean isEquals(T thiz, Object other) {
		if(other == null)
			return false;
		if(other == thiz)
			return true;
		if(!other.getClass().equals(thiz.getClass()))
			return false;
		T t = (T)other;
		for(Function<T, Object> func : functions) {
			Object v1 = func.apply(t);
			Object v2 = func.apply(thiz);
			if(v1 == v2)
				continue;
			if(v1 == null)
				return false;
			if(v2 == null)
				return false;
			if(!v1.equals(v2))
				return false;
		}
		return true;
	}
	
	public EzyEquals<T> function(Function<T, Object> function) {
		this.functions.add(function);
		return this;
	}
	
}
