package com.tvd12.ezyfoxserver.util;

public final class EzyIfElse {

	private EzyIfElse() {
	}
	
	public static void withIf(boolean condition, Runnable ifTask) {
		if(condition)
			ifTask.run();
	}
	
	public static void withElse(boolean condition, Runnable elseTask) {
		if(!condition)
			elseTask.run();
	}
	
	public static void withIfElse(boolean condition, Runnable ifTask, Runnable elseTask) {
		if(condition)
			ifTask.run();
		else
			elseTask.run();
	}
}
