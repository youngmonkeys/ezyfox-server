package com.tvd12.ezyfoxserver.ssl;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.util.EzyReturner;
import com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting;
import com.tvd12.ezyfoxserver.setting.EzySslConfigSetting;

import javax.net.ssl.SSLContext;
import java.nio.file.Paths;

public class EzySslContextInitializer {

    protected String homeFolderPath;
    protected EzySslConfigSetting sslConfig;

    protected EzySslContextInitializer(Builder builder) {
        this.sslConfig = builder.sslConfig;
        this.homeFolderPath = builder.homeFolderPath;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SSLContext init() {
        return newSslContext();
    }

    protected SSLContext newSslContext() {
        EzySslConfig config = loadSslConfig();
        EzySslContextFactoryBuilder builder =
            newSslContextFactoryBuilder();
        EzySslContextFactory factory = builder.build();
        return EzyReturner.returnWithException(
            () -> factory.newSslContext(config)
        );
    }

    protected EzySslConfig loadSslConfig() {
        String file = getSslConfigFile();
        EzySslConfigLoader loader = newSslConfigLoader();
        return loader.load(file);
    }

    protected EzySslContextFactoryBuilder newSslContextFactoryBuilder() {
        return EzyClasses.newInstance(
            sslConfig.getContextFactoryBuilder()
        );
    }

    protected EzySslConfigLoader newSslConfigLoader() {
        return EzyClasses.newInstance(sslConfig.getLoader());
    }

    protected String getSslConfigFile() {
        return getPath(
            homeFolderPath,
            EzyFolderNamesSetting.SETTINGS,
            sslConfig.getFile()
        );
    }

    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    public static class Builder
        implements EzyBuilder<EzySslContextInitializer> {

        protected String homeFolderPath;
        protected EzySslConfigSetting sslConfig;

        public Builder homeFolderPath(String homeFolderPath) {
            this.homeFolderPath = homeFolderPath;
            return this;
        }

        public Builder sslConfig(EzySslConfigSetting sslConfig) {
            this.sslConfig = sslConfig;
            return this;
        }

        @Override
        public EzySslContextInitializer build() {
            return new EzySslContextInitializer(this);
        }
    }
}
