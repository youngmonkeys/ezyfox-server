package com.tvd12.ezyfoxserver.client.context;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.client.serialize.impl.EzyRequestSerializerImpl;
import com.tvd12.ezyfoxserver.concurrent.EzyExecutors;
import com.tvd12.ezyfoxserver.function.EzyApply;

public class EzyClientContextBuilder implements EzyBuilder<EzyClientContext> {

	protected final EzyClient client;
	
	public EzyClientContextBuilder() {
		this.client = newClient();
	}
	
	public static EzyClientContextBuilder newInstance() {
		return new EzyClientContextBuilder();
	}
	
	protected EzyClient newClient() {
		return new EzyClient();
	}
	
	public EzyClientContextBuilder setupClient(EzyApply<EzyClient> applier) {
		applier.apply(client);
		return this;
	}
	
	@Override
	public EzyClientContext build() {
		EzySimpleClientContext context = newProduct();
		context.setClient(client);
		context.setProperty(EzyRequestSerializer.class,	newRequestSerializer());
		context.setWorkerExecutor(EzyExecutors.newFixedThreadPool(16, "client-worker"));
		return context;
	}
	
	protected EzySimpleClientContext newProduct() {
		return new EzySimpleClientContext();
	}
	
	 protected EzyRequestSerializer newRequestSerializer() {
		 return EzyRequestSerializerImpl.builder().build();
	 }
	
}
