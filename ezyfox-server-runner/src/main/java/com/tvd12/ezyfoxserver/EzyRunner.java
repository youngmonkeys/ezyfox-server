/**
 * 
 */
package com.tvd12.ezyfoxserver;

/**
 * @author tavandung12
 *
 */
public class EzyRunner {
    
    public static void main(final String[] args) throws Exception {
    	validateArguments(args);
    	startSystem(args[0]);
    }
    
    private static void validateArguments(final String[] args) {
    	if(args.length == 0)
    		throw new IllegalStateException("must specific config.properties file");
    }
    
    private static void startSystem(final String configFile) throws Exception {
    	newStarter(configFile).start();
    }
    
    private static EzyStarter newStarter(final String configFile) {
    	return newStarterBuilder().configFile(configFile).build();
    }
    
    private static EzyStarter.Builder newStarterBuilder() {
    	return new EzyStarter.Builder();
    }
    
}
