package com.tvd12.ezyfoxserver.binding.template;

import java.util.Map;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyWriter;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.util.EzyEntityBuilders;

@SuppressWarnings("rawtypes")
public class EzyMapObjectWriter
		extends EzyEntityBuilders
		implements EzyWriter<Map, EzyObject> {

	@Override
	public EzyObject write(EzyMarshaller marshaller, Map map) {
		EzyObjectBuilder builder = newObjectBuilder();
		for(Object key : map.keySet()) {
			Object tkey = marshaller.marshal(key);
			Object tvalue = marshaller.marshal(map.get(key));
			builder.append(tkey, tvalue);
		}
		return builder.build();
	}

}
