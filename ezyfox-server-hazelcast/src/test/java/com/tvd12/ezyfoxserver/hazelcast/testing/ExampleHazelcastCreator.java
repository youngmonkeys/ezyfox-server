package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.core.HazelcastInstance;
import com.tvd12.ezyfoxserver.function.EzyCreation;
import com.tvd12.ezyfoxserver.hazelcast.EzyAbstractHazelcastFactory;
import com.tvd12.ezyfoxserver.hazelcast.EzySimpleHazelcastFactory;
import com.tvd12.ezyfoxserver.hazelcast.EzySimpleMapConfigsFetcher.Builder;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoresFetcher;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzySimpleMapstoresFetcher;
import com.tvd12.ezyfoxserver.hazelcast.util.EzyHazelcastConfigs;
import com.tvd12.ezyfoxserver.io.EzyMaps;

public class ExampleHazelcastCreator implements EzyCreation<HazelcastInstance> {

	private String filePath;
	
	public ExampleHazelcastCreator filePath(String filePath) {
		this.filePath = filePath;
		return this;
	}
	
	@Override
	public HazelcastInstance create() {
		EzyAbstractHazelcastFactory factory = new EzySimpleHazelcastFactory() {
			@Override
			protected void applyMapConfigs(Builder builder) {
				builder
				.mapConfigApply("example_users", mc -> {
					mc.setEvictionPolicy(EvictionPolicy.LFU);
					mc.setMaxIdleSeconds(100);
				})
				.mapConfigApplies(EzyMaps.newHashMap("example_users", mc -> {
					mc.setEvictionPolicy(EvictionPolicy.LFU);
					mc.setMaxIdleSeconds(100);
				}));
			}
			
			@Override
			protected void applyMapstoreConfigs(Builder builder) {
				super.applyMapstoreConfigs(builder);
				builder.mapstoreConfigApplies(EzyMaps.newHashMap("example_users", mc -> {
					mc.setInitialLoadMode(InitialLoadMode.EAGER);
				}));
			}
			
			@Override
			protected Set<String> normalMapNames() {
				return Sets.newHashSet("a", "b", "c");
			}
			
			@Override
			protected EzyMapstoreCreator newMapstoreCreator() {
				return new EzySimpleMapstoreCreator() {
					@Override
					public Set<String> getMapNames() {
						Set<String> set = new HashSet<>();
						set.addAll(super.getMapNames());
						set.add("a");
						set.add("b");
						set.add("c");
						return set;
					}
				};
			}
		};
		factory.setMapstoresFetcher(newMapstoresFetcher());
		return factory.newHazelcast(newConfig());
	}
	
	private Config newConfig() {
		return EzyHazelcastConfigs.newXmlConfigBuilder(filePath);
	}
	
	private EzyMapstoresFetcher newMapstoresFetcher() {
		return EzySimpleMapstoresFetcher.builder()
				.scan("com.tvd12.ezyfoxserver.hazelcast.testing.mapstore")
				.build();
	}
	
}
