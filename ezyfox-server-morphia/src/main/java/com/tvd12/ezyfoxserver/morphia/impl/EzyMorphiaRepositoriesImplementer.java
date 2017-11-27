package com.tvd12.ezyfoxserver.morphia.impl;

import com.tvd12.ezyfoxserver.mongodb.bean.EzySimpleRepositoriesImplementer;
import com.tvd12.ezyfoxserver.mongodb.bean.EzySimpleRepositoryImplementer;

public class EzyMorphiaRepositoriesImplementer
		extends EzySimpleRepositoriesImplementer {
	
	@Override
	protected EzySimpleRepositoryImplementer newRepoImplementer(Class<?> itf) {
		return new EzyMorphiaRepositoryImplementer(itf);
	}
	
}
