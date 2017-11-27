package com.tvd12.ezyfoxserver.morphia.testing.repo.autoimpl;

import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoryImplementer;
import com.tvd12.ezyfoxserver.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;
import com.tvd12.ezyfoxserver.morphia.testing.repo.CatRepo;

public class Try1 extends BaseMongoDBTest {
	
	public static void main(String[] args) throws Exception {
		EzyMorphiaRepositoryImplementer implementer = 
				new EzyMorphiaRepositoryImplementer(CatRepo.class);
		CatRepo repo = (CatRepo) implementer.implement(DATASTORE);
		Cat cat = repo.findById(2019072087L);
		System.out.println(cat);
	}
	
}
