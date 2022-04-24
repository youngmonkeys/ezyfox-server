package com.tvd12.ezyfoxserver.ssl;

import java.nio.file.Paths;

import com.tvd12.ezyfox.mapping.properties.EzyPropertiesFileReader;
import com.tvd12.ezyfox.mapping.properties.EzySimplePropertiesFileMapper;

public class EzySimpleSslConfigLoader implements EzySslConfigLoader {
    
    @Override
    public EzySslConfig load(String filePath) {
        EzySimpleSslConfig answer = readConfig(filePath);
        String parent = getParentFolder(filePath);
        answer.setKeyStoreFile(getPath(parent, answer.getKeyStoreFile()));
        answer.setKeyStorePasswordFile(getPath(parent, answer.getKeyStorePasswordFile()));
        answer.setCertificatePasswordFile(getPath(parent, answer.getCertificatePasswordFile()));
        return answer;
    }
    
    protected EzySimpleSslConfig readConfig(String filePath) {
        return newPropertiesReader().read(filePath, EzySimpleSslConfig.class);
    }
    
    protected EzyPropertiesFileReader newPropertiesReader() {
        return EzySimplePropertiesFileMapper.builder()
                .context(getClass())
                .build();
    }

    protected String getParentFolder(String filePath) {
        return Paths.get(filePath).getParent().toString();
    }

    protected String getPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

}
