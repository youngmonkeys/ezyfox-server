package com.tvd12.ezyfoxserver.setting;

import java.nio.file.Paths;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.mapping.jaxb.EzySimplXmlMapper;
import com.tvd12.ezyfoxserver.mapping.jaxb.EzyXmlReader;
import com.tvd12.ezyfoxserver.util.EzyInitable;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

public class EzySimpleSettingsReader
        extends EzyLoggable
        implements EzySettingsReader {

    protected String homePath;
    protected ClassLoader classLoader; 
    
    protected EzySimpleSettingsReader(Builder builder) {
        this.homePath = builder.homePath;
        this.classLoader = builder.classLoader;
    }
    
    @Override
    public EzySettings read() {
        EzySettings settings = readSettings();
        postReadSettings(settings);
        return settings;
    }
    
    protected EzySettings readSettings() {
        getLogger().info("read setting file: " + getSettingsFilePath());
        return newXmlReader().read(getSettingsFilePath(), EzySimpleSettings.class);
    }
    
    protected void postReadSettings(EzySettings settings) {
        updatePluginsSetting(settings.getPlugins());
        updateAppsSetting(settings.getApplications());
        updateUserManagementSetting(settings.getUserManagement());
        updateSessionManagementSetting(settings.getSessionManagement());
    }
    
    protected void updateAppsSetting(EzyAppsSetting apps) {
        for(EzyAppSetting app : apps.getApps())
            ((EzyHomePathAware)app).setHomePath(homePath);
    }
    
    protected void updatePluginsSetting(EzyPluginsSetting plugins) {
        for(EzyPluginSetting plugin : plugins.getPlugins())
            ((EzyHomePathAware)plugin).setHomePath(homePath);
    }
    
    protected void updateUserManagementSetting(EzyUserManagementSetting setting) {
        ((EzyInitable)setting).init();
    }
    
    protected void updateSessionManagementSetting(EzySessionManagementSetting setting) {
        ((EzyInitable)setting).init();
    }
    
    protected EzyXmlReader newXmlReader() {
        return EzySimplXmlMapper.builder()
                .classLoader(classLoader)
                .contextPath("com.tvd12.ezyfoxserver")
                .build();
    }
    
    protected String getSettingsPath() {
        return getPath(homePath, EzyFolderNamesSetting.SETTINGS);
    }
    
    protected String getSettingsFilePath() {
        return getPath(getSettingsPath(), EzyFileNames.SETTINGS);
    }
    
    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzySettingsReader> {
        
        protected String homePath;
        protected ClassLoader classLoader; 
        
        public Builder homePath(String homePath) {
            this.homePath = homePath;
            return this;
        }
        
        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }
        
        @Override
        public EzySettingsReader build() {
            return new EzySimpleSettingsReader(this);
        }
        
    }
    
}
