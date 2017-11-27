package com.tvd12.ezyfoxserver.morphia.bean;

import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoriesImplementer;

public final class EzyMorphiaRepositories {

	private EzyMorphiaRepositories() {
	}
	
	public static EzyMorphiaRepositoriesImplementer newRepositoriesImplementer() {
		return new EzyMorphiaRepositoriesImplementer();
	}
	
}
