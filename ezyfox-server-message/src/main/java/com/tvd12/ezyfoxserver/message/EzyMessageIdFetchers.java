package com.tvd12.ezyfoxserver.message;

import java.lang.annotation.Annotation;
import java.util.Set;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.identifier.EzySimpleIdFetchers;
import com.tvd12.ezyfoxserver.message.annotation.EzyMessage;

public class EzyMessageIdFetchers extends EzySimpleIdFetchers {

	public EzyMessageIdFetchers(Builder builder) {
		super(builder);
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends EzySimpleIdFetchers.Builder {
	
		@SuppressWarnings("unchecked")
		@Override
		protected Set<Class<? extends Annotation>> getAnnotationClasses() {
			return Sets.newHashSet(EzyMessage.class);
		}
	
	}
	
}
