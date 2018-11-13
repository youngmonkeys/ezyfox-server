package com.tvd12.ezyfoxserver.setting;

import java.nio.file.Paths;

import com.tvd12.ezyfox.builder.EzyBuilder;
import com.tvd12.ezyfox.mapping.jaxb.EzySimplXmlMapper;
import com.tvd12.ezyfox.mapping.jaxb.EzyXmlReader;
import com.tvd12.ezyfox.util.EzyInitable;
import com.tvd12.ezyfox.util.EzyLoggable;

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
        logger.info("read setting file: " + getSettingsFilePath());
        EzyXmlReader xmlReader = newXmlReader();
        EzySimpleSettings settings = readSettingsFile(xmlReader);
        EzySimpleZoneFilesSetting zoneFiles = settings.getZoneFiles();
        zoneFiles.forEach(zf -> {
            if(!zf.isActive()) return;
            EzySimpleZoneSetting zoneSetting = readZoneConfigFile(xmlReader, zf.getConfigFile());
            postReadZoneSettings(zoneSetting);
            zoneSetting.setName(zf.getName());
            zoneSetting.setConfigFile(zf.getConfigFile());
            zoneSetting.init();
            settings.addZone(zoneSetting);
        });
        return settings;
    }
    
    protected EzySimpleSettings readSettingsFile(EzyXmlReader xmlReader) {
        return xmlReader.read(getSettingsFilePath(), EzySimpleSettings.class);
    }
    
    protected EzySimpleZoneSetting readZoneConfigFile(EzyXmlReader xmlReader, String configFile) {
        return xmlReader.read(getZoneConfigFilePath(configFile), EzySimpleZoneSetting.class);
    }
    
    protected void postReadSettings(EzySettings settings) {
        updateSessionManagementSetting(settings.getSessionManagement());
    }
    
    protected void postReadZoneSettings(EzyZoneSetting zoneSetting) {
        updatePluginsSetting(zoneSetting.getPlugins());
        updateAppsSetting(zoneSetting.getApplications());
        updateUserManagementSetting(zoneSetting.getUserManagement());
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
    
    protected String getZoneConfigFilePath(String configFile) {
        return getPath(getSettingsPath(), EzyFolderNamesSetting.ZONES, configFile);
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
