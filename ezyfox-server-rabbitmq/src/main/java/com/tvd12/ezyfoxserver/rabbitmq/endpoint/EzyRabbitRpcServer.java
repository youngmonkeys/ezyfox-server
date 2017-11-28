package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.tvd12.ezyfoxserver.rabbitmq.EzyRpcProcedureCaller;
import com.tvd12.ezyfoxserver.rabbitmq.codec.EzyRpcProcedureDeserializer;
import com.tvd12.ezyfoxserver.rabbitmq.codec.EzyRpcSimpleProcedureDeserializer;
import com.tvd12.ezyfoxserver.rabbitmq.entity.EzyRpcProcedure;
import com.tvd12.ezyfoxserver.rabbitmq.exception.EzyRpcException;

public class EzyRabbitRpcServer extends EzyRabbitEndpoint {

	protected EzyRabbitRpcHandler server;
	protected Map<String, Object> queueArguments;
	protected EzyRpcProcedureCaller procedureCaller;
	protected EzyRpcProcedureDeserializer procedureDeserializer;
	
	protected EzyRabbitRpcServer(Builder builder) {
		super(builder);
		this.queueArguments = builder.queueArguments;
		this.procedureCaller = builder.procedureCaller;
		this.procedureDeserializer = builder.newProcedureDeserializer();
	}
	
	@Override
	public void start() throws Exception {
		getLogger().info("start server queue = {}", queue);
		server = newServer();
		server.mainloop();
	}
	
	protected EzyRabbitRpcHandler newServer() throws IOException {
		return new EzyRabbitRpcHandler(channel, queue) {
			public byte[] handleCall(byte[] requestBody, 
					BasicProperties.Builder replyPropertiesBuilder) {
				return doHandleCall(requestBody, replyPropertiesBuilder);
			}
		};
	}
	
	protected byte[] doHandleCall(byte[] requestBody, 
			BasicProperties.Builder replyPropertiesBuilder) {
		int code = 200;
		String message = "success";
		byte[] responseBody = null;
		try {
			EzyRpcProcedure procedure = getProcedure(requestBody);
			Object answer = executeProcedure(procedure);
			responseBody = serializeToBytes(answer);
		}
		catch (EzyRpcException e) {
			code = e.getCode();
			message = e.getMessage();
		}
		Map<String, Object> headers = new HashMap<>();
		headers.put("code", code);
		headers.put("msg", message);
		replyPropertiesBuilder.headers(headers);
		return responseBody;
	}
	
	protected Object executeProcedure(EzyRpcProcedure procedure) 
			throws EzyRpcException {
		return procedureCaller.call(procedure);
	}
	
	protected EzyRpcProcedure getProcedure(byte[] body) {
		return procedureDeserializer.deserialize(body);
	}
	
	protected BasicProperties newReplyProps(BasicProperties rev) {
		return new BasicProperties.Builder()
	        .correlationId(rev.getCorrelationId())
	        .build();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder extends EzyRabbitEndpoint.Builder<Builder> {
		protected Map<String, Object> queueArguments;
		protected EzyRpcProcedureCaller procedureCaller;
		protected EzyRpcProcedureDeserializer procedureDeserializer;
		
		public Builder queueArguments(Map<String, Object> queueArguments) {
			this.queueArguments = queueArguments;
			return this;
		}
		
		public Builder procedureCaller(EzyRpcProcedureCaller procedureCaller) {
			this.procedureCaller = procedureCaller;
			return this;
		}
		
		@Override
		public EzyRabbitRpcServer build() {
			return new EzyRabbitRpcServer(this);
		}

		protected Map<String, Object> getQueueArguments() {
			return queueArguments != null ? queueArguments : new HashMap<>();
		}
		
		protected EzyRpcProcedureDeserializer newProcedureDeserializer() {
			return new EzyRpcSimpleProcedureDeserializer(messageDeserializer);
		}
	}
	
}
