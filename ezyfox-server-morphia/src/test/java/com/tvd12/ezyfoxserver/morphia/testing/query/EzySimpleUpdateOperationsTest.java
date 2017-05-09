package com.tvd12.ezyfoxserver.morphia.testing.query;

import org.mongodb.morphia.query.UpdateOperations;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.morphia.query.impl.EzySimpleUpdateOperations;
import com.tvd12.ezyfoxserver.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;
import com.tvd12.ezyfoxserver.morphia.testing.data.Kitty;

public class EzySimpleUpdateOperationsTest extends BaseMongoDBTest {

	@Test
	public void test() {
		UpdateOperations<Cat> realOperations = DATASTORE.createUpdateOperations(Cat.class);
		EzySimpleUpdateOperations<Cat> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		
		proxyOperations.addToSet("valueSet", "a");
		proxyOperations.addToSet("valueSet", Lists.newArrayList("b", "c"));
		proxyOperations.dec("age");
		proxyOperations.dec("age", 3);
		proxyOperations.disableValidation();
		proxyOperations.enableValidation();
		proxyOperations.inc("age");
		proxyOperations.inc("age", 10);
		assert !proxyOperations.isIsolated();
		proxyOperations.isolated();
		assert proxyOperations.isIsolated();
		proxyOperations.max("age", 100);
		proxyOperations.min("age", 0);
		proxyOperations.push("valueSet", "d");
		proxyOperations.push("valueSet", Lists.newArrayList("e", "f"));
		proxyOperations.push("age", Lists.newArrayList("g", "h"), options -> options.sort(1).slice(5).position(0));
		proxyOperations.push("kitties", new Kitty(100L), options -> options.sort("age", 1));
		proxyOperations.removeAll("valueList", "d");
		proxyOperations.removeAll("valueList", Lists.newArrayList("a", "b", "c"));
		proxyOperations.removeFirst("valueList");
		proxyOperations.removeLast("valueList");
		proxyOperations.setOnInsert("free", 100);
		proxyOperations.unset("unset");
		
		
	}
	
}
