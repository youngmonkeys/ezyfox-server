/**
 * 
 */
package com.tvd12.ezyfoxserver.mongodb.loader;

import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

/**
 * @author dung.tv@zinza.com.vn
 *
 */
public class EzyPropertiesMongoClientLoader 
		extends EzyLoggable 
		implements EzyMongoClientLoader {

    protected Properties properties;
    
    public EzyPropertiesMongoClientLoader() {
        this.properties = new Properties();
    }
    
    public static MongoClient load(Properties properties) {
    	return new EzyPropertiesMongoClientLoader()
    			.properties(properties)
    			.load();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public EzyPropertiesMongoClientLoader properties(Map map) {
        this.properties.putAll(map);
        return this;
    }
    
    public EzyPropertiesMongoClientLoader property(String name, Object value) {
        this.properties.put(name, value);
        return this;
    }
    
    /* (non-Javadoc)
     * @see com.lagente.base.db.util.MongoClientLoader#load()
     */
    @Override
    public MongoClient load() {
        this.preload();
        return this.createMongoClient();
    }
    
    protected void preload() {
    }
    
    protected MongoClient createMongoClient() {
        return new MongoClient(
                new ServerAddress(getHost(), getPort()), 
                Lists.newArrayList(createCredential()));
    }
    
    protected MongoCredential createCredential() {
        return MongoCredential.createCredential(
                getUsername(), 
                getDatabase(), 
                getPassword().toCharArray());
    }

    protected String getHost() {
        return (String) properties.get(EzyMongoClientLoader.HOST);
    }
    
    protected int getPort() {
        return Integer.valueOf((String) properties.get(EzyMongoClientLoader.PORT));
    }
    
    protected String getUsername() {
        return (String) properties.get(EzyMongoClientLoader.USERNAME);
    }
    
    protected String getPassword(){
        return (String) properties.get(EzyMongoClientLoader.PASSWORD);
    }
    
    protected String getDatabase() {
        return (String) properties.get(EzyMongoClientLoader.DATABASE);
    }
    
}
