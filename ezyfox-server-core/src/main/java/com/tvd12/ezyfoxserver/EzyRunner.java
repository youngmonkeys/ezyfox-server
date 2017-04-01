/**
 * 
 */
package com.tvd12.ezyfoxserver;

/**
 * @author tavandung12
 *
 */
public abstract class EzyRunner {
    
    public void run(String[] args) throws Exception {
    	validateArguments(args);
    	startSystem(args[0]);
    }
    
    protected void validateArguments(String[] args) {
    	if(args.length == 0)
    		throw new IllegalStateException("must specific config.properties file");
    }
    
    protected void startSystem(String configFile) throws Exception {
    	newStarter(configFile).start();
    }
    
    protected EzyStarter newStarter(String configFile) {
    	return newStarterBuilder().configFile(configFile).build();
    }
    
    protected abstract EzyStarter.Builder<?> newStarterBuilder();
    
}
