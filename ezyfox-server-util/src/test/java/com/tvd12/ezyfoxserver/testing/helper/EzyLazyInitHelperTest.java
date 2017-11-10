package com.tvd12.ezyfoxserver.testing.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.function.EzyInitialize;
import com.tvd12.ezyfoxserver.function.EzyVoid;
import com.tvd12.ezyfoxserver.helper.EzyLazyInitHelper;
import com.tvd12.test.base.BaseTest;
import com.tvd12.test.reflect.ReflectMethodUtil;

public class EzyLazyInitHelperTest extends BaseTest {

	@Test
	public void test() {
		EzyLazyInitHelper.init(this, () -> new String());
		EzyLazyInitHelper.init(this, () -> new String(), () -> new String());
	}
	
	@Test
	public void test2() {
		Map<String, String> map = new HashMap<>();
		EzyLazyInitHelper.voidInit(this, 
				() -> !map.containsKey("1"), 
				() -> map.put("1", "a"));
		assert map.get("1").equals("a");
		EzyLazyInitHelper.voidInit(this, 
				() -> !map.containsKey("1"), 
				() -> map.put("1", "v"));
		assert map.get("1").equals("a");
	}
	
	@Test
	public void test1() throws Exception {
		Method method = ReflectMethodUtil.getMethod("syncInit",
				EzyLazyInitHelper.class, Object.class, Supplier.class, EzyInitialize.class);
		method.setAccessible(true);
		method.invoke(null, this, new Supplier<String>() {
				@Override
				public String get() {
					return null;
				}
			}, new EzyInitialize<String>() {
				@Override
				public String init() {
					return new String();
				}
		});
		
		method.invoke(null, this, new Supplier<String>() {
				@Override
				public String get() {
					return new String();
				}
			}, new EzyInitialize<String>() {
				@Override
				public String init() {
					return new String();
				}
		});
	}
	
	
	@Test
	public void test3() throws Exception {
		Map<String, String> map = new HashMap<>();
		Method method = ReflectMethodUtil.getMethod("syncVoidInit",
				EzyLazyInitHelper.class, Object.class, Supplier.class, EzyVoid.class);
		method.setAccessible(true);
		method.invoke(null, this, new Supplier<Boolean>() {
				@Override
				public Boolean get() {
					return !map.containsKey("1");
				}
			}, new EzyVoid() {
				@Override
				public void apply() {
					map.put("1", "a");
				}
		});
		
		assert map.get("1").equals("a");
		
		method.invoke(null, this, new Supplier<Boolean>() {
				@Override
				public Boolean get() {
					return !map.containsKey("1");
				}
			}, new EzyVoid() {
				@Override
				public void apply() {
					map.put("1", "b");
				}
		});
		assert map.get("1").equals("a");
	}
	
	@Test(expectedExceptions = {InvocationTargetException.class})
	public void test4() throws Exception {
		Map<String, String> map = new HashMap<>();
		Method method = ReflectMethodUtil.getMethod("syncVoidInit",
				EzyLazyInitHelper.class, Object.class, Supplier.class, EzyVoid.class);
		method.setAccessible(true);
		method.invoke(EzyLazyInitHelper.class, map, new Supplier<Boolean>() {
				@Override
				public Boolean get() {
					return !map.containsKey("1");
				}
			}, new EzyVoid() {
				@Override
				public void apply() {
					map.put("1", "a");
				}
		});
		
		assert map.get("1").equals("a");
		
		method.invoke(EzyLazyInitHelper.class, map, new Supplier<Boolean>() {
				@Override
				public Boolean get() {
					throw new RuntimeException();
				}
			}, new EzyVoid() {
				@Override
				public void apply() {
					map.put("1", "b");
				}
		});
		assert map.get("1").equals("a");
	}
	
	@Override
	public Class<?> getTestClass() {
		return EzyLazyInitHelper.class;
	}
	
	public static void main(String[] args) {
		String main="2014-11-30 13:30:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";
		DateFormat formatter = new SimpleDateFormat(pattern);
		try {
			Date date = formatter.parse(main);
			if(!main.equals(formatter.format(date)))
				System.out.println("Wrong input");
		} catch (ParseException e) {
			System.out.println("Wrong input");
		}
	}
	
}
