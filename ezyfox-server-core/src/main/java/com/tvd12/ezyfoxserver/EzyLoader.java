/**
 * 
 */
package com.tvd12.ezyfoxserver;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.service.EzySessionTokenGenerator;
import com.tvd12.ezyfoxserver.service.impl.EzySimpleSessionTokenGenerator;
import com.tvd12.ezyfoxserver.setting.EzyEventControllersSetting;
import com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting;
import com.tvd12.ezyfoxserver.setting.EzySettings;
import com.tvd12.ezyfoxserver.setting.EzySettingsReader;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettingsReader;
import com.tvd12.ezyfoxserver.statistics.EzySimpleStatistics;
import com.tvd12.ezyfoxserver.statistics.EzyStatistics;
import com.tvd12.ezyfoxserver.wrapper.EzyEventControllers;
import com.tvd12.ezyfoxserver.wrapper.EzyServerControllers;
import com.tvd12.ezyfoxserver.wrapper.EzySessionManager;
import com.tvd12.ezyfoxserver.wrapper.EzySimpleSessionManager;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyEventControllersImpl;
import com.tvd12.ezyfoxserver.wrapper.impl.EzyServerControllersImpl;

/**
 * @author tavandung12
 *
 */
@SuppressWarnings("rawtypes")
public abstract class EzyLoader extends EzyLoggable {
    
    protected EzyConfig config;
    protected ClassLoader classLoader;
    
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
                .build();
    }
    
    protected EzyStatistics newStatistics() {
        return new EzySimpleStatistics();
    }
    
    protected abstract EzySimpleSessionManager.Builder 
            createSessionManagerBuilder(EzySettings settings);
    
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
    
    protected Map<String, EzyAppClassLoader> newAppClassLoaders() {
        Map<String, EzyAppClassLoader> answer = new ConcurrentHashMap<>();
        for(File dir : getEntryFolders())
            answer.put(dir.getName(), newAppClassLoader(dir));
        return answer;
    }
    
    protected EzyAppClassLoader newAppClassLoader(File dir) {
    	    logger.info("load: {}", dir);
        return new EzyAppClassLoader(dir, classLoader);
    }
    
    protected EzyEventControllers newEventControllers(EzyEventControllersSetting setting) {
        return EzyEventControllersImpl.create(setting);
    }
    
    protected File[] getEntryFolders() {
        File entries = getEntriesFolder();
        return entries.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
    
    protected File getEntriesFolder() {
        String entriesPath = getEntriesPath();
        File entries = new File(entriesPath);
        return entries;
    }
    
    protected String getEntriesPath() {
        return getPath(getAppsPath(), EzyFolderNamesSetting.ENTRIES);
    }
    
    protected String getAppsPath() {
    	    return getPath(getHomePath(), EzyFolderNamesSetting.APPS);
    }
    
    protected String getSettingsPath() {
        return getPath(getHomePath(), EzyFolderNamesSetting.SETTINGS);
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
    
    
}
