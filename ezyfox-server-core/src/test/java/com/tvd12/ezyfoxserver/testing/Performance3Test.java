package com.tvd12.ezyfoxserver.testing;

import java.util.concurrent.ConcurrentHashMap;

import com.tvd12.ezyfoxserver.EzyServer;
import com.tvd12.ezyfoxserver.command.EzyCommand;
import com.tvd12.ezyfoxserver.command.EzyFetchAppByName;
import com.tvd12.ezyfoxserver.command.EzyFetchServer;
import com.tvd12.ezyfoxserver.command.EzyRunWorker;
import com.tvd12.ezyfoxserver.command.EzySendMessage;
import com.tvd12.ezyfoxserver.config.EzyApp;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.reflect.EzyClassUtil;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.test.base.BaseTest;

public class Performance3Test extends BaseTest {

	@SuppressWarnings("unused")
	public static void main(String args[]) {
		System.out.println("\n========= begin =========\n");
		long start = System.currentTimeMillis();
		ConcurrentHashMap<Object, Object> strs = new ConcurrentHashMap<>();
		strs.put(EzyCommand.class, EzyObject.class);
		strs.put(EzyFetchAppByName.class, EzyObject.class);
		strs.put(EzyFetchServer.class, EzyObject.class);
		strs.put(EzyRunWorker.class, EzyObject.class);
		strs.put(EzySendMessage.class, EzyObject.class);
		strs.put(EzyObject.class, EzyObject.class);
		strs.put(EzyArray.class, EzyObject.class);
		strs.put(EzyApp.class, EzyObject.class);
		strs.put(EzyServer.class, EzyObject.class);
		strs.put(EzyLoggable.class, EzyObject.class);
		strs.put(EzyClassUtil.class, EzyObject.class);
		for(int i = 0 ; i < 10000000 ; i++) {
			Object abc = strs.get(EzyLoggable.class);
		}
		long end = System.currentTimeMillis();
		System.out.println("time = " + (end - start));
		System.out.println("\n========= end =========\n");
	}
	
}
