package com.tvd12.ezyfoxserver.kafka.testing;

import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class KafkaExample3 {

	private final static String TOPIC = "my-example-topic";

	public static void main(String... args) throws Exception {
		runProducer(5);
		runConsumer();
	}

	@SuppressWarnings("unchecked")
	private static Producer<Long, String> createProducer() {
		Producer<Long, String> producer = TestUtil.newProducer();
		return producer;
	}

	@SuppressWarnings("unchecked")
	private static Consumer<Long, String> createConsumer() {
		Consumer<Long, String> consumer = TestUtil.newConsumer();
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
