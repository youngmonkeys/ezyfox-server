package com.tvd12.ezyfoxserver.hazelcast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MapStoreConfig.InitialLoadMode;
import com.hazelcast.core.MapStoreFactory;
import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.function.EzyApply;
import com.tvd12.ezyfoxserver.io.EzyLists;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings("rawtypes")
public class EzySimpleMapConfigsFetcher 
        extends EzyLoggable 
        implements EzyMapConfigsFetcher {

	protected Set<String> mapNames;
	protected MapStoreFactory mapstoreFactory;
    protected EzyApply<MapConfig> mapAllConfigApply;
    protected EzyApply<MapStoreConfig> mapstoreAllConfigApply;
    protected Map<String, EzyApply<MapConfig>> mapConfigApplies;
    protected Map<String, EzyApply<MapStoreConfig>> mapstoreConfigApplies;
    
    protected EzySimpleMapConfigsFetcher(Builder builder) {
	    	this.mapNames = builder.mapNames;
	    	this.mapstoreFactory = builder.mapstoreFactory;
	    	this.mapConfigApplies = builder.mapConfigApplies;
	    	this.mapAllConfigApply = builder.mapAllConfigApply;
	    	this.mapstoreConfigApplies = builder.mapstoreConfigApplies;
	    	this.mapstoreAllConfigApply = builder.mapstoreAllConfigApply;
    }
	
	@Override
	public Iterable<MapConfig> get() {
		return EzyLists.newArrayList(
		        getMapNames(), (mn) -> newMapConfig(mn));
	}

	protected MapConfig newMapConfig(String name) {
	    	MapConfig mapCfg = new MapConfig();
	    	mapCfg.setName(name);
	    	mapCfg.setMapStoreConfig(newMapStoreConfig(name));
	    	applyCommonConfig(mapCfg);
	    	applyConfig(name, mapCfg);
	    	getLogger().debug("config map: {}", name);
	    	return mapCfg;
    }
	
	protected void applyConfig(String mapName, MapConfig config) {
	    if(mapConfigApplies.containsKey(mapName))
	        mapConfigApplies.get(mapName).apply(config);
	}
	
	protected void applyCommonConfig(MapConfig config) {
	    if(mapAllConfigApply != null)
	        mapAllConfigApply.apply(config);
	}
    
    protected MapStoreConfig newMapStoreConfig(String mapName) {
	    	MapStoreConfig config = new MapStoreConfig();
	    	config.setEnabled(true);
	    	config.setInitialLoadMode(InitialLoadMode.EAGER);
	    	applyCommonConfig(config);
	    	applyConfig(mapName, config);
	    	config.setFactoryImplementation(mapstoreFactory);
	    	return config;
    }
    
    protected void applyConfig(String mapName, MapStoreConfig config) {
    		if(mapstoreConfigApplies.containsKey(mapName))
    			mapstoreConfigApplies.get(mapName).apply(config);
    }
    
    protected void applyCommonConfig(MapStoreConfig config) {
        if(mapstoreAllConfigApply != null)
            mapstoreAllConfigApply.apply(config);
    }
    
    protected Set<String> getMapNames() {
        return mapNames;
    }
    
    public static Builder builder() {
    		return new Builder();
    }
    
    public static class Builder implements EzyBuilder<EzyMapConfigsFetcher> {
	    	protected Set<String> mapNames;
	    	protected MapStoreFactory mapstoreFactory;
	    	protected EzyApply<MapConfig> mapAllConfigApply;
	    	protected EzyApply<MapStoreConfig> mapstoreAllConfigApply;
	    	protected Map<String, EzyApply<MapConfig>> mapConfigApplies = new HashMap<>();
	    	protected Map<String, EzyApply<MapStoreConfig>> mapstoreConfigApplies = new HashMap<>();
	    	
	    	public Builder mapNames(Set<String> mapNames) {
	    		this.mapNames = mapNames;
	    		return this;
	    	}
        
        public Builder mapstoreFactory(MapStoreFactory mapStoreFactory) {
        		this.mapstoreFactory = mapStoreFactory;
        		return this;
        }
        
        public Builder mapConfigApply(
                String mapName, EzyApply<MapConfig> apply) {
            this.mapConfigApplies.put(mapName, apply);
            return this;
        }
        
        public Builder mapstoreConfigApply(
        		String mapName, EzyApply<MapStoreConfig> apply) {
	        	this.mapstoreConfigApplies.put(mapName, apply);
	        	return this;
        }
        
        public Builder mapConfigApplies(
                Map<String, EzyApply<MapConfig>> mapConfigApplies) {
            this.mapConfigApplies.putAll(mapConfigApplies);
            return this;
        }
        
        public Builder mapstoreConfigApplies(
        		Map<String, EzyApply<MapStoreConfig>> mapStoreConfigApplies) {
	        	this.mapstoreConfigApplies.putAll(mapStoreConfigApplies);
	        	return this;
        }
        
        public Builder mapAllConfigApply(EzyApply<MapConfig> mapAllConfigApply) {
            this.mapAllConfigApply = mapAllConfigApply;
            return this;
        }
        
        public Builder mapstoreAllConfigApply(EzyApply<MapStoreConfig> mapStoreAllConfigApply) {
            this.mapstoreAllConfigApply = mapStoreAllConfigApply;
            return this;
        }

		@Override
		public EzySimpleMapConfigsFetcher build() {
			return new EzySimpleMapConfigsFetcher(this);
		}
        
    }
	
}
