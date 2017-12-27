package com.tvd12.ezyfoxserver.testing;

import java.security.KeyPair;

import com.tvd12.ezyfoxserver.EzyLoader;
import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.builder.EzyServerContextBuilder;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.config.EzyConfigLoader;
import com.tvd12.ezyfoxserver.config.EzySimpleConfigLoader;
import com.tvd12.ezyfoxserver.constant.EzyConstant;
import com.tvd12.ezyfoxserver.context.EzyServerContext;
import com.tvd12.ezyfoxserver.delegate.EzySessionDelegate;
import com.tvd12.ezyfoxserver.entity.EzySession;
import com.tvd12.ezyfoxserver.entity.EzySimpleUser;
import com.tvd12.ezyfoxserver.entity.EzyUser;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.mapping.jackson.EzyJsonMapper;
import com.tvd12.ezyfoxserver.mapping.jackson.EzySimpleJsonMapper;
import com.tvd12.ezyfoxserver.mapping.jaxb.EzySimplXmlMapper;
import com.tvd12.ezyfoxserver.mapping.jaxb.EzyXmlReader;
import com.tvd12.ezyfoxserver.sercurity.EzyKeysGenerator;
import com.tvd12.ezyfoxserver.wrapper.EzyManagers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerUserManager;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyManagersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyRequestMappersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;
import com.tvd12.test.base.BaseTest;

public class BaseCoreTest extends BaseTest {
    
    protected EzyServerContext newServerContext() {
        return newServerContextBuilder().build();
    }

    protected EzyServerContextBuilder<?> newServerContextBuilder() {
        return new MyTestServerContextBuilder()
                .server(newServer());
    }
    
    protected EzySimpleServer newServer() {
        try {
            EzySimpleServer answer = (EzySimpleServer) loadEzyFox(readConfig("test-data/settings/config.properties"));
            answer.setRequestMappers(EzyRequestMappersImpl.builder().build());
            answer.getManagers().addManager(EzySessionManager.class, MyTestSessionManager.builder().build());
            answer.getManagers().addManager(EzyServerUserManager.class, MyTestUserManager.builder().build());
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
        return new EzyLoader()
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
    
    public EzyManagers newManagers() {
        return EzyManagersImpl.builder().build();
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
    
    protected KeyPair newRSAKeys() {
        return EzyKeysGenerator.builder()
                .build()
                .generate();
    }
    
    protected EzySession newSessionHasKey(String reconnectToken) {
        KeyPair keyPair = newRSAKeys();
        EzySession session = newSession();
        session.setReconnectToken(reconnectToken);
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
        public void onSessionRemoved(EzyConstant reason) {
        }

        @Override
        public void onSessionLoggedIn(EzyUser user) {
        }
        
    }
}
