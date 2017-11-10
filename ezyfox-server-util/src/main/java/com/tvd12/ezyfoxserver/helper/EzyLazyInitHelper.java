package com.tvd12.ezyfoxserver.helper;

import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.function.EzyInitialize;
import com.tvd12.ezyfoxserver.function.EzyVoid;

public final class EzyLazyInitHelper {
	
	private EzyLazyInitHelper() {
	}

	public static <T> T init(
			Object context, EzyInitialize<T> initer) {
		return init(context, () -> null, initer);
	}
	
	public static <T> T init(
			Object context, Supplier<T> current, EzyInitialize<T> initer) {
		T value = current.get();
		return value != null ? value : syncInit(context, current, initer); 
	}
	
	private static <T> T syncInit(
			Object context, Supplier<T> current, EzyInitialize<T> initer) {
		synchronized (context) {
			T value = current.get();
			return value != null ? value : initer.init();
		}
	}
	
	public static void voidInit(
			Object context, Supplier<Boolean> condition, EzyVoid applier) {
		if(condition.get())
			syncVoidInit(context, condition, applier);
	}
	
	private static void syncVoidInit(
			Object context, Supplier<Boolean> condition, EzyVoid applier) {
		synchronized (context) {
			if(condition.get())
				applier.apply();
		}
	}
	
}
