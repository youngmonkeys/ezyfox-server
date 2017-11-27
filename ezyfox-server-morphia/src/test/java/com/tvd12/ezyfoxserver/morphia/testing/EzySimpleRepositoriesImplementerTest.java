package com.tvd12.ezyfoxserver.morphia.testing;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoriesImplementer;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;

public class EzySimpleRepositoriesImplementerTest extends BaseMongoDBTest {

	@Test
	public void test() {
		EzyMorphiaRepositoriesImplementer implementer = new EzyMorphiaRepositoriesImplementer();
		implementer.repositoryInterfaces(Object.class);
		implementer.repositoryInterfaces(InterfaceA.class);
		implementer.repositoryInterfaces(CatXRepo.class);
		Map<Class<?>, Object> map = implementer.implement(DATASTORE);
		assert map.containsKey(CatXRepo.class);
	}
	
	public static interface InterfaceA {
		
	}
	
	public static interface CatXRepo extends EzyMongoRepository<String, Cat> {
		
	}
}
