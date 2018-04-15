package com.tvd12.ezyfoxserver.testing;

import java.net.SocketAddress;

import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

public class MyTestStarter extends EzyStarter {

	protected MyTestStarter(Builder builder) {
		super(builder);
	}

	@Override
	protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
		return new MyTestServerBootstrapBuilder();
	}
	
	@SuppressWarnings({ "rawtypes" })
    @Override
    protected EzySimpleSessionManager.Builder 
            newSessionManagerBuilder(EzySettings settings) {
        return new ExSessionManager.SBuilder();
    }
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyStarter.Builder<Builder> {
		@Override
		public EzyStarter build() {
			return new MyTestStarter(this);
		}
	}
	
	public static class ExSessionManager extends EzySimpleSessionManager<ExSession> {

        protected ExSessionManager(SBuilder builder) {
            super(builder);
        }
        
        public static class SBuilder extends EzySimpleSessionManager.Builder<ExSession> {

            @Override
            public EzySimpleSessionManager<ExSession> build() {
                return new ExSessionManager(this);
            }

            @Override
            protected EzyObjectFactory<ExSession> newObjectFactory() {
                return new EzyObjectFactory<MyTestStarter.ExSession>() {
                    @Override
                    public ExSession newProduct() {
                        return new ExSession();
                    }
                };
            }
        }
	    
	}
	
	public static class ExSession extends EzyAbstractSession {
        private static final long serialVersionUID = 2019546923661465393L;

        @Override
        public SocketAddress getClientAddress() {
            return null;
        }

        @Override
        public SocketAddress getServerAddress() {
            return null;
        }

        @Override
        public void close() {
        }

        @Override
        public void disconnect() {
        }
	    
	}

}
