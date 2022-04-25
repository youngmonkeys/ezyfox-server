package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.*;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class EzyLoader extends EzyLoggable {

    protected EzyConfig config;
    protected ClassLoader classLoader;
    protected EzySettingsDecorator settingsDecorator;

    public EzyServer load() {
        EzySettings settings = readSettings();
        EzySimpleServer answer = new EzySimpleServer();
        answer.setConfig(config);
        answer.setSettings(settings);
        answer.setClassLoader(classLoader);
        answer.setAppClassLoaders(newAppClassLoaders());
        answer.setStatistics(newStatistics());
        answer.setControllers(newControllers());
        answer.setSessionManager(newSessionManagers(settings));
        answer.setEventControllers(newEventControllers(settings.getEventControllers()));
        return answer;
    }

    protected EzySettings readSettings() {
        return newSettingsReader().read();
    }

    protected EzySettingsReader newSettingsReader() {
        return EzySimpleSettingsReader.builder()
            .homePath(getHomePath())
            .classLoader(classLoader)
            .settingsDecorator(settingsDecorator)
            .build();
    }

    protected EzyStatistics newStatistics() {
        return new EzySimpleStatistics();
    }

    @SuppressWarnings("rawtypes")
    protected abstract EzySimpleSessionManager.Builder createSessionManagerBuilder(
        EzySettings settings
    );

    @SuppressWarnings("rawtypes")
    protected EzySessionManager newSessionManagers(EzySettings settings) {
        EzySimpleSessionManager.Builder builder
            = createSessionManagerBuilder(settings);
        EzySessionTokenGenerator tokenGenerator
            = new EzySimpleSessionTokenGenerator(settings.getNodeName());
        builder
            .tokenGenerator(tokenGenerator)
            .maxSessions(settings.getMaxSessions());
        return builder.build();
    }

    protected EzyServerControllers newControllers() {
        return EzyServerControllersImpl.builder().build();
    }

    protected Map<String, ClassLoader> newAppClassLoaders() {
        Map<String, ClassLoader> answer = new ConcurrentHashMap<>();
        File[] entryFolders = getEntryFolders();
        for (File dir : entryFolders) {
            answer.put(dir.getName(), newAppClassLoader(dir));
        }
        return answer;
    }

    protected ClassLoader newAppClassLoader(File dir) {
        logger.info("load: {}", dir);
        if (config.isEnableAppClassLoader()) {
            return new EzyAppClassLoader(dir, classLoader);
        }
        return classLoader;
    }

    protected EzyEventControllers newEventControllers(EzyEventControllersSetting setting) {
        return EzyEventControllersImpl.create(setting);
    }

    protected File[] getEntryFolders() {
        File entries = getEntriesFolder();
        File[] answer = entries.listFiles(File::isDirectory);
        return answer != null ? answer : new File[0];
    }

    protected File getEntriesFolder() {
        String entriesPath = getEntriesPath();
        return new File(entriesPath);
    }

    protected String getEntriesPath() {
        return getPath(getAppsPath(), EzyFolderNamesSetting.ENTRIES);
    }

    protected String getAppsPath() {
        return getPath(getHomePath(), EzyFolderNamesSetting.APPS);
    }

    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    protected String getHomePath() {
        return config.getEzyfoxHome();
    }

    public EzyLoader classLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public EzyLoader config(EzyConfig config) {
        this.config = config;
        return this;
    }

    public EzyLoader settingsDecorator(EzySettingsDecorator settingsDecorator) {
        this.settingsDecorator = settingsDecorator;
        return this;
    }
}
