package com.tvd12.ezyfoxserver.support.command;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfoxserver.context.EzyContext;

public abstract class AbstractObjectResponse 
		extends AbstractResponse<ObjectResponse>
		implements ObjectResponse {

	protected final Map<Object, Object> additionalParams = new HashMap<>();
	
	public AbstractObjectResponse(EzyContext context, EzyMarshaller marshaller) {
		super(context, marshaller);
	}
	
	@Override
	public ObjectResponse param(Object key, Object value) {
		additionalParams.put(key, value);
		return this;
	}

	@Override
	protected EzyData getResponseData() {
		EzyObject object = data != null
				? marshaller.marshal(data)
				: newObjectBuilder().build();
		for(Object key : additionalParams.keySet()) {
			Object value = additionalParams.get(key);
			Object skey = marshaller.marshal(key);
			Object svalue = marshaller.marshal(value);
			object.put(skey, svalue);
		}
		return object;
	}
	
}
