package com.tvd12.ezyfoxserver.config;

import com.tvd12.ezyfoxserver.mapping.properties.EzyPropertiesFileReader;
import com.tvd12.ezyfoxserver.mapping.properties.EzySimplePropertiesFileMapper;

public class EzySimpleConfigLoader implements EzyConfigLoader {
    
    @Override
    public EzyConfig load(String filePath) {
        return newPropertiesReader().read(filePath, EzySimpleConfig.class);
    }
    
    protected EzyPropertiesFileReader newPropertiesReader() {
        return EzySimplePropertiesFileMapper.builder()
                .context(getClass())
                .build();
    }
}
