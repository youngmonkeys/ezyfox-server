package com.tvd12.ezyfoxserver.util;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;


public final class EzyNameStyles {

	private EzyNameStyles() {
	}

	public static String toLowerHyphen(String name) {
		return UPPER_CAMEL.to(LOWER_HYPHEN, name);
	}
	
}
