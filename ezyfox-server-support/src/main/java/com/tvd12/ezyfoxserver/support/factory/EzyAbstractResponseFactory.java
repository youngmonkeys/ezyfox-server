package com.tvd12.ezyfoxserver.support.factory;

import com.tvd12.ezyfox.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfox.bean.annotation.EzySingleton;
import com.tvd12.ezyfox.binding.EzyMarshaller;

import lombok.Setter;

@Setter
@EzySingleton("responseFactory")
public abstract class EzyAbstractResponseFactory implements EzyResponseFactory {

	@EzyAutoBind
	protected EzyMarshaller marshaller;
	
}
