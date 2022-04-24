package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyObject;
import com.tvd12.ezyfox.reflect.EzyClasses;
import com.tvd12.ezyfox.util.EzyLoggable;
import com.tvd12.ezyfoxserver.EzySimpleServer;
import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.setting.EzySimpleAppSetting;
import com.tvd12.test.base.BaseTest;

import java.util.concurrent.ConcurrentHashMap;

public class Performance3Test extends BaseTest {

    @SuppressWarnings("unused")
    public static void main(String args[]) {
        System.out.println("\n========= begin =========\n");
        long start = System.currentTimeMillis();
        ConcurrentHashMap<Object, Object> strs = new ConcurrentHashMap<>();
        strs.put(EzyCommand.class, EzyObject.class);
        strs.put(EzyObject.class, EzyObject.class);
        strs.put(EzyArray.class, EzyObject.class);
        strs.put(EzySimpleAppSetting.class, EzyObject.class);
        strs.put(EzySimpleServer.class, EzyObject.class);
        strs.put(EzyLoggable.class, EzyObject.class);
        strs.put(EzyClasses.class, EzyObject.class);
        for (int i = 0; i < 10000000; ++i) {
            Object abc = strs.get(EzyLoggable.class);
        }
        long end = System.currentTimeMillis();
        System.out.println("time = " + (end - start));
        System.out.println("\n========= end =========\n");
    }
}
