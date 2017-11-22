package com.tvd12.ezyfoxserver.kafka.testing;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class KafkaExample {

	private final static String TOPIC = "my-example-topic";
	private final static String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";

	public static void main(String... args) throws Exception {
		runProducer(5);
		runConsumer();
	}

	private static Producer<Long, String> createProducer() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		return new KafkaProducer<>(props);
	}

	private static Consumer<Long, String> createConsumer() {
		Properties props = new Properties();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		Consumer<Long, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));
		return consumer;
	}

	// Async
	// static void runProducer(final int sendMessageCount) throws
	// InterruptedException {
	// final Producer<Long, String> producer = createProducer();
	// long time = System.currentTimeMillis();
	// final CountDownLatch countDownLatch = new
	// CountDownLatch(sendMessageCount);
	//
	// try {
	// for (long index = time; index < time + sendMessageCount; index++) {
	// final ProducerRecord<Long, String> record =
	// new ProducerRecord<>(TOPIC, index, "Hello Mom " + index);
	// producer.send(record, (metadata, exception) -> {
	// long elapsedTime = System.currentTimeMillis() - time;
	// if (metadata != null) {
	// System.out.printf("sent record(key=%s value=%s) " +
	// "meta(partition=%d, offset=%d) time=%d\n",
	// record.key(), record.value(), metadata.partition(),
	// metadata.offset(), elapsedTime);
	// } else {
	// exception.printStackTrace();
	// }
	// countDownLatch.countDown();
	// });
	// }
	// countDownLatch.await(25, TimeUnit.SECONDS);
	// }finally {
	// producer.flush();
	// producer.close();
	// }
	// }

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
