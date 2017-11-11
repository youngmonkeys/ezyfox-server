package com.tvd12.ezyfoxserver.client;

import java.util.concurrent.ExecutorService;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.codec.EzyCodecCreator;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.util.EzyLoggable;
import com.tvd12.ezyfoxserver.util.EzyProcessor;
import com.tvd12.ezyfoxserver.util.EzyStartable;

import lombok.Getter;

@Getter
public class EzyClientStarter 
		extends EzyLoggable 
		implements EzyStartable {

	protected int port;
	protected String host;
	protected boolean fromMainThread;
	protected EzyCodecCreator codecCreator;
	protected EzyClientContext clientContext;
	
	protected final ExecutorService startService;
	
	protected EzyClientStarter(Builder builder) {
		this.host = builder.host;
		this.port = builder.port;
		this.codecCreator = builder.codecCreator;
		this.clientContext = builder.clientContext;
		this.startService = EzyExecutors.newSingleThreadExecutor("client-starter");
		Runtime.getRuntime().addShutdownHook(new Thread(() -> startService.shutdown()));
	}
	
	public void start() throws Exception {
    	if(fromMainThread)
    		doStart();
    	else
    		startService.submit(() -> EzyProcessor.processWithException(this::doStart));
    }
	
	public void doStart() throws Exception {
		EzyClientBoostrap.builder()
			.host(host)
			.port(port)
			.codecCreator(codecCreator)
			.clientContext(clientContext)
			.build()
			.start();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyClientStarter> {
		protected int port;
		protected String host;
		protected EzyCodecCreator codecCreator;
		protected EzyClientContext clientContext;
		protected boolean fromMainThread = true;
		
		public Builder host(String host) {
			this.host = host;
			return this;
		}
		
		public Builder port(int port) {
			this.port = port;
			return this;
		}
		
		public Builder fromMainThread(boolean fromMainThread) {
			this.fromMainThread = fromMainThread;
			return this;
		}
		
		public Builder clientContext(EzyClientContext clientContext) {
			this.clientContext = clientContext;
			return this;
		}
		
		public Builder codecCreator(EzyCodecCreator codecCreator) {
			this.codecCreator = codecCreator;
			return this;
		}

		@Override
		public EzyClientStarter build() {
			return new EzyClientStarter(this);
		}
		
	}
	
}
