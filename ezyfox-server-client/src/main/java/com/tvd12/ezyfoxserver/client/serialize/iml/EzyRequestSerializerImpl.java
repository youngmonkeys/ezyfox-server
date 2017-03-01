package com.tvd12.ezyfoxserver.client.serialize.iml;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.request.EzyRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.factory.EzyFactory;

import lombok.Builder;

@Builder
public class EzyRequestSerializerImpl implements EzyRequestSerializer {

	@SuppressWarnings("rawtypes")
	private static final Map<Class, Serializer> SERIALIZERS;

	static {
		SERIALIZERS = defaultSerializers();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T serialize(EzyRequest request, Class<T> outType) {
		return (T) SERIALIZERS.get(outType).serialize(request);
	}
	
	@SuppressWarnings("rawtypes")
	private static Map<Class, Serializer> defaultSerializers() {
		Map<Class, Serializer> answer = new HashMap<>();
		answer.put(EzyArray.class, new Serializer<EzyRequest, EzyArray>() {
			@Override
			public EzyArray serialize(EzyRequest request) {
				return EzyFactory.create(EzyArrayBuilder.class)
						.append(request.getCommand())
						.append(request.getData())
						.build();
			}
		});
		return answer;
	}

}

interface Serializer<I,O> {
	O serialize(I request);
}
