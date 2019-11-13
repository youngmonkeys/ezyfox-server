package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.core.annotation.EzyClientRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;

import lombok.Setter;

@Setter
@EzyPrototype
@EzyClientRequestListener("noUser")
@EzyObjectBinding(read = false)
public class ClientNoUserRequestHandler
		extends EzyLoggable
		implements 
			EzyHandler, 
			EzySessionAware, 
			EzyDataBinding {

	protected EzySession session;
	
	@Override
	public void handle() {
	}

}
