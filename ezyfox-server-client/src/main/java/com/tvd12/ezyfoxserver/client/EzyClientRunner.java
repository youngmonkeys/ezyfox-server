/**
 * 
 */
package com.tvd12.ezyfoxserver.client;

import com.tvd12.ezyfoxserver.codec.MsgPackCodecCreator;

import lombok.AllArgsConstructor;

/**
 * @author tavandung12
 *
 */
@AllArgsConstructor
public class EzyClientRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 2)
            System.err.println(getErrorMessage());
        start(args[0], Integer.parseInt(args[1]));
    }
    
    private static void start(String host, int port) {
    	try {
    		tryStart(host, port);
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private static void tryStart(String host, int port) throws Exception {
    	EzyClientBoostrap.builder()
    		.client(new EzyClient())
    		.codecCreator(new MsgPackCodecCreator())
    		.host(host)
    		.port(port)
    		.build().start();
    }
    
    private static String getErrorMessage() {
    	return "Usage: " + EzyClientRunner.class.getSimpleName() + " <host> <port>";
    }
    
}
