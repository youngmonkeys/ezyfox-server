package com.tvd12.ezyfoxserver.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.tvd12.ezyfox.io.EzyStrings;
import com.tvd12.ezyfoxserver.setting.EzyFolderNamesSetting;

import ch.qos.logback.classic.util.ContextInitializer;

public class EzyLoggerConfig {
    
    private static final EzyLoggerConfig INSTANCE = new EzyLoggerConfig();
    
    private EzyLoggerConfig() {}
    
    public static EzyLoggerConfig getInstance() {
        return INSTANCE;
    }

    public void config(String configFile) throws IOException {
        File loggerConfigFile = getLoggerConfigFile(configFile);
        if(loggerConfigFile.exists()) {
            System.setProperty(
                ContextInitializer.CONFIG_FILE_PROPERTY,
                loggerConfigFile.toString()
            );
        }
    }
    
    private File getLoggerConfigFile(String configFile) throws IOException {
        Properties configuration = readConfiguration(configFile);
        String homePath = configuration.getProperty("ezyfox.home");
        String loggerFile = configuration.getProperty("logger.config.file", "logback.xml");
        Path loggerFilePath = EzyStrings.isBlank(homePath)
            ? Paths.get(EzyFolderNamesSetting.SETTINGS, loggerFile)
            : Paths.get(homePath, EzyFolderNamesSetting.SETTINGS, loggerFile);
        return loggerFilePath.toFile();
    }
    
    private Properties readConfiguration(String configFile) throws IOException {
        Properties properties = new Properties();
        if (configFile != null) {
            try(InputStream inputStream = new FileInputStream(configFile)) {
                properties.load(inputStream);
            }
        }
        return properties;
    }
}
