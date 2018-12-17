package com.tvd12.ezyfoxserver;

import java.util.Map;

import com.tvd12.ezyfox.json.EzyJsonMapper;
import com.tvd12.ezyfoxserver.api.EzyResponseApi;
import com.tvd12.ezyfoxserver.api.EzyStreamingApi;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;

public interface EzyServer extends EzyEventComponent {

    /**
     * Get server version
     * 
     * @return the server version
     */
    String getVersion();

    /**
     * Get server config
     * 
     * @return the server config
     */
    EzyConfig getConfig();

    /**
     * Get server settings
     * 
     * @return the server setting
     */
    EzySettings getSettings();

    /**
     * Get server class loader
     * 
     * @return the server class loader
     */
    ClassLoader getClassLoader();
    
    /**
     * Get server statistics
     * 
     * @return the server statistics
     */
    EzyStatistics getStatistics();

    /**
     * Get json mappers
     * 
     * @return the json mapper
     */
    EzyJsonMapper getJsonMapper();

    /**
     * Get server controllers
     * 
     * @return the server controllers
     */
    EzyServerControllers getControllers();

    /**
     * Get server response api
     * 
     * @return the server response api
     */
    EzyResponseApi getResponseApi();
    
    /**
     * Get server streaming api
     * 
     * @return the server streaming api
     */
    EzyStreamingApi getStreamingApi();
    
    /**
     * Get server session manager
     * 
     * @return the session manager
     */
    @SuppressWarnings("rawtypes")
    EzySessionManager getSessionManager();
    
    /**
     * Get server's applications class loader
     * 
     * @return the applications class loader
     */
    Map<String, EzyAppClassLoader> getAppClassLoaders();

}
