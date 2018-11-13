package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfoxserver.ccl.EzyAppClassLoader;
import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public class EzyAppsStarter extends EzyZoneComponentsStater {

    protected final Map<String, EzyAppClassLoader> appClassLoaders;
    
    protected EzyAppsStarter(Builder builder) {
        super(builder);
        this.appClassLoaders = builder.appClassLoaders;
    }
    
    @Override
    public void start() {
        startAllApps();
    }
    
    protected void startAllApps() {
        Set<String> appNames = getAppNames();
        logger.info("start apps: {}", appNames);
        appNames.forEach(this::startApp);
    }
    
    protected void startApp(String appName) {
        try {
            logger.debug("app: " + appName + " loading...");
            EzyAppContext context = zoneContext.getAppContext(appName);
            EzyApplication application = context.getApp();
            EzyAppEntry entry = startApp(appName, newAppEntryLoader(appName));
            ((EzyEntryAware)application).setEntry(entry);
            logger.debug("app: " + appName + " loaded");
        }
        catch(Exception e) {
            logger.error("can not start app " + appName, e);
        } 
    }
    
    protected EzyAppEntry startApp(String appName, EzyAppEntryLoader loader) throws Exception {
        EzyAppEntry entry = loader.load();
        entry.config(getAppContext(appName));
        entry.start();
        return entry;
    }
    
    protected Set<String> getAppNames() {
        return zoneSetting.getAppNames();
    }
    
    protected EzyAppSetting getAppByName(String name) {
        return zoneSetting.getAppByName(name);
    }
    
    protected Class<EzyAppEntryLoader> 
            getAppEntryLoaderClass(String appName) throws Exception {
        return getAppEntryLoaderClass(getAppByName(appName));
    }
    
    @SuppressWarnings("unchecked")
    protected Class<EzyAppEntryLoader> 
            getAppEntryLoaderClass(EzyAppSetting app) throws Exception {
        EzyAppClassLoader classLoader = getClassLoader(app.getName(), app.getFolder());
        return (Class<EzyAppEntryLoader>) 
                Class.forName(app.getEntryLoader(), true, classLoader);
    }
    
    protected EzyAppEntryLoader newAppEntryLoader(String appName) throws Exception {
         Class<EzyAppEntryLoader> entryLoaderClass = getAppEntryLoaderClass(appName);
         return entryLoaderClass.newInstance();
    }
    
    protected EzyAppClassLoader getClassLoader(String appName, String appFolder) {
        if(appClassLoaders.containsKey(appFolder)) 
            return appClassLoaders.get(appFolder);
        throw new IllegalArgumentException(
                "folder: " + appFolder + " for app: " + appName + " doesn't exist");
    }
    
    protected EzyAppContext getAppContext(String appName) {
        return zoneContext.getAppContext(appName);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder extends EzyZoneComponentsStater.Builder<EzyAppsStarter, Builder> {
        protected Map<String, EzyAppClassLoader> appClassLoaders;
        
        public Builder appClassLoaders(Map<String, EzyAppClassLoader> appClassLoaders) {
            this.appClassLoaders = appClassLoaders;
            return this;
        }
        
        @Override
        public EzyAppsStarter build() {
            return new EzyAppsStarter(this);
        }
    }
    
}
