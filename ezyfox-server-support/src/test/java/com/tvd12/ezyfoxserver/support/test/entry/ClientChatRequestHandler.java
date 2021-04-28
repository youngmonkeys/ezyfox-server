package com.tvd12.ezyfoxserver.support.test.entry;

import com.tvd12.ezyfox.bean.annotation.EzyPrototype;
import com.tvd12.ezyfox.binding.EzyDataBinding;
import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfox.core.annotation.EzyRequestListener;
import com.tvd12.ezyfox.function.EzyHandler;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySessionAware;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.entity.EzyUserAware;

import lombok.Setter;

@Setter
@EzyPrototype
@EzyRequestListener("chat")
@EzyObjectBinding(read = false)
public class ClientChatRequestHandler
		extends EzyLoggable
		implements 
			EzyHandler, 
			EzyUserAware, 
			EzySessionAware, 
			EzyDataBinding {

	protected EzyUser user;
	protected EzySession session;
	
	@Override
	public void handle() {
	}

}
