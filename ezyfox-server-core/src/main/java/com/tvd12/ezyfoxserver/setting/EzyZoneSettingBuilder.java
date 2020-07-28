package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.builder.EzyBuilder;

public class EzyZoneSettingBuilder implements EzyBuilder<EzySimpleZoneSetting> {

    protected String name = "default";
    protected String configFile;
    protected int maxUsers = 999999;
    protected EzySimpleStreamingSetting streaming = new EzySimpleStreamingSetting();
    protected EzySimplePluginsSetting plugins = new EzySimplePluginsSetting();
    protected EzySimpleAppsSetting applications = new EzySimpleAppsSetting();
    protected EzySimpleUserManagementSetting userManagement = new EzySimpleUserManagementSetting();
    protected EzySimpleEventControllersSetting eventControllers = new EzySimpleEventControllersSetting();

    public EzyZoneSettingBuilder name(String name) {
        this.name = name;
        return this;
    }

    public EzyZoneSettingBuilder configFile(String configFile) {
        this.configFile = configFile;
        return this;
    }

    public EzyZoneSettingBuilder maxUsers(int maxUsers) {
        this.maxUsers = maxUsers;
        return this;
    }

    public EzyZoneSettingBuilder streaming(EzySimpleStreamingSetting streaming) {
        this.streaming = streaming;
        return this;
    }
    
    public EzyZoneSettingBuilder plugin(EzySimplePluginSetting plugin) {
        this.plugins.setItem(plugin);
        return this;
    }

    public EzyZoneSettingBuilder plugins(EzySimplePluginsSetting plugins) {
        this.plugins = plugins;
        return this;
    }
    
    public EzyZoneSettingBuilder application(EzySimpleAppSetting application) {
        this.applications.setItem(application);
        return this;
    }

    public EzyZoneSettingBuilder applications(EzySimpleAppsSetting applications) {
        this.applications = applications;
        return this;
    }

    public EzyZoneSettingBuilder userManagement(EzySimpleUserManagementSetting userManagement) {
        this.userManagement = userManagement;
        return this;
    }

    public EzyZoneSettingBuilder eventControllers(EzySimpleEventControllersSetting eventControllers) {
        this.eventControllers = eventControllers;
        return this;
    }

    @Override
    public EzySimpleZoneSetting build() {
        EzySimpleZoneSetting p = new EzySimpleZoneSetting();
        p.setName(name);
        p.setConfigFile(configFile);
        p.setMaxUsers(maxUsers);
        p.setStreaming(streaming);
        p.setPlugins(plugins);
        p.setApplications(applications);
        p.setUserManagement(userManagement);
        p.setEventControllers(eventControllers);
        return p;
    }
    
}
