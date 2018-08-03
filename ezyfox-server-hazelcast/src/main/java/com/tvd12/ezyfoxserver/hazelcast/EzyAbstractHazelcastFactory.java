package com.tvd12.ezyfoxserver.hazelcast;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapStoreFactory;
import com.tvd12.ezyfoxserver.hazelcast.constant.EzyMapNames;
import com.tvd12.ezyfoxserver.hazelcast.factory.EzySimpleMapstoreFactory;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoreCreator;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoresFetcher;
import com.tvd12.ezyfoxserver.hazelcast.mapstore.EzyMapstoresFetcherAware;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@Setter
@SuppressWarnings("rawtypes")
public abstract class EzyAbstractHazelcastFactory 
		extends EzyLoggable 
		implements EzyHazelcastFactory, EzyMapstoresFetcherAware {
	
	protected EzyMapstoresFetcher mapstoresFetcher;
	
	@Override
	public HazelcastInstance newHazelcast(Config config) {
		customizeConfig(config);
		addMapConfigs(config, getMapConfigs());
		return Hazelcast.newHazelcastInstance(config);
	}
	
	protected void customizeConfig(Config config) {
	    	EzyConfigCustomizer customizer = getConfigCustomizer();
	    	customizer.customize(config);
    }
    
    protected void addMapConfigs(Config config, Iterable<MapConfig> mapConfigs) {
	    	Iterator<MapConfig> iterator = mapConfigs.iterator();
	    	while(iterator.hasNext())
	    		config.addMapConfig(iterator.next());
    }
	
	protected Iterable<MapConfig> getMapConfigs() {
    		EzyMapConfigsFetcher fetcher = getMapConfigsFetcher();
    		return fetcher.get();
    }
    
    protected EzyConfigCustomizer getConfigCustomizer() {
    		return newConfigCustomizer();
    }
    protected EzyMapConfigsFetcher getMapConfigsFetcher() {
    		return newMapConfigsFetcher();
    }
    
    protected EzyConfigCustomizer newConfigCustomizer() {
		return new EzySimpleConfigCustomizer();
	}
	
	protected EzyMapConfigsFetcher newMapConfigsFetcher() {
	    EzySimpleMapConfigsFetcher.Builder builder
	            = newMapConfigsFetcherBuilder();
	    EzyMapstoreCreator mapstoreCreator = newMapstoreCreator();
	    if(mapstoreCreator instanceof EzyMapstoresFetcherAware)
	    	((EzyMapstoresFetcherAware)mapstoreCreator)
	    			.setMapstoresFetcher(mapstoresFetcher);
	    MapStoreFactory mapstoreFactory = newMapstoreFactory(mapstoreCreator);
	    builder.mapstoreFactory(mapstoreFactory);
	    Set<String> mapNames = new HashSet<>();
	    mapNames.addAll(normalMapNames());
	    mapNames.addAll(mapstoreCreator.getMapNames());
	    builder.mapNames(mapNames);
	    applyMapConfigs(builder);
	    applyMapstoreConfigs(builder);
		return builder.build();
	}
	
	protected Set<String> normalMapNames() {
		return new HashSet<>();
	}
	
	protected MapStoreFactory newMapstoreFactory(EzyMapstoreCreator mapstoreCreator) {
	    return new EzySimpleMapstoreFactory(mapstoreCreator);
	}
	
	protected EzySimpleMapConfigsFetcher.Builder newMapConfigsFetcherBuilder() {
		return EzySimpleMapConfigsFetcher.builder();
	}
	
	protected void applyMapConfigs(
	        EzySimpleMapConfigsFetcher.Builder builder) {
	}
	
	protected void applyMapstoreConfigs(
	        EzySimpleMapConfigsFetcher.Builder builder) {
	    builder
	    .mapAllConfigApply(mc -> {
	        mc.setBackupCount(0);
	        mc.setAsyncBackupCount(1);
	    })
	    .mapstoreAllConfigApply(mc -> {
	        mc.setWriteDelaySeconds(3);
	        mc.setWriteBatchSize(25);
	    })
	    .mapstoreConfigApply(EzyMapNames.MAX_ID, mc -> {
	        mc.setWriteDelaySeconds(2);
	    });
	}
	
	protected abstract EzyMapstoreCreator newMapstoreCreator();
	
}
