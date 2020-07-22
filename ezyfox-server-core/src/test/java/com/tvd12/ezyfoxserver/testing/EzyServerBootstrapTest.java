package com.tvd12.ezyfoxserver.testing;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.EzyBootstrap;
import com.tvd12.ezyfoxserver.EzyServerBootstrap;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.config.EzySimpleConfig;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.entity.EzyAbstractSession;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import com.tvd12.test.reflect.MethodInvoker;

public class EzyServerBootstrapTest extends BaseCoreTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void test() throws Exception {
        EzyServerBootstrap bt = new MyTestServerBootstrapBuilder()
            .server(newServer())
            .build();
        bt.destroy();
        
        assert MethodInvoker.create().object(bt).method("getServer").invoke() != null;
        assert MethodInvoker.create().object(bt).method("getServerSettings").invoke() != null;
        assert MethodInvoker.create().object(bt).method("getHttpSetting").invoke() != null;
        assert MethodInvoker.create().object(bt).method("getSocketSetting").invoke() != null;
        assert MethodInvoker.create().object(bt).method("getWebSocketSetting").invoke() != null;
        
        EzyServerBootstrap bootstrap = new EzyServerBootstrap() {
          
            @Override
            protected void startOtherBootstraps(Runnable callback) throws Exception {
                callback.run();
            }
            
        };
        
        EzyServerContext serverContext = mock(EzyServerContext.class);
        EzySimpleServer server = new EzySimpleServer();
        EzySimpleConfig config = new EzySimpleConfig();
        server.setConfig(config);
        when(serverContext.getServer()).thenReturn(server);
        EzySimpleSettings settings = new EzySimpleSettings();
        server.setSettings(settings);
        EzySessionManager sessionManager = new ExEzySimpleSessionManager.Builder()
                .objectFactory(() -> spy(EzyAbstractSession.class))
                .build();
        server.setSessionManager(sessionManager);
        
        EzyBootstrap localBootstrap = EzyBootstrap.builder()
                .context(serverContext)
                .build();
        
        bootstrap.setContext(serverContext);
        bootstrap.setLocalBootstrap(localBootstrap);
        
        bootstrap.start();
    }
    
    public static class ExEzySimpleSessionManager extends EzySimpleSessionManager<EzySession> {
        
        public ExEzySimpleSessionManager(Builder builder) {
            super(builder);
        }
        
        public static class Builder extends EzySimpleSessionManager.Builder<EzySession> {

            @Override
            public EzySimpleSessionManager<EzySession> build() {
                return new ExEzySimpleSessionManager(this);
            }
            
        }
         
    }
    
}
