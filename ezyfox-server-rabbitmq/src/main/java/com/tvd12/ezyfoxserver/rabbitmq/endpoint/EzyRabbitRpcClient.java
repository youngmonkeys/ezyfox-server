package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.RpcClient.Response;
import com.rabbitmq.client.ShutdownSignalException;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRpcClient;
import com.tvd12.ezyfoxserver.rabbitmq.codec.EzyRpcProcedureSerializer;
import com.tvd12.ezyfoxserver.rabbitmq.codec.EzyRpcSimpleProcedureSerializer;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcHeaders;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcResponseEntity;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcSimpleHeaders;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcSimpleResponseEntity;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcValueProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.factory.EzyCorrelationIdFactory;
import com.tvd12.ezyfoxserver.rabbitmq.factory.EzySimpleCorrelationIdFactory;

public class EzyRabbitRpcClient 
		extends EzyRabbitEndpoint implements EzyRpcClient {

	protected int timeout;
	protected String exchange;
	protected String routingKey;
	protected String replyQueue;
	
	protected EzyCorrelationIdFactory correlationIdFactory;
	protected EzyRpcProcedureSerializer procedureSerializer;
	
	protected EzyRabbitRpcCaller client;
	
	protected EzyRabbitRpcClient(Builder builder) {
		super(builder);
		this.timeout = builder.timeout;
		this.exchange = builder.exchange;
		this.replyQueue = builder.replyQueue;
		this.routingKey = builder.routingKey;
		this.procedureSerializer = builder.getProcedureSeriazlier();
		this.correlationIdFactory = builder.getCorrelationIdFactory();
	}
	
	@Override
	public void start() throws Exception {
		this.client = newClient();
	}
	
	protected EzyRabbitRpcCaller newClient() throws IOException {
		return new EzyRabbitRpcCaller(channel, exchange, routingKey, replyQueue, timeout);
	}
	
	@Override
	public EzyRpcResponseEntity sync(EzyRpcProcedure procedure) {
		try {
			return call(procedure);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public EzyRpcResponseEntity call(EzyRpcProcedure procedure) 
			throws IOException, ShutdownSignalException, TimeoutException {
        return call(procedure, newCorrelationId());
    }
	
	protected EzyRpcResponseEntity call(EzyRpcProcedure procedure, String corrId) 
			throws IOException, ShutdownSignalException, TimeoutException {
		byte[] body = serializeProcedure(procedure);
		BasicProperties props = newReplyProperties(corrId);
		Response response = client.doCall(props, body);
		Object responseBody = deserializeResult(response.getBody(), getReturnType(procedure));
		EzyRpcHeaders responseHeader = newResponseHeaders(response.getProperties().getHeaders());
		return newResponseEntity(responseHeader, responseBody);
	}
	
	protected EzyRpcResponseEntity newResponseEntity(EzyRpcHeaders headers, Object body) {
		return EzyRpcSimpleResponseEntity.builder().headers(headers).body(body).build();
	}
	
	@SuppressWarnings("rawtypes")
	protected EzyRpcHeaders newResponseHeaders(Map headers) {
		return EzyRpcSimpleHeaders.builder().putAll(headers).build();
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getReturnType(EzyRpcProcedure procedure) {
		if(!(procedure instanceof EzyRpcValueProcedure))
			return null;
		return ((EzyRpcValueProcedure) procedure).getReturnType();
	}
	
	@SuppressWarnings("rawtypes")
	protected <T> T deserializeResult(byte[] result, Class type) {
		return deserializeToObject(result);
	}
	
	protected byte[] serializeProcedure(EzyRpcProcedure procedure) {
		return procedureSerializer.serialize(procedure);
	}
	
	protected BasicProperties newReplyProperties(String correlationId) {
		return new BasicProperties.Builder()
                .correlationId(correlationId)
                .replyTo(replyQueue)
                .build();
	}
	
	protected String newCorrelationId() {
		return correlationIdFactory.newCorrelationId();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyRabbitEndpoint.Builder<Builder> {
		protected int timeout = 3000;
		protected String replyQueue = "hello-queue";
		protected String routingKey = "hola";
		protected String exchange = "hello-exchange";
		
		protected EzyCorrelationIdFactory correlationIdFactory;
		protected EzyRpcProcedureSerializer procedureSerializer;
		
		public Builder timeout(int timeout) {
			this.timeout = timeout;
			return this;
		}
		
		public Builder replyQueue(String replyQueue) {
			this.replyQueue = replyQueue;
			return this;
		}
		
		public Builder exchange(String exchange) {
			this.exchange = exchange;
			return this;
		}
		
		public Builder routingKey(String routingKey) {
			this.routingKey = routingKey;
			return this;
		}
		
		@Override
		public EzyRabbitRpcClient build() {
			return new EzyRabbitRpcClient(this);
		}
		
		protected String getReplyQueue() {
			try {
				return channel.queueDeclare().getQueue();
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}
		
		protected EzyCorrelationIdFactory getCorrelationIdFactory() {
			if(correlationIdFactory != null)
				return correlationIdFactory;
			return new EzySimpleCorrelationIdFactory();
		}
		
		protected EzyRpcProcedureSerializer getProcedureSeriazlier() {
			if(procedureSerializer != null)
				return procedureSerializer;
			return new EzyRpcSimpleProcedureSerializer(messageSerializer);
		}
		
	}
}
