package com.tvd12.ezyfoxserver.util;

import java.util.function.Function;

import com.tvd12.ezyfoxserver.constant.EzyHasIntId;

public final class EzyEnums {

	private EzyEnums() {
	}
	
	public static <T extends EzyHasIntId> T valueOf(T[] values, int id) {
		return valueOf(values, id, v -> v.getId());
	}
	
	public static <T> T valueOf(
			T[] values, Object id, Function<T, Object> idFetcher) {
		for(T v : values) {
			Object vid = idFetcher.apply(v);
            if(vid.equals(id)) {
                return v;
            }
		}
        throw new IllegalArgumentException("has no enum value with id = " + id);
	}
}
