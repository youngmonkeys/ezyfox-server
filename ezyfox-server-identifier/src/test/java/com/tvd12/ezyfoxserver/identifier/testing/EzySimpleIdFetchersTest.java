package com.tvd12.ezyfoxserver.identifier.testing;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.Test;

import com.google.common.collect.Sets;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetcher;
import com.tvd12.ezyfoxserver.identifier.EzySimpleIdFetcherImplementer;
import com.tvd12.ezyfoxserver.identifier.EzySimpleIdFetchers;
import com.tvd12.ezyfoxserver.identifier.testing.annotation.HasIdTest;
import com.tvd12.ezyfoxserver.identifier.testing.entity.Message2;
import com.tvd12.ezyfoxserver.identifier.testing.entity.Message3;
import com.tvd12.test.base.BaseTest;

public class EzySimpleIdFetchersTest extends BaseTest {

	@Test
	public void test() {
		EzySimpleIdFetcherImplementer.setDebug(true);
		Map<Class<?>, EzyIdFetcher> idFetchers = new EzySimpleIdFetchers.Builder() {
				@SuppressWarnings("unchecked")
				protected Set<Class<? extends Annotation>> getAnnotationClasses() {
					return Sets.newHashSet(HasIdTest.class);
				};
			}
			.scan("com.tvd12.ezyfoxserver.identifier.testing.entity")
			.addIdFetcher(Message3.class, m3 -> ((Message3)m3).getId())
			.build()
			.getIdFetchers();
		EzyIdFetcher idFetcher = idFetchers.get(Message2.class);
		assert idFetcher.getId(new Message2(100L, "hello")).equals(100L);
	}
	
}
