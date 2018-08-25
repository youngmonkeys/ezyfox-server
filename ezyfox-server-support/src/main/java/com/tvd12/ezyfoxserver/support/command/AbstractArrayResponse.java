package com.tvd12.ezyfoxserver.support.command;

import java.util.ArrayList;
import java.util.List;

import com.tvd12.ezyfox.binding.EzyMarshaller;
import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;
import com.tvd12.ezyfoxserver.context.EzyContext;

public abstract class AbstractArrayResponse 
		extends AbstractResponse<ArrayResponse>
		implements ArrayResponse {
	
	protected final List<Object> additionalParams = new ArrayList<>();

	public AbstractArrayResponse(EzyContext context, EzyMarshaller marshaller) {
		super(context, marshaller);
	}

	@Override
	public ArrayResponse param(Object value) {
		additionalParams.add(value);
		return this;
	}
	
	@Override
	protected EzyData getResponseData() {
		EzyArray array = data != null 
				? marshaller.marshal(data) 
				: newArrayBuilder().build();
		for(Object object : additionalParams) {
			Object value = marshaller.marshal(object);
			array.add(value);
		}
		return array;
	}
	
}
