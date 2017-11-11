package com.tvd12.ezyfoxserver.client.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.tvd12.ezyfoxserver.builder.EzyBuilder;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.client.entity.EzyClientUser;
import com.tvd12.ezyfoxserver.constant.EzyConstant;

public class EzyReceiverDeterminer {
	
	protected Map<EzyConstant, Supplier<Object>> suppliers;
	
	protected EzyReceiverDeterminer(Builder builder) {
		this.suppliers = builder.newSuppliers();
	}
	
	public Object determine(EzyConstant cmd) {
		return suppliers.get(cmd).get();
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder implements EzyBuilder<EzyReceiverDeterminer> {
		
		protected Supplier<EzyClientUser> userSupplier;
		protected Supplier<EzyClientSession> sessionSupplier;
		
		public Builder userSupplier(Supplier<EzyClientUser> userSupplier) {
			this.userSupplier = userSupplier;
			return this;
		}
		
		public Builder sessionSupplier(Supplier<EzyClientSession> sessionSupplier) {
			this.sessionSupplier = sessionSupplier;
			return this;
		}
		
		@Override
		public EzyReceiverDeterminer build() {
			return new EzyReceiverDeterminer(this);
		}
		
		protected Map<EzyConstant, Supplier<Object>> newSuppliers() {
			Map<EzyConstant, Supplier<Object>> answer = new ConcurrentHashMap<>();
			answer.put(EzyClientCommand.PONG, () -> userSupplier.get());
			answer.put(EzyClientCommand.CONNECT_SUCCESS, () -> sessionSupplier.get());
			answer.put(EzyClientCommand.ERROR, () -> userSupplier.get());
			answer.put(EzyClientCommand.HANDSHAKE, () -> sessionSupplier.get());
			answer.put(EzyClientCommand.DISCONNECT, () -> userSupplier.get());
			answer.put(EzyClientCommand.LOGIN, () -> sessionSupplier.get());
			answer.put(EzyClientCommand.LOGIN_ERROR, () -> sessionSupplier.get());
			answer.put(EzyClientCommand.LOGOUT, () -> userSupplier.get());
			answer.put(EzyClientCommand.APP_ACCESS, () -> userSupplier.get());
			answer.put(EzyClientCommand.APP_JOINED, () -> userSupplier.get());
			answer.put(EzyClientCommand.APP_REQUEST, () -> userSupplier.get());
			answer.put(EzyClientCommand.APP_EXIT, () -> userSupplier.get());
			return answer;
		}
	}
	
}
