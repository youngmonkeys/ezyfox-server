package com.tvd12.ezyfoxserver.config;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfox.mapping.properties.EzyPropertiesFileReader;
import com.tvd12.ezyfox.mapping.properties.EzySimplePropertiesFileMapper;

public class EzySimpleConfigLoader implements EzyConfigLoader {

    @Override
    public EzyConfig load(String filePath) {
        if (EzyStrings.isNoContent(filePath)) {
            return EzySimpleConfig.defaultConfig();
        }
        EzyPropertiesFileReader reader = newPropertiesReader();
        EzySimpleConfig config = reader.read(filePath, EzySimpleConfig.class);
        return config;
    }

    protected EzyPropertiesFileReader newPropertiesReader() {
        return EzySimplePropertiesFileMapper.builder()
            .context(getClass())
            .build();
    }
}
