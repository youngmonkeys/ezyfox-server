package com.tvd12.ezyfoxserver.morphia.impl;

import com.tvd12.ezyfoxserver.mongodb.bean.EzySimpleRepositoriesImplementor;
import com.tvd12.ezyfoxserver.mongodb.bean.EzySimpleRepositoryImplementor;

public class EzyMorphiaRepositoriesImplementor
		extends EzySimpleRepositoriesImplementor {
	
	@Override
	protected EzySimpleRepositoryImplementor newRepoImplementor(Class<?> itf) {
		return new EzyMorphiaRepositoryImplementor(itf);
	}
	
}
