package com.tvd12.ezyfoxserver.elasticsearch.util;

import static com.tvd12.ezyfoxserver.util.EzyNameStyles.toLowerHyphen;

import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexType;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.annotation.EzyDataIndex;

public final class EzyDataIndexAnnotations {

	private EzyDataIndexAnnotations() {
	}
	
	public static String[] getTypes(Class<?> clazz, EzyDataIndex anno) {
		String[] types = anno.types();
		if(types.length > 0)
			return anno.types();
		return new String[] {toLowerHyphen(clazz.getSimpleName())};
	}
	
	public static EzyIndexTypes getIndexTypes(Class<?> clazz, EzyDataIndex anno) {
		String index = anno.index();
		String[] types = getTypes(clazz, anno);
		EzyIndexTypes.Builder builder = EzyIndexTypes.builder();
		for(String type : types) {
			builder.add(new EzyIndexType(index, type));
		}
		EzyIndexTypes indexTypes = builder.build();
		return indexTypes;
	}
	
}
