package com.tvd12.ezyfoxserver.util;

import java.util.function.Consumer;

public interface EzyForeach {

	<T> void forEach(Consumer<T> action);
	
}
