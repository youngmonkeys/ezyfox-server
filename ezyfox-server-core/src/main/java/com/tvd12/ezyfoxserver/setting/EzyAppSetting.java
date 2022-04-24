package com.tvd12.ezyfoxserver.setting;

public interface EzyAppSetting extends EzyBaseSetting {

    int getMaxUsers();

    /**
     * return the full path configuration file which you input
     * let's say you setup the configuration file is config.properties
     * so this function will return
     * {ezyfox_server_home_path}/apps/entries/{app_folder}/config.properties
     *
     * @return the full path of configuration file
     */
    @Override
    String getConfigFile();

}
