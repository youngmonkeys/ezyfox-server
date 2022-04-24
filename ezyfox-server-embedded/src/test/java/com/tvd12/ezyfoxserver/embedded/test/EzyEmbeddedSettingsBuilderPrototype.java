package com.tvd12.ezyfoxserver.embedded.test;

import java.util.HashSet;
import java.util.Set;

import com.tvd12.ezyfoxserver.ext.EzyAbstractAppEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAbstractPluginEntryLoader;
import com.tvd12.ezyfoxserver.ext.EzyAppEntry;
import com.tvd12.ezyfoxserver.ext.EzyPluginEntry;
import com.tvd12.ezyfoxserver.setting.EzyAppSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzyPluginSettingBuilder;
import com.tvd12.ezyfoxserver.setting.EzySettingsBuilder;
import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.ezyfoxserver.setting.EzyZoneSettingBuilder;
import com.tvd12.ezyfoxserver.support.entry.EzySimpleAppEntry;
import com.tvd12.ezyfoxserver.support.entry.EzySimplePluginEntry;

public class EzyEmbeddedSettingsBuilderPrototype extends EzySettingsBuilder {

    protected Set<String> packagesToScan;
    protected static final String DEFAULT_NAME = "default";
    
    public EzyEmbeddedSettingsBuilderPrototype() {
        this.packagesToScan = new HashSet<>();
    }
    
    public EzyEmbeddedSettingsBuilderPrototype scan(String packageToScan) {
        this.packagesToScan.add(packageToScan);
        return this;
    }
    
    public EzyEmbeddedSettingsBuilderPrototype scan(String... packagesToScan) {
        for(String packageToScan : packagesToScan)
            scan(packageToScan);
        return this;
    }
    
    public EzyEmbeddedSettingsBuilderPrototype scan(Iterable<String> packagesToScan) {
        for(String packageToScan : packagesToScan)
            scan(packageToScan);
        return this;
    }
    
    @Override
    public EzySimpleSettings build() {
        if(zones.getSize() == 0) {
            EzyPluginSettingBuilder pluginSettingBuilder = new EzyPluginSettingBuilder()
                    .name(DEFAULT_NAME)
                    .entryLoader(EmbeddedPluginEntryLoader.class)
                    .entryLoaderArgs(new Object[] {packagesToScan});
            
            EzyAppSettingBuilder appSettingBuilder = new EzyAppSettingBuilder()
                    .name(DEFAULT_NAME)
                    .entryLoader(EmbeddedAppEntryLoader.class)
                    .entryLoaderArgs(new Object[] {packagesToScan});
            
            EzyZoneSettingBuilder zoneSettingBuilder = new EzyZoneSettingBuilder()
                    .name(DEFAULT_NAME)
                    .application(appSettingBuilder.build())
                    .plugin(pluginSettingBuilder.build());
            zones.setItem(zoneSettingBuilder.build());
        }
        return super.build();
    }
    
    public static class EmbeddedAppEntry extends EzySimpleAppEntry {

        protected String[] packagesToScan;
        
        public EmbeddedAppEntry(String[] packagesToScan) {
            this.packagesToScan = packagesToScan;
        }
        
        @Override
        protected String[] getScanableBeanPackages() {
            return this.packagesToScan;
        }

        @Override
        protected String[] getScanableBindingPackages() {
            return this.packagesToScan;
        }
        
    }
    
    public static class EmbeddedAppEntryLoader extends EzyAbstractAppEntryLoader {

        protected String[] packagesToScan;
        
        public EmbeddedAppEntryLoader(Set<String> packagesToScan) {
            this.packagesToScan = 
                    packagesToScan.toArray(new String[packagesToScan.size()]);
        }
        
        @Override
        public EzyAppEntry load() throws Exception {
            return new EmbeddedAppEntry(packagesToScan);
        }
        
    }
    
    public static class EmbeddedPluginEntry extends EzySimplePluginEntry {

        protected String[] packagesToScan;
        
        public EmbeddedPluginEntry(String[] packagesToScan) {
            this.packagesToScan = packagesToScan;
        }
        
        @Override
        protected String[] getScanableBeanPackages() {
            return this.packagesToScan;
        }

    }

    public static class EmbeddedPluginEntryLoader extends EzyAbstractPluginEntryLoader {

        protected String[] packagesToScan;
        
        public EmbeddedPluginEntryLoader(Set<String> packagesToScan) {
            this.packagesToScan = 
                    packagesToScan.toArray(new String[packagesToScan.size()]);
        }
        
        @Override
        public EzyPluginEntry load() throws Exception {
            return new EmbeddedPluginEntry(packagesToScan);
        }
        
    }
}
