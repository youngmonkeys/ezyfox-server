package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EzyConnectionTypes {

    private static final Map<String, EzyConstant> TYPE_BY_NAME =
        new ConcurrentHashMap<>();

    static {
        for (EzyConnectionType type : EzyConnectionType.values()) {
            TYPE_BY_NAME.put(type.getName(), type);
        }
    }

    private EzyConnectionTypes() {}

    public static EzyConstant of(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        return TYPE_BY_NAME.computeIfAbsent(name, EzyConstant::one);
    }

    public static boolean isBuiltIn(EzyConstant type) {
        return type instanceof EzyConnectionType;
    }

    public static boolean isBuiltIn(String name) {
        for (EzyConnectionType type : EzyConnectionType.values()) {
            if (type.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
