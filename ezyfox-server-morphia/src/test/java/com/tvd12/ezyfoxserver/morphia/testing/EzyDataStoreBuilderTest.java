package com.tvd12.ezyfoxserver.morphia.testing;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;
import com.tvd12.ezyfoxserver.morphia.testing.data.Person;

public class EzyDataStoreBuilderTest extends BaseMongoDBTest {
	
	@Test
	public void test() {
		DATASTORE.save(new Cat());
		DATASTORE.save(new Person());
	}


}
