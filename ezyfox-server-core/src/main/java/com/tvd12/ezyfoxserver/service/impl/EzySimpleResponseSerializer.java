package com.tvd12.ezyfoxserver.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.function.EzySerializer;
import com.tvd12.ezyfoxserver.response.EzyResponse;
import com.tvd12.ezyfoxserver.service.EzyResponseSerializer;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleResponseSerializer 
        extends EzyLoggable implements EzyResponseSerializer {

	@SuppressWarnings("rawtypes")
	private final Map<Class, EzySerializer> serializers;
	
	protected EzySimpleResponseSerializer(Builder builder) {
	    this.serializers = builder.newSerializers();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T serialize(EzyResponse response, Class<T> outType) {
		Object answer = serializers.get(outType).serialize(response);
		return (T)answer;
	}
	
	public static Builder builder() {
	    return new Builder();
	}
	
	public static class Builder
	        extends EzyEntityBuilders
	        implements EzyBuilder<EzyResponseSerializer> {
	   
	    @Override
	    public EzyResponseSerializer build() {
	        return new EzySimpleResponseSerializer(this);
	    }
	    
	    @SuppressWarnings("rawtypes")
	    protected Map<Class, EzySerializer> newSerializers() {
	        Map<Class, EzySerializer> answer = new HashMap<>();
	        answer.put(EzyArray.class, new EzySerializer<EzyResponse, EzyArray>() {
	            @Override
	            public EzyArray serialize(EzyResponse response) {
	                return newArrayBuilder()
	                        .append(response.getCommand().getId())
	                        .append(response.getData())
	                        .build();
	            }
	        });
	        return answer;
	    }
	    
	}

}