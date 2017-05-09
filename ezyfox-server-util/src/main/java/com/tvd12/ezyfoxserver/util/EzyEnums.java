package com.tvd12.ezyfoxserver.util;

import com.tvd12.ezyfoxserver.constant.EzyHasId;

public final class EzyEnums {

	private EzyEnums() {
	}
	
	public static <T extends EzyHasId> T valueOf(T[] values, int id) {
		for(T v : values)
            if(v.getId() == id)
                return v;
        throw new IllegalArgumentException("has no enum value with id = " + id);
	}
}
