/**
 * 
 */
package com.tvd12.ezyfoxserver.client.testing;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.client.EzyClient;
import com.tvd12.ezyfoxserver.client.EzyClientStarter;
import com.tvd12.ezyfoxserver.client.cmd.EzySendRequest;
import com.tvd12.ezyfoxserver.client.constants.EzyClientCommand;
import com.tvd12.ezyfoxserver.client.context.EzyClientAppContext;
import com.tvd12.ezyfoxserver.client.context.EzyClientContext;
import com.tvd12.ezyfoxserver.client.context.EzyClientContextBuilder;
import com.tvd12.ezyfoxserver.client.controller.EzyClientAppController;
import com.tvd12.ezyfoxserver.client.controller.EzyHandShakeController;
import com.tvd12.ezyfoxserver.client.controller.EzyLoginController;
import com.tvd12.ezyfoxserver.client.entity.EzyClientSession;
import com.tvd12.ezyfoxserver.client.entity.EzySimpleClientUser;
import com.tvd12.ezyfoxserver.client.listener.EzyClientAppResponseListener;
import com.tvd12.ezyfoxserver.client.request.EzyAccessAppRequest;
import com.tvd12.ezyfoxserver.client.request.EzyLoginRequest;
import com.tvd12.ezyfoxserver.client.serialize.EzyRequestSerializer;
import com.tvd12.ezyfoxserver.client.serialize.impl.EzyRequestSerializerImpl;
import com.tvd12.ezyfoxserver.context.EzyContext;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyEntityFactory;
import com.tvd12.ezyfoxserver.netty.codec.MsgPackCodecCreator;

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
    
    private static void start(String host, int port) throws Exception {
    	EzyClientStarter.builder()
			.host(host)
			.port(port)
			.clientContext(newClientContext())
			.codecCreator(new MsgPackCodecCreator())
			.build()
			.start();
    }
    
    protected EzyRequestSerializer newRequestSerializer() {
    	return EzyRequestSerializerImpl.builder().build();
    }
    
    private static EzyClientContext newClientContext() {
    	return new EzyClientContextBuilder()
    			.setupClient(EzyClientRunner::setupClient)
    			.build();
    }
    
    private static void setupClient(EzyClient client) {
    	
    	client.addController(EzyClientCommand.HANDSHAKE, new EzyHandShakeController() {
    		@Override
    		public void handle(EzyClientContext ctx, EzyClientSession session, EzyArray data) {
    			super.handle(ctx, session, data);
    			sendLoginRequest(ctx, session);
    		}
    		
    		protected void sendLoginRequest(EzyContext ctx, EzyClientSession session) {
    			ctx.get(EzySendRequest.class)
    				.sender(session)
    				.request(newLoginRequest())
    				.execute();
    		}
    		
    		protected String getUsername() {
    			return "dungtv";
    		}
    		
    		protected String getPassword() {
    			return "123456";
    		}
    		
    		protected EzyLoginRequest newLoginRequest() {
    			return EzyLoginRequest.builder()
    					.username(getUsername())
    					.password(getPassword())
    					.data(newLoginInData())
    					.build();
    		}
    		
    		protected EzyData newLoginInData() {
    			return newArrayBuilder()
    					.append("1.0.0")
    					.append("android")
    					.build();
    		}
    	});
    	
    	client.addController(EzyClientCommand.LOGIN, new EzyLoginController() {
    		
    		@Override
    		protected void processNotReconnect(
    				EzyClientContext ctx, EzyClientSession session, EzyArray data) {
    			sendAccessAppsRequest(ctx);
    		}
    		
    		protected void sendAccessAppsRequest(EzyClientContext ctx) {
    			ctx.get(EzySendRequest.class)
    				.sender(ctx.getMe())
    				.request(newAccessAppRequest())
    				.execute();
    		}
    		
    		protected EzyAccessAppRequest newAccessAppRequest() {
    			return EzyAccessAppRequest.builder()
    					.appName("ezyfox-chat")
    					.data(newAccessAppData())
    					.build();
    		}
    		
    		protected EzyObject newAccessAppData() {
    			return newObjectBuilder().build();
    		}
    		
    	});
    	
    	client.addAppController(EzyClientCommand.ACESS_APP_SUCCESS, new EzyClientAppController<EzyArray>() {

			@Override
			public void handle(EzyClientAppContext ctx, EzySimpleClientUser rev, EzyArray data) {
				sendChatMessageRequest(ctx);
				sendPluginRequest(ctx);
			}
			
			protected void sendChatMessageRequest(EzyClientAppContext appCtx) {
				EzyArray params = EzyEntityFactory.create(EzyArrayBuilder.class).append("nice to meet you!").build();
				for(int i = 0 ; i < 1000 ; i++)
					appCtx.sendRequest("1", params);
			}
			
			protected void sendPluginRequest(EzyClientAppContext ctx) {
				ctx.sendPluginRequest("ezyfox-auth-plugin", newPluginRequestData());
			}
			
			protected EzyArray newPluginRequestData() {
				return EzyEntityFactory.create(EzyArrayBuilder.class).append(1).append("hello world").build();
			}
    		
		});
    	
    	client.addClientAppResponseListener("1", new EzyClientAppResponseListener<EzyArray>() {
    		@Override
    		public void execute(EzyClientAppContext ctx, EzyArray params) {
    			System.out.println("\n\nparams = " + params + "\n\n");
    		}
		});
    }
    private static String getErrorMessage() {
    	return "Usage: " + EzyClientRunner.class.getSimpleName() + " <host> <port>";
    }
    
}
