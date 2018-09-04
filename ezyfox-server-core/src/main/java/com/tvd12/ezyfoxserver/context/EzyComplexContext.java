package com.tvd12.ezyfoxserver.context;

public interface EzyComplexContext 
        extends EzyContext, EzyPluginContextsFetcher, EzyAppContextsFetcher{

    EzyAppContext getAppContext(int appId);

    EzyPluginContext getPluginContext(int pluginId);

}
