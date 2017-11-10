package com.tvd12.ezyfoxserver.testing.stream;

import java.util.Set;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.io.EzyClassesFetcher;
import com.tvd12.ezyfoxserver.stream.EzyStreamClassesFetcher;
import com.tvd12.test.base.BaseTest;

public class EzyStreamClassesFetcherTest extends BaseTest {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void test() {
		Set<Class> set = EzyStreamClassesFetcher.builder()
			.context(getClass())
			.build()
			.asSet("classes.txt");
		Class<EzyClassesFetcher> first = set.iterator().next();
		assert first.getClassLoader() == EzyClassesFetcher.class.getClassLoader();
		assert first == EzyClassesFetcher.class;
	}
	
}
