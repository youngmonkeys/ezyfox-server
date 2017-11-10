package com.tvd12.ezyfoxserver.morphia.testing;

import java.util.Map;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.mongodb.EzyMongoRepository;
import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoriesImplementor;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;

public class EzySimpleRepositoriesImplementorTest extends BaseMongoDBTest {

	@Test
	public void test() {
		EzyMorphiaRepositoriesImplementor implementor = new EzyMorphiaRepositoriesImplementor();
		implementor.repositoryInterfaces(Object.class);
		implementor.repositoryInterfaces(InterfaceA.class);
		implementor.repositoryInterfaces(CatXRepo.class);
		Map<Class<?>, Object> map = implementor.implement(DATASTORE);
		assert map.containsKey(CatXRepo.class);
	}
	
	public static interface InterfaceA {
		
	}
	
	public static interface CatXRepo extends EzyMongoRepository<String, Cat> {
		
	}
}
