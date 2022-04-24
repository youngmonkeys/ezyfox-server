package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.util.EzyToMap;

public interface EzyBaseSetting extends EzyToMap {

    int getId();

    String getName();

    String getFolder();

    String getActiveProfiles();

    String getPackageName();

    int getZoneId();

    String getLocation();

    String getEntryLoader();

    Object[] getEntryLoaderArgs();

    int getThreadPoolSize();

    /**
     * return the path of configuration file which you input
     * if noParent is true see {@code getConfigFileInput} function
     * if noParent is false see {@code getConfigFile} function.
     *
     * @param noParent include parent path or not
     * @return the path of configuration file
     */
    String getConfigFile(boolean noParent);

    /**
     * return the full path configuration file which you input.
     *
     * @return the full path of configuration file
     */
    String getConfigFile();

    /**
     * return the input configuration file which you input
     * let's say you setting up the configuration file is config.properties
     * so this function will return config.properties.
     *
     * @return the input configuration file
     */
    String getConfigFileInput();
}
