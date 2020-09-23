package com.tvd12.ezyfoxserver;

import java.util.Map;
import java.util.Set;

import com.tvd12.ezyfoxserver.context.EzyAppContext;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyEntryAware;
import com.tvd12.ezyfoxserver.setting.EzyAppSetting;

public class EzyAppsStarter extends EzyZoneComponentsStater {

    protected final ClassLoader classLoader;
    protected final boolean enableAppClassLoader;
    protected final Map<String, ClassLoader> appClassLoaders;
    
    protected EzyAppsStarter(Builder builder) {
        super(builder);
        this.classLoader = builder.classLoader;
        this.appClassLoaders = builder.appClassLoaders;
        this.enableAppClassLoader = builder.enableAppClassLoader;
    }
    
    @Override
    public void start() {
        startAllApps();
    }
    
    protected void startAllApps() {
        Set<String> appNames = getAppNames();
        logger.info("start apps: {}", appNames);
        for(String appName : appNames)
            this.startApp(appName);
    }
    
    protected void startApp(String appName) {
        try {
            logger.debug("app: {} loading...", appName);
            EzyAppContext context = zoneContext.getAppContext(appName);
            EzyApplication application = context.getApp();
            EzyAppEntry entry = startApp(appName, newAppEntryLoader(appName));
            ((EzyEntryAware)application).setEntry(entry);
            logger.debug("app: {} loaded", appName);
        }
        catch(Exception e) {
            logger.error("can not start app: {}", appName, e);
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
        ClassLoader classLoader = getAppClassLoader(app.getName(), app.getFolder());
        return (Class<EzyAppEntryLoader>) 
                Class.forName(app.getEntryLoader(), true, classLoader);
    }
    
    protected EzyAppEntryLoader newAppEntryLoader(String appName) throws Exception {
         Class<EzyAppEntryLoader> entryLoaderClass = getAppEntryLoaderClass(appName);
         EzyAppSetting appSetting = getAppByName(appName);
         if(appSetting.getEntryLoaderArgs() == null)
             return entryLoaderClass.newInstance();
         return (EzyAppEntryLoader) entryLoaderClass.getConstructors()[0]
                 .newInstance(appSetting.getEntryLoaderArgs());
    }
    
    protected ClassLoader getAppClassLoader(String appName, String appFolder) {
        ClassLoader appClassLoader = appClassLoaders.get(appFolder);
        if(appClassLoader != null) 
            return appClassLoader;
        if(!enableAppClassLoader && classLoader != null)
            return classLoader;
        throw new IllegalArgumentException(
                "folder: " + appFolder + " for app: " + appName + " doesn't exist");
    }
    
    protected EzyAppContext getAppContext(String appName) {
        return zoneContext.getAppContext(appName);
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder 
            extends EzyZoneComponentsStater.Builder<EzyAppsStarter, Builder> {
        
        protected ClassLoader classLoader;
        protected boolean enableAppClassLoader;
        protected Map<String, ClassLoader> appClassLoaders;
        
        public Builder classLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
            return this;
        }
        
        public Builder enableAppClassLoader(boolean enableAppClassLoader) {
            this.enableAppClassLoader = enableAppClassLoader;
            return this;
        }
        
        public Builder appClassLoaders(Map<String, ClassLoader> appClassLoaders) {
            this.appClassLoaders = appClassLoaders;
            return this;
        }
        
        @Override
        public EzyAppsStarter build() {
            return new EzyAppsStarter(this);
        }
    }
    
}
