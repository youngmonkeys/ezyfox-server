package com.tvd12.ezyfoxserver.setting;

import com.tvd12.ezyfox.constant.EzyConstant;

import java.util.Set;

public interface EzyPluginSetting extends EzyBaseSetting {

    int getPriority();

    EzyListenEvents getListenEvents();

    /**
     * return the full path configuration file which you input
     * let's say you setup the configuration file is config.properties
     * so this function will return
     * {ezyfox_server_home_path}/plugins/{plugin_folder}/config.properties
     *
     * @return the full path of configuration file
     */
    @Override
    String getConfigFile();

    interface EzyListenEvents {

        Set<EzyConstant> getEvents();

    }
}
