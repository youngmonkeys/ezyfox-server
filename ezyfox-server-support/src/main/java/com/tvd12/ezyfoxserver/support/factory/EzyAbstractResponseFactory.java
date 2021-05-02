package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfox.binding.EzyMarshaller;

import lombok.Setter;

@Setter
public abstract class EzyAbstractResponseFactory implements EzyResponseFactory {

	protected EzyMarshaller marshaller;
	
}
