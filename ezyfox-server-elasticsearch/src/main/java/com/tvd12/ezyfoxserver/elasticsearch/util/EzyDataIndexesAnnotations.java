package com.tvd12.ezyfoxserver.elasticsearch.util;

import static com.tvd12.ezyfoxserver.elasticsearch.util.EzyDataIndexAnnotations.getTypes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexType;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexTypes;
import com.tvd12.ezyfoxserver.elasticsearch.annotation.EzyDataIndex;
import com.tvd12.ezyfoxserver.elasticsearch.annotation.EzyDataIndexes;
import com.tvd12.ezyfoxserver.util.EzyNameStyles;

public final class EzyDataIndexesAnnotations {

	private static final Logger LOGGER 
			= LoggerFactory.getLogger(EzyDataIndexesAnnotations.class);
	
	private EzyDataIndexesAnnotations() {
	}
	
	public static EzyIndexTypes getIndexTypes(Class<?> clazz) {
		EzyDataIndexes indexesAnno = clazz.getAnnotation(EzyDataIndexes.class);
		if(indexesAnno != null)
			return EzyDataIndexesAnnotations.getIndexTypes(clazz, indexesAnno);
		
		EzyDataIndex indexAnno = clazz.getAnnotation(EzyDataIndex.class);
		if(indexAnno != null)
			return EzyDataIndexAnnotations.getIndexTypes(clazz, indexAnno);
		
		warningAnnotationNotFound(clazz);
		
		String className = clazz.getSimpleName();
		String hyphenClassName = EzyNameStyles.toLowerHyphen(className);
		return EzyIndexTypes.builder()
				.add(hyphenClassName, hyphenClassName)
				.build();
	}
	
	private static EzyIndexTypes getIndexTypes(Class<?> clazz, EzyDataIndexes anno) {
		EzyIndexTypes.Builder builder = EzyIndexTypes.builder();
		for(EzyDataIndex item : anno.value()) {
			String index = item.index();
			String[] types = getTypes(clazz, item);
			for(String type : types) {
				builder.add(new EzyIndexType(index, type));
			}
		}
		return builder.build();
	}
	
	private static void warningAnnotationNotFound(Class<?> clazz) {
		LOGGER.warn("{} was not annotated by @EzyDataIndexes or @EzyDataIndex, use class name by default");
	}
	
}
