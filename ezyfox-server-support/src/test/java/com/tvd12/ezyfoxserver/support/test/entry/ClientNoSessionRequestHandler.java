package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;

import lombok.Setter;

@Setter
@EzyPrototype
@EzyRequestListener("noSession")
@EzyObjectBinding(read = false)
public class ClientNoSessionRequestHandler
		extends EzyLoggable
		implements 
			EzyHandler, 
			EzyDataBinding {

	@Override
	public void handle() {
	}

}
