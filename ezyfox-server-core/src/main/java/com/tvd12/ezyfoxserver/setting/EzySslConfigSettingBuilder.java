package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzySslConfigSettingBuilder implements EzyBuilder<EzySimpleSslConfigSetting> {

    protected String file = "ssl-config.properties";
    protected String loader = "com.tvd12.ezyfoxserver.ssl.EzySimpleSslConfigLoader";
    protected String contextFactoryBuilder = "com.tvd12.ezyfoxserver.ssl.EzySimpleSslContextFactoryBuilder";

    public EzySslConfigSettingBuilder file(String file) {
        this.file = file;
        return this;
    }

    public EzySslConfigSettingBuilder loader(String loader) {
        this.loader = loader;
        return this;
    }

    public EzySslConfigSettingBuilder contextFactoryBuilder(String contextFactoryBuilder) {
        this.contextFactoryBuilder = contextFactoryBuilder;
        return this;
    }

    @Override
    public EzySimpleSslConfigSetting build() {
        EzySimpleSslConfigSetting setting = new EzySimpleSslConfigSetting();
        setting.setFile(file);
        setting.setLoader(loader);
        setting.setContextFactoryBuilder(contextFactoryBuilder);
        return setting;
    }
}
