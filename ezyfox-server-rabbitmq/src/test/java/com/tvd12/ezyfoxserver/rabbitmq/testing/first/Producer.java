package com.tvd12.ezyfoxserver.rabbitmq.testing.first;

import java.io.IOException;
import java.io.Serializable;


/**
 * The producer endpoint that writes to the queue.
 * @author syntx
 *
 */
public class Producer extends EndPoint{
	
	public Producer(String endPointName) throws Exception{
		super(endPointName);
	}

	public void sendMessage(Serializable object) throws IOException {
	    channel.basicPublish("",endPointName, null, object.toString().getBytes());
	}	
}