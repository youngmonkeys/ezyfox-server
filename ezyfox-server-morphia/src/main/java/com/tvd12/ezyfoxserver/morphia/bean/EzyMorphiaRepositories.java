package com.tvd12.ezyfoxserver.morphia.bean;

import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoriesImplementor;

public final class EzyMorphiaRepositories {

	private EzyMorphiaRepositories() {
	}
	
	public static EzyMorphiaRepositoriesImplementor newRepositoriesImplementor() {
		return new EzyMorphiaRepositoriesImplementor();
	}
	
}
