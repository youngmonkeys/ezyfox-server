package com.tvd12.ezyfoxserver.morphia.testing.repo.impl;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;
import com.tvd12.ezyfoxserver.morphia.repository.EzyDatastoreRepository;
import com.tvd12.ezyfoxserver.morphia.testing.data.Monkey;
import com.tvd12.ezyfoxserver.morphia.testing.repo.MonkeyRepo;

@EzySingleton
public class MonkeyRepoImpl
		extends EzyDatastoreRepository<Long, Monkey>
		implements MonkeyRepo {

	@Override
	public void save2Monkey(Monkey monkey1, Monkey monkey2) {
		datastore.save(Lists.newArrayList(monkey1, monkey2));
	}
	
	@Override
	protected Class<Monkey> getEntityType() {
		return Monkey.class;
	}
	
}
