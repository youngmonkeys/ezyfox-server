/**
 * 
 */
package com.tvd12.ezyfoxserver;

import com.tvd12.ezyfoxserver.context.EzyServerContext;

import lombok.Getter;

/**
 * @author tavandung12
 *
 */
public abstract class EzyRunner {
    
    @Getter
    protected EzyServerContext serverContext;
    
    public void run(String[] args) throws Exception {
        validateArguments(args);
        startSystem(args);
    }
    
    protected void validateArguments(String[] args) {
	    if(args.length == 0)
	        throw new IllegalStateException("must specific config.properties file");
    }
    
    protected void startSystem(String[] args) throws Exception {
        String configFile = args.length > 0 ? args[0] : null;
        EzyStarter starter = newStarter(configFile);
        starter.start();
        serverContext = starter.getServerContext();
    }
    
    protected EzyStarter newStarter(String configFile) {
        return newStarterBuilder().configFile(configFile).build();
    }
    
    protected abstract EzyStarter.Builder<?> newStarterBuilder();
    
}
