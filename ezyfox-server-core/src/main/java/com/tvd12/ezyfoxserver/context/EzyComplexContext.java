package com.tvd12.ezyfoxserver.context;

public interface EzyComplexContext extends EzyContext {

    EzyAppContext getAppContext(int appId);

    EzyAppContext getAppContext(String appName);

    EzyPluginContext getPluginContext(int pluginId);

    EzyPluginContext getPluginContext(String pluginName);

}
