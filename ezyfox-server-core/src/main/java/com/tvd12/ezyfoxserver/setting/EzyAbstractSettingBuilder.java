package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

@SuppressWarnings("unchecked")
public abstract class EzyAbstractSettingBuilder<
    T extends EzyAbstractSetting,
    B extends EzyAbstractSettingBuilder<T, B>>
    implements EzyBuilder<T> {

    protected String name;
    protected String entryLoader;
    protected String packageName;
    protected int threadPoolSize;
    protected String configFile;
    protected Object[] entryLoaderArgs;

    public B name(String name) {
        this.name = name;
        return (B) this;
    }

    public B entryLoader(String entryLoader) {
        this.entryLoader = entryLoader;
        return (B) this;
    }

    public B entryLoader(Class<?> entryLoader) {
        return entryLoader(entryLoader.getName());
    }

    public B packageName(String packageName) {
        this.packageName = packageName;
        return (B) this;
    }

    public B threadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
        return (B) this;
    }

    public B configFile(String configFile) {
        this.configFile = configFile;
        return (B) this;
    }

    public B entryLoaderArgs(Object[] entryLoaderArgs) {
        this.entryLoaderArgs = entryLoaderArgs;
        return (B) this;
    }

    @Override
    public T build() {
        T p = newSetting();
        p.setName(name);
        p.setEntryLoader(entryLoader);
        p.setPackageName(packageName);
        p.setThreadPoolSize(threadPoolSize);
        p.setConfigFile(configFile);
        p.setEntryLoaderArgs(entryLoaderArgs);
        return p;
    }

    protected abstract T newSetting();
}
