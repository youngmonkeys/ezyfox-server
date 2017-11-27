package com.tvd12.ezyfoxserver.rabbitmq.testing.tutorial;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.SerializationUtils;


/**
 * The producer endpoint that writes to the queue.
 *
 */
public class Producer extends EndPoint{
	
	public Producer(String endPointName) throws IOException, TimeoutException{
		super(endPointName);
	}

	public void sendMessage(Serializable object) throws IOException {
	    channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
	}	
}
