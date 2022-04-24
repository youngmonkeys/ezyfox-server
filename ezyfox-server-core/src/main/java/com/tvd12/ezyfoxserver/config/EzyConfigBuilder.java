package com.tvd12.ezyfoxserver.config;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyConfigBuilder implements EzyBuilder<EzyConfig> {

    protected String ezyfoxHome;
    protected boolean printSettings;
    protected boolean printBanner;
    protected String bannerFile;
    protected String loggerConfigFile;
    protected boolean enableAppClassLoader;

    public EzyConfigBuilder() {
        this.printSettings = true;
        this.printBanner = true;
        this.enableAppClassLoader = false;
        this.bannerFile = "ezyfox-banner.txt";
    }

    public EzyConfigBuilder ezyfoxHome(String ezyfoxHome) {
        this.ezyfoxHome = ezyfoxHome;
        return this;
    }

    public EzyConfigBuilder printSettings(boolean printSettings) {
        this.printSettings = printSettings;
        return this;
    }

    public EzyConfigBuilder printBanner(boolean printBanner) {
        this.printBanner = printBanner;
        return this;
    }

    public EzyConfigBuilder bannerFile(String bannerFile) {
        this.bannerFile = bannerFile;
        return this;
    }

    public EzyConfigBuilder loggerConfigFile(String loggerConfigFile) {
        this.loggerConfigFile = loggerConfigFile;
        return this;
    }

    public EzyConfigBuilder enableAppClassLoader(boolean enableAppClassLoader) {
        this.enableAppClassLoader = enableAppClassLoader;
        return this;
    }

    @Override
    public EzyConfig build() {
        EzySimpleConfig p = new EzySimpleConfig();
        p.setEzyfoxHome(ezyfoxHome);
        p.setPrintSettings(printSettings);
        p.setPrintBanner(printBanner);
        p.setBannerFile(bannerFile);
        p.setLoggerConfigFile(loggerConfigFile);
        p.setEnableAppClassLoader(enableAppClassLoader);
        return p;
    }

}
