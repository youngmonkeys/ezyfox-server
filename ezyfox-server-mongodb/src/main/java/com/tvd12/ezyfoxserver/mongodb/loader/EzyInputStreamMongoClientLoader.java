/**
 * 
 */
package com.tvd12.ezyfoxserver.mongodb.loader;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.tvd12.properties.file.reader.BaseFileReader;


/**
 * 
 * @author tavandung12
 *
 */
public class EzyInputStreamMongoClientLoader extends EzyPropertiesMongoClientLoader {
    
    private InputStream inputStream;
    
    public EzyInputStreamMongoClientLoader inputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }
    
    @SuppressWarnings({ "rawtypes" })
    @Override
    public EzyInputStreamMongoClientLoader properties(Map map) {
        return (EzyInputStreamMongoClientLoader) super.properties(map);
    }
    
    @Override
    public EzyInputStreamMongoClientLoader property(String name, Object value) {
        return (EzyInputStreamMongoClientLoader) super.property(name, value);
    }
    
    @Override
    protected void preload() {
        this.properties.putAll(loadInputStream());
    }
    
    private Properties loadInputStream() {
        return new BaseFileReader().loadInputStream(inputStream);
    }
    
}
