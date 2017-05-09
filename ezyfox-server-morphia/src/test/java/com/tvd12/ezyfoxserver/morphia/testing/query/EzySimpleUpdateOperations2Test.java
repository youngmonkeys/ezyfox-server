package com.tvd12.ezyfoxserver.morphia.testing.query;

import java.util.concurrent.TimeUnit;

import org.mongodb.morphia.FindAndModifyOptions;
import org.mongodb.morphia.query.UpdateOperations;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.morphia.query.impl.EzySimpleFindAndModifyOptions;
import com.tvd12.ezyfoxserver.morphia.query.impl.EzySimpleUpdateOperations;
import com.tvd12.ezyfoxserver.morphia.testing.BaseMongoDBTest;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;
import com.tvd12.ezyfoxserver.morphia.testing.data.Kitty;

public class EzySimpleUpdateOperations2Test extends BaseMongoDBTest {

	@Test
	public void test() {
		UpdateOperations<Cat> realOperations = DATASTORE.createUpdateOperations(Cat.class);
		EzySimpleUpdateOperations<Cat> proxyOperations = new EzySimpleUpdateOperations<>(realOperations);
		
		proxyOperations.disableValidation();
		proxyOperations.enableValidation();
		proxyOperations.inc("age", 10);
		assert !proxyOperations.isIsolated();
		proxyOperations.isolated();
		assert proxyOperations.isIsolated();
		proxyOperations.push("valueSet", Lists.newArrayList("e", "f"));
		proxyOperations.push("kitties", new Kitty(100L), options -> options.sort("age", 1));
		proxyOperations.removeAll("valueList", Lists.newArrayList("a", "b", "c"));
		proxyOperations.setOnInsert("free", 100);
		proxyOperations.unset("unset");
		
		
		FindAndModifyOptions realOptions = new FindAndModifyOptions();
		EzySimpleFindAndModifyOptions proxyOptions = new EzySimpleFindAndModifyOptions(realOptions);
		proxyOptions.upsert(true)
			.maxTime(10, TimeUnit.SECONDS)
			.remove(false)
			.returnNew(true);
		assert !proxyOptions.isRemove();
		assert proxyOptions.isReturnNew();
		assert proxyOptions.isUpsert();
		assert proxyOptions.getMaxTime(TimeUnit.SECONDS) == 10;
		
		Cat cat = new Cat();
		DATASTORE.delete(DATASTORE.createQuery(Cat.class));
		DATASTORE.findAndModify(DATASTORE.createQuery(
				Cat.class).field("id").equal(cat.getId()), 
				realOperations,
				realOptions);
		
	}
	
}
