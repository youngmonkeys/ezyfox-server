package com.tvd12.ezyfoxserver.testing;

import static org.mockito.Mockito.mock;

import org.testng.annotations.Test;

import com.tvd12.ezyfox.pattern.EzyObjectFactory;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.EzyStarter;
import com.tvd12.ezyfoxserver.builder.EzyAbtractServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerBootstrapBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;

@SuppressWarnings({"rawtypes", "unchecked"})
public class EzyStarterTest {

    @Test
    public void normalCaseTest() throws Exception {
        EzyStarter starter = new ExEzyStarter.Builder()
                .configFile("test-data/settings/config.properties")
                .build();
        starter.start();
    }
    
    public static class ExEzyStarter extends EzyStarter {
        
        protected ExEzyStarter(Builder builder) {
            super(builder);
        }
        
        @Override
        protected EzyServerBootstrapBuilder newServerBootstrapBuilder() {
            EzyAbtractServerBootstrapBuilder builder = new EzyAbtractServerBootstrapBuilder() {

                @Override
                protected EzyServerBootstrap newServerBootstrap() {
                    EzyServerBootstrap bootstrap = new EzyServerBootstrap() {

                        @Override
                        protected void startOtherBootstraps(Runnable callback) throws Exception {
                        }
                        
                        @Override
                        protected void startLocalBootstrap() throws Exception {
                        }
                        
                    };
                    return bootstrap;
                }
                
            };
            return builder;
        }


        @Override
        protected EzySimpleSessionManager.Builder newSessionManagerBuilder(
                EzySettings settings) {
            return new ExEzySimpleSessionManager.Builder();
        }
         
        public static class Builder extends EzyStarter.Builder<Builder> {

            @Override
            public EzyStarter build() {
                return new ExEzyStarter(this);
            }
            
        }
        
    }
    
    public static class ExEzySimpleSessionManager extends EzySimpleSessionManager {
        
        protected ExEzySimpleSessionManager(Builder builder) {
            super(builder);
        }
        
        public static class Builder extends EzySimpleSessionManager.Builder {

            @Override
            public EzySimpleSessionManager build() {
                return new ExEzySimpleSessionManager(this);
            }
            
            @Override
            protected EzyObjectFactory newObjectFactory() {
                EzyObjectFactory factory = mock(EzyObjectFactory.class);
                return factory;
            }
            
        }
        
    }
    
    
}
