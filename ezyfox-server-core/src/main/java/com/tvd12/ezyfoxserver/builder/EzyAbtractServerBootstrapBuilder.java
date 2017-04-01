package com.tvd12.ezyfoxserver.builder;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.context.EzyServerContext;

public abstract class EzyAbtractServerBootstrapBuilder 
		implements EzyServerBootstrapBuilder {

	protected int port;
	protected int wsport;
	protected EzyServer boss;
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#port(int)
	 */
	@Override
	public EzyServerBootstrapBuilder port(int port) {
		this.port = port;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#wsport(int)
	 */
	@Override
	public EzyServerBootstrapBuilder wsport(int wsport) {
		this.wsport = wsport;
		return this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder#boss(com.tvd12.ezyfoxserver.EzyServer)
	 */
	@Override
	public EzyServerBootstrapBuilder boss(EzyServer boss) {
		this.boss = boss;
		return this;
	}
	
	protected EzyBootstrap newLocalBoostrap(EzyServerContext context) {
    	return EzyBootstrap.builder().context(context).build();
    }
	
	/*
	 * (non-Javadoc)
	 * @see com.tvd12.ezyfoxserver.builder.EzyBuilder#build()
	 */
	@Override
	public EzyServerBootstrap build() {
		return build(newContext());
	}

	protected abstract EzyServerContext newContext();
	protected abstract EzyServerBootstrap build(EzyServerContext ctx);
}


