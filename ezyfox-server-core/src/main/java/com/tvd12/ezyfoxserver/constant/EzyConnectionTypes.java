package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;
import com.tvd12.ezyfox.util.EzyEnums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class EzyConnectionTypes {

    private static final Map<String, EzyConnectionType> BUILT_IN_TYPE_BY_NAME =
        EzyEnums.enumMap(EzyConnectionType.class, EzyConnectionType::getName);
    private static final Map<String, EzyConstant> EXTENDED_TYPE_BY_NAME =
        new ConcurrentHashMap<>();

    private EzyConnectionTypes() {}

    public static EzyConstant of(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        EzyConstant builtInType = BUILT_IN_TYPE_BY_NAME.get(name);
        if (builtInType != null) {
            return builtInType;
        }
        return EXTENDED_TYPE_BY_NAME.computeIfAbsent(
            name,
            EzyConstant::one
        );
    }

    public static boolean isBuiltIn(String name) {
        return BUILT_IN_TYPE_BY_NAME.containsKey(name);
    }
}
