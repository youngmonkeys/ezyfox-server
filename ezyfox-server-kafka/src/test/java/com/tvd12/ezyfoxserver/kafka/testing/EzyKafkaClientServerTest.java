package com.tvd12.ezyfoxserver.kafka.testing;

import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.tvd12.ezyfoxserver.binding.EzyBindingContext;
import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetchers;
import com.tvd12.ezyfoxserver.identifier.EzySimpleIdFetcherImplementer;
import com.tvd12.ezyfoxserver.kafka.EzyKafkaClient;
import com.tvd12.ezyfoxserver.kafka.EzyKafkaServer;
import com.tvd12.ezyfoxserver.kafka.EzyKafkaSimpleClient;
import com.tvd12.ezyfoxserver.kafka.EzyKafkaSimpleServer;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageBuilder;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageConfig;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageConfigBuilder;
import com.tvd12.ezyfoxserver.kafka.record.EzyKafkaSimpleConsumerRecordReader;
import com.tvd12.ezyfoxserver.kafka.record.EzyKafkaSimpleProducerRecordCreator;
import com.tvd12.ezyfoxserver.kafka.testing.entity.KafkaChatMessage;
import com.tvd12.ezyfoxserver.message.EzyMessageIdFetchers;
import com.tvd12.ezyfoxserver.util.EzyDataHandler;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;
import com.tvd12.test.base.BaseTest;

@SuppressWarnings("rawtypes")
public class EzyKafkaClientServerTest extends BaseTest {
	
	private final static String TOPIC = "my-example-topic";
	
	public static void main(String[] args) throws Exception {
		new EzyKafkaClientServerTest().run();
	}
	
	private void run() throws Exception {
		EzyKafkaClient client = newClient();
		runClient(client, 5);
		runServer();
	}
	
	private EzyKafkaServer newServer() {
		Consumer consumer = newConsumer();
		EzyBindingContext bindingContext = newBindingContext();
		EzyUnmarshaller unmarshaller = bindingContext.newUnmarshaller();
		EzyKafkaSimpleConsumerRecordReader recordReader
				= new EzyKafkaSimpleConsumerRecordReader();
		recordReader.setUnmarshaller(unmarshaller);
		EzyKafkaMessageConfig messageConfig 
				= EzyKafkaMessageConfigBuilder.messageConfigBuilder()
				.keyType(Long.class)
				.valueType(KafkaChatMessage.class)
				.build();
		EzyKafkaSimpleServer server = new EzyKafkaSimpleServer();
		server.setConsumer(consumer);
		server.setRecordReader(recordReader);
		server.setMessageConfig(messageConfig);
		server.addDataHandler(new EzyDataHandler<List<KafkaChatMessage>>() {
			@Override
			public void handleData(List<KafkaChatMessage> messages) {
				System.out.println("GREAT! We have just received message: " + messages);
			}
		});
		server.addExceptionHandler(new EzyExceptionHandler() {
			
			@Override
			public void handleException(Thread thread, Throwable throwable) {
				throwable.printStackTrace();
			}
		});
		return server;
	}
	
	private EzyKafkaClient newClient() {
		EzySimpleIdFetcherImplementer.setDebug(true);
		Producer producer = newProducer();
		EzyBindingContext bindingContext = newBindingContext();
		EzyMarshaller marshaller = bindingContext.newMarshaller();
		EzyIdFetchers messageIdFetchers = newMessageIdFetchers();
		EzyKafkaSimpleProducerRecordCreator recordCreator 
				= new EzyKafkaSimpleProducerRecordCreator();
		recordCreator.setMarshaller(marshaller);
		recordCreator.setMessageIdFetchers(messageIdFetchers);
		EzyKafkaSimpleClient client = new EzyKafkaSimpleClient();
		client.setProducer(producer);
		client.setRecordCreator(recordCreator);
		return client;
	}
	
	@SuppressWarnings("unchecked")
	private Consumer newConsumer() {
		Consumer consumer = TestUtil.newConsumer();
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}
	
	private Producer newProducer() {
		return TestUtil.newProducer();
	}

	private EzyBindingContext newBindingContext() {
		return EzyBindingContext.builder()
				.scan("com.tvd12.ezyfoxserver.kafka.testing.entity")
				.build();
	}
	
	private EzyIdFetchers newMessageIdFetchers() {
		return EzyMessageIdFetchers.builder()
				.scan("com.tvd12.ezyfoxserver.kafka.testing.entity")
				.build();
	}
	
	
	private void runServer() throws Exception {
		EzyKafkaServer server = newServer();
		server.start();
	}
	
	private void runClient(EzyKafkaClient client, int sendMessageCount) throws Exception {
		long time = System.currentTimeMillis();

		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				EzyKafkaMessage message = EzyKafkaMessageBuilder.messageBuilder()
						.topic(TOPIC)
						.value(new KafkaChatMessage(index, "Meessage#" + index))
						.build();

				RecordMetadata metadata = client.sync(message);

				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("sent record(value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
						message.getValue(), metadata.partition(), metadata.offset(), elapsedTime);

			}
		} finally {
			client.flush();
			client.shutdown();
		}
	}
	
	
}
