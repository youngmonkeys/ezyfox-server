package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("unchecked")
public abstract class EzyAbstractSettingBuilder<
        T extends EzyAbstractSetting, 
        B extends EzyAbstractSettingBuilder<T, B>> 
        implements EzyBuilder<T> {

    protected String name;
    protected String entryLoader;
    protected int threadPoolSize;
    protected String configFile;

    public B name(String name) {
        this.name = name;
        return (B)this;
    }

    public B entryLoader(String entryLoader) {
        this.entryLoader = entryLoader;
        return (B)this;
    }
    
    public B entryLoader(Class<?> entryLoader) {
        this.entryLoader = entryLoader.getName();
        return (B)this;
    }

    public B threadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return (B)this;
    }

    public B configFile(String configFile) {
        this.configFile = configFile;
        return (B)this;
    }

    @Override
    public T build() {
        T p = newSetting();
        p.setName(name);
        p.setEntryLoader(entryLoader);
        p.setThreadPoolSize(threadPoolSize);
        p.setConfigFile(configFile);
        return p;
    }
	
    protected abstract T newSetting();
    
}
