package com.tvd12.ezyfoxserver.config;

import java.util.HashMap;
import java.util.Map;

import com.tvd12.properties.file.annotation.Property;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EzySimpleConfig implements EzyConfig {

    @Property("ezyfox.home")
    protected String ezyfoxHome;
    
    @Property("settings.print")
    protected boolean printSettings;
    
    @Property("banner.print")
    protected boolean printBanner;
    
    @Property("banner.file")
    protected String bannerFile;
    
    @Property("logger.config.file")
    protected String loggerConfigFile;
    
    @Property("app.classloader.enable")
    protected boolean enableAppClassLoader;
    
    public EzySimpleConfig() {
        this.printSettings = true;
        this.printBanner = true;
        this.enableAppClassLoader = true;
        this.bannerFile = "ezyfox-banner.txt";
    }
    
    public static EzySimpleConfig defaultConfig() {
        EzySimpleConfig config = new EzySimpleConfig();
        config.setEnableAppClassLoader(false);
        return config;
    }
    
    public String getEzyfoxHome() {
        if(ezyfoxHome == null)
            return "";
        return ezyfoxHome;
    }
    
    @Override
    public Map<Object, Object> toMap() {
        Map<Object, Object> map = new HashMap<>();
        map.put("ezyfoxHome", getEzyfoxHome());
        map.put("loggerConfigFile", loggerConfigFile != null ? loggerConfigFile : "default");
        map.put("enableAppClassLoader", enableAppClassLoader);
        return map;
    }
    
}
