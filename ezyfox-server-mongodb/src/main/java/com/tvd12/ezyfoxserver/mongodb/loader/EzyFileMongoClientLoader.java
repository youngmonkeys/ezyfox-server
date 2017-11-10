/**
 * 
 */
package com.tvd12.ezyfoxserver.mongodb.loader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Properties;

import com.mongodb.MongoClient;
import com.tvd12.properties.file.reader.BaseFileReader;



/**
 * @author dung.tv@zinza.com.vn
 *
 */
public class EzyFileMongoClientLoader extends EzyPropertiesMongoClientLoader {

    private File file;
    
    public static MongoClient load(File file) {
    	return new EzyFileMongoClientLoader()
    			.file(file)
    			.load();
    }
    
    public static MongoClient load(String filePath) {
    	return load(new File(filePath));
    }
    
    public EzyFileMongoClientLoader file(File file) {
        this.file = file;
        return this;
    }
    
    public EzyFileMongoClientLoader filePath(String filePath) {
        return file(new File(filePath));
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public EzyFileMongoClientLoader properties(Map map) {
        return (EzyFileMongoClientLoader) super.properties(map);
    }
    
    @Override
    public EzyFileMongoClientLoader property(String name, Object value) {
        return (EzyFileMongoClientLoader) super.property(name, value);
    }
    
    @Override
    protected void preload() {
    	getLogger().info("load mongo client config from file: {}", file.getAbsolutePath());
        this.properties.putAll(loadInputStream());
    }
    
    private Properties loadInputStream() {
        try {
            return new BaseFileReader().loadInputStream(new FileInputStream(file));
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
    
}
