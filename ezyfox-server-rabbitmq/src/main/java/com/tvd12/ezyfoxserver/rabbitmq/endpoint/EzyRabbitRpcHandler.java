// Copyright (c) 2007-Present Pivotal Software, Inc.  All rights reserved.
//
// This software, the RabbitMQ Java client library, is triple-licensed under the
// Mozilla Public License 1.1 ("MPL"), the GNU General Public License version 2
// ("GPL") and the Apache License version 2 ("ASL"). For the MPL, please see
// LICENSE-MPL-RabbitMQ. For the GPL, please see LICENSE-GPL2.  For the ASL,
// please see LICENSE-APACHE2.
//
// This software is distributed on an "AS IS" basis, WITHOUT WARRANTY OF ANY KIND,
// either express or implied. See the LICENSE file for specific language governing
// rights and limitations of this software.
//
// If you have any questions regarding licensing, please contact us at
// info@rabbitmq.com.

package com.tvd12.ezyfoxserver.rabbitmq.endpoint;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.RpcServer;

@SuppressWarnings("deprecation")
public class EzyRabbitRpcHandler extends RpcServer {
	
	public EzyRabbitRpcHandler(Channel channel, String queueName) throws IOException {
		super(channel, queueName);
	}

	@Override
	public void processRequest(QueueingConsumer.Delivery request)
	        throws IOException {
	        AMQP.BasicProperties requestProperties = request.getProperties();
	        String correlationId = requestProperties.getCorrelationId();
	        String replyTo = requestProperties.getReplyTo();
	        if (correlationId != null && replyTo != null)
	        {
	            AMQP.BasicProperties.Builder replyPropertiesBuilder = new AMQP.BasicProperties.Builder();
	            byte[] replyBody = handleCall(request, replyPropertiesBuilder);
	            replyPropertiesBuilder.correlationId(correlationId);
	            AMQP.BasicProperties replyProperties = replyPropertiesBuilder.build();
	            getChannel().basicPublish("", replyTo, replyProperties, replyBody);
	        } else {
	            handleCast(request);
	        }
	}
	
	protected byte[] handleCall(
			QueueingConsumer.Delivery request, BasicProperties.Builder replyPropertiesBuilder) {
		return handleCall(request.getBody(), replyPropertiesBuilder);
	}
	
	protected byte[] handleCall(
			byte[] requestBody, BasicProperties.Builder replyPropertiesBuilder) {
		return new byte[0];
	}

}
