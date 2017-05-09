package com.tvd12.ezyfoxserver.binding.template;

import java.util.Map;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

@SuppressWarnings("rawtypes")
public class EzyMapArrayWriter
		extends EzyEntityBuilders
		implements EzyWriter<Map, EzyArray> {

	@Override
	public EzyArray write(EzyMarshaller marshaller, Map map) {
		EzyArrayBuilder arrayBuilder = newArrayBuilder();
		for(Object key : map.keySet()) {
			EzyObjectBuilder objectBuilder = newObjectBuilder();
			Object tkey = marshaller.marshal(key);
			Object tvalue = marshaller.marshal(map.get(key));
			objectBuilder.append("k", tkey);
			objectBuilder.append("v", tvalue);
			arrayBuilder.append(objectBuilder);
		}
		return arrayBuilder.build();
	}

}
