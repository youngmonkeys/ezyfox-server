package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.EzyHttpBootstrap;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.builder.EzyAbtractServerBootstrapBuilder;

public class MyTestServerBootstrapBuilder extends EzyAbtractServerBootstrapBuilder {

	@Override
	protected EzyServerBootstrap newServerBootstrap() {
	    return new MyTestServerBootstrap();
	}

	@Override
	public String getCodecCreatorClassName() {
	    return super.getCodecCreatorClassName();
	}
	
	@Override
	public String getWsCodecCreatorClassName() {
	    return super.getWsCodecCreatorClassName();
	}
	
	@Override
	protected EzyHttpBootstrap newHttpBottstrap() {
	    return new EzyHttpBootstrap() {
            
            @Override
            public void destroy() {
                
            }
            
            @Override
            public void start() throws Exception {
                
            }
        };
	}
	
}
