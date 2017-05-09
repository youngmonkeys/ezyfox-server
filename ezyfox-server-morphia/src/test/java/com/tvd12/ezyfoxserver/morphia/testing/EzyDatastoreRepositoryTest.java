package com.tvd12.ezyfoxserver.morphia.testing;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.morphia.testing.data.Cat;
import com.tvd12.ezyfoxserver.morphia.testing.repo.CatRepo;
import com.tvd12.ezyfoxserver.morphia.testing.repo.DuckRepo;

public class EzyDatastoreRepositoryTest extends BaseMongoDBTest {

	@Test
	public void test() {
		CatRepo repo = (CatRepo) BEAN_CONTEXT.getBean(CatRepo.class);
		System.out.println("cat#count: " + repo.count());
		repo.deleteAll();
		assert repo.count() == 0;
		Cat cat1 = new Cat();
		Cat cat2 = new Cat();
		repo.save(Lists.newArrayList(cat1, cat2));
		assert cat1.equals(repo.findById(cat1.getId()));
		assert repo.findListByIds(Sets.newHashSet(cat1.getId(), cat2.getId())).size() == 2;
		assert repo.findListByField("fixedValue", "fixedValue").size() == 2;
		assert repo.findListByField("fixedValue", "fixedValue", 0, 1).size() == 1;
		assert repo.findAll().size() == 2;
		assert repo.findAll(0, 1).size() == 1;
		cat1.setName("updated name");
		repo.updateOneById(cat1.getId(), cat1);
		repo.delete(cat2.getId());
		assert repo.count() == 1;
		repo.save(cat2);
		assert repo.count() == 2;
		repo.deleteByIds(Lists.newArrayList(cat2.getId()));
		assert repo.count() == 1;
		Cat cat3 = new Cat();
		cat3.setId(cat1.getId() + 1);
		
		repo.findAndModifyById(cat1.getId(), 
				operations -> {
					operations
					.set("name", "cat#1#findAndModify")
					.set("fixedValue", "fixedValue");
				}
		);
		
		repo.findAndModifyById(cat3.getId(), 
				operations -> {
					operations
					.set("name", "cat#3#findAndModify")
					.set("fixedValue", "fixedValue");
				},
				options -> {
					options.upsert(true);
				}
		);
		
		DuckRepo duckRepo = (DuckRepo) BEAN_CONTEXT.getBean(DuckRepo.class);
		duckRepo.deleteAll();
		Duck duck1 = new Duck();
		duck1.setName("duck1");
		duckRepo.save(duck1);
		duckRepo.updateOneById(duck1.getId(), op -> op.set("name", "duck1#hasupdated"));
		
		Duck duck2 = new Duck(duck1.getId() + 1);
		duck2.setName("duck2");
		duckRepo.save(duck2);
		duck2.setName("duck2#updated");
		duckRepo.updateOneByField("name", "duck2", duck2);
		
		Duck duck3 = new Duck(duck2.getId() + 1);
		duck3.setName("duck3");
		duckRepo.save(duck3);
		duckRepo.updateOneByField("name", "duck3", op -> op.set("name", "duck3#updated"));
		
		Duck duck4 = new Duck(duck3.getId() + 1);
		duck4.setName("duck4");
		
		duckRepo.findAndModifyByField("name", "duck4", 
				operations -> operations
					.set("_id", duck4.getId())
					.set("name", duck4.getName())
					.set("fixedValue", "duck4FixedValue"),
				options -> options
					.upsert(true));
		
		Duck duck5 = new Duck(duck4.getId() + 1);
		duck5.setName("duck5");
		duckRepo.save(duck5);
		duckRepo.findAndModifyByField("name", "duck5", 
				operations -> operations.set("name", "duck5#updated"));
		
		duckRepo.updateManyByField("fixedValue", "fixedValue", op -> op.set("fixedValue", "newFixedValue"));
		
	}
	
}
