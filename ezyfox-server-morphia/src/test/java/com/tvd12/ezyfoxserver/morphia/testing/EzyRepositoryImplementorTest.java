package com.tvd12.ezyfoxserver.morphia.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.morphia.impl.EzyMorphiaRepositoryImplementor;

public class EzyRepositoryImplementorTest extends BaseMongoDBTest {

	@Test(expectedExceptions = {IllegalStateException.class})
	public void test() {
		new EzyMorphiaRepositoryImplementor(ClassA.class).implement(DATASTORE);
	}
	
	public static class ClassA {
		
	}
	
}
