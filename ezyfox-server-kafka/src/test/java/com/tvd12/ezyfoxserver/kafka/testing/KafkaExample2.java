package com.tvd12.ezyfoxserver.kafka.testing;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaConsumerBuilder;
import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaProducerBuilder;
import com.tvd12.ezyfoxserver.kafka.constant.EzySerializationConfig;

public class KafkaExample2 {

	private final static String TOPIC = "my-example-topic";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

	public static void main(String... args) throws Exception {
		runProducer(5);
//		runConsumer();
	}

	@SuppressWarnings("unchecked")
	private static Producer<Long, String> createProducer() {
		return EzyKafkaProducerBuilder.producerBuilder()
			.property(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
			.property(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer")
			.property(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleSerializer")
			.property(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleSerializer")
			.property(EzySerializationConfig.MESSAGE_SERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer")
			.property(EzySerializationConfig.MESSAGE_DESERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer")
			.build();
	}

	@SuppressWarnings("unchecked")
	private static Consumer<Long, String> createConsumer() {
		Consumer<Long, String> consumer = EzyKafkaConsumerBuilder.consumerBuilder()
				.property(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
				.property(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer")
				.property(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleDeserializer")
				.property(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleDeserializer")
				.property(EzySerializationConfig.MESSAGE_SERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer")
				.property(EzySerializationConfig.MESSAGE_DESERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer")
				.build();
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}

	// Async
	static void runProducerAsync(final int sendMessageCount) throws InterruptedException {
		final Producer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();
		final CountDownLatch countDownLatch = new CountDownLatch(sendMessageCount);

		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, "Hello Mom " + index);
				producer.send(record, (metadata, exception) -> {
					long elapsedTime = System.currentTimeMillis() - time;
					if (metadata != null) {
						System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
								record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);
					} else {
						exception.printStackTrace();
					}
					countDownLatch.countDown();
				});
			}
			countDownLatch.await(25, TimeUnit.SECONDS);
		} finally {
			producer.flush();
			producer.close();
		}
	}

	static void runProducer(final int sendMessageCount) throws Exception {
		final Producer<Long, String> producer = createProducer();
		long time = System.currentTimeMillis();

		try {
			for (long index = time; index < time + sendMessageCount; index++) {
				final ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC, index, "Hello Mom " + index);

				RecordMetadata metadata = producer.send(record).get();

				long elapsedTime = System.currentTimeMillis() - time;
				System.out.printf("sent record(key=%s value=%s) " + "meta(partition=%d, offset=%d) time=%d\n",
						record.key(), record.value(), metadata.partition(), metadata.offset(), elapsedTime);

			}
		} finally {
			producer.flush();
			producer.close();
		}
	}

	static void runConsumer() throws InterruptedException {
		Consumer<Long, String> consumer = createConsumer();

		while (true) {
			final ConsumerRecords<Long, String> consumerRecords = consumer.poll(100);

			if (consumerRecords.count() == -1) {
				break;
			}

			consumerRecords.forEach(record -> {
				System.out.println(
						"Got Record: (" + record.key() + ", " + record.value() + ") at offset " + record.offset());
			});
			consumer.commitAsync();
		}
		consumer.close();
		System.out.println("DONE");
	}

}
