package com.tvd12.ezyfoxserver;

public final class EzySystem {

    private static final EzyEnvironment ENV = newEnviroment();

    private EzySystem() {
    }

    public static EzyEnvironment getEnv() {
        return ENV;
    }

    private static EzyEnvironment newEnviroment() {
        EzyEnvironment env = new EzyEnvironment();
        env.setProperty(EzyEnvironment.DATE_FORMAT_PATTERN, "yyyy-MM-dd'T'HH:mm:ss:SSS");
        return env;
    }

}
