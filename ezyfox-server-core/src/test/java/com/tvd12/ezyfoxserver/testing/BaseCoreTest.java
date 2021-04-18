package com.tvd12.ezyfoxserver.testing;

import static org.mockito.Mockito.mock;

import java.security.KeyPair;

import com.tvd12.ezyfox.builder.EzyArrayBuilder;
import com.tvd12.ezyfox.builder.EzyObjectBuilder;
import com.tvd12.ezyfox.factory.EzyEntityFactory;
import com.tvd12.ezyfox.json.EzyJsonMapper;
import com.tvd12.ezyfox.mapping.jackson.EzySimpleJsonMapper;
import com.tvd12.ezyfox.mapping.jaxb.EzySimplXmlMapper;
import com.tvd12.ezyfox.mapping.jaxb.EzyXmlReader;
import com.tvd12.ezyfox.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfox.util.EzyInitable;
import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.builder.EzyServerContextBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.context.EzySimpleServerContext;
import com.tvd12.ezyfoxserver.context.EzyZoneContext;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager.Builder;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;
import com.tvd12.test.base.BaseTest;

public class BaseCoreTest extends BaseTest {
    
    protected EzyServerContext newServerContext() {
        EzyServerContext serverContext = newServerContextBuilder().build();
        for(EzyZoneContext zoneContext : serverContext.getZoneContexts()) {
            ((EzyInitable)zoneContext).init();
        }
        for(EzyAppContext appContext : serverContext.getAppContexts()) {
            ((EzyInitable)appContext).init();
        }
        EzySimpleServerContext ctx = (EzySimpleServerContext) serverContext;
        EzySimpleServer server = (EzySimpleServer) ctx.getServer();
        server.setResponseApi(mock(EzyResponseApi.class));
        return ctx;
    }

    protected EzyServerContextBuilder<?> newServerContextBuilder() {
        return new MyTestServerContextBuilder()
                .server(newServer()); 
    }
    
    protected EzySimpleServer newServer() {
        try {
            EzySimpleServer answer = (EzySimpleServer) loadEzyFox(readConfig("test-data/settings/config.properties"));
            return answer;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected EzyConfig readConfig(String configFile) throws Exception {
        return getConfigLoader().load(configFile);
    }
    
    protected EzyConfigLoader getConfigLoader() {
        return new EzySimpleConfigLoader();
    }
    
    protected EzyServer loadEzyFox(EzyConfig config) {
        return new EzyLoader() {

            @SuppressWarnings({ "rawtypes" })
            @Override
            protected Builder createSessionManagerBuilder(EzySettings settings) {
                return MyTestSessionManager.builder();
            }
            
        }
        .config(config)
        .classLoader(getClassLoader())
        .load();
    }
    
    protected EzyXmlReader getXmlReader() {
        return EzySimplXmlMapper.builder()
                .classLoader(getClassLoader())
                .contextPath("com.tvd12.ezyfoxserver.mapping")
                .build();
    }
    
    protected EzyJsonMapper getJsonMapper() {
        return EzySimpleJsonMapper.builder().build();
    }
    
    public EzyServerControllers newControllers() {
        return EzyServerControllersImpl.builder().build();
    }

    protected ClassLoader getClassLoader() {
        return EzySimpleServer.class.getClassLoader();
    }
    
    protected EzySession newSession() {
        MyTestSession session = new MyTestSession();
        session.setDelegate(new SessionDelegate());
        return session;
    }
    
    protected EzySession newSession(int id) {
        MyTestSession session = new MyTestSession();
        session.setId(id);
        session.setDelegate(new SessionDelegate());
        return session;
    }
    
    protected KeyPair newRSAKeys() {
        return EzyKeysGenerator.builder()
                .build()
                .generate();
    }
    
    protected EzySession newSessionHasKey(String token) {
        KeyPair keyPair = newRSAKeys();
        EzySession session = newSession();
        session.setToken(token);
        session.setPublicKey(keyPair.getPublic().getEncoded());
        return session;
    }
    
    protected EzySimpleUser newUser() {
        return new EzySimpleUser();
    }
    
    protected EzyArrayBuilder newArrayBuilder() {
        return EzyEntityFactory.create(EzyArrayBuilder.class);
    }
    
    protected EzyObjectBuilder newObjectBuilder() {
        return EzyEntityFactory.create(EzyObjectBuilder.class);
    }
    
    public static class SessionDelegate implements EzySessionDelegate {

        @Override
        public void onSessionLoggedIn(EzyUser user) {
        }
        
    }
}
