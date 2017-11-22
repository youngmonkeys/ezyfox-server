package com.tvd12.ezyfoxserver.kafka.testing;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaConsumerBuilder;
import com.tvd12.ezyfoxserver.kafka.builder.EzyKafkaProducerBuilder;
import com.tvd12.ezyfoxserver.kafka.constant.EzySerializationConfig;

public final class TestUtil {

	public final static String BOOTSTRAP_SERVERS = "localhost:9092";
	
	private TestUtil() {
	}
	
	@SuppressWarnings("rawtypes")
	public static Producer newProducer() {
		return EzyKafkaProducerBuilder.producerBuilder()
				.property(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
				.property(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExampleProducer")
				.property(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleSerializer")
				.property(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleSerializer")
				.property(EzySerializationConfig.MESSAGE_SERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer")
				.property(EzySerializationConfig.MESSAGE_DESERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer")
				.build();
	}
	
	@SuppressWarnings("rawtypes")
	public static Consumer newConsumer() {
		return EzyKafkaConsumerBuilder.consumerBuilder()
				.property(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS)
				.property(ConsumerConfig.GROUP_ID_CONFIG, "KafkaExampleConsumer")
				.property(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleDeserializer")
				.property(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.tvd12.ezyfoxserver.kafka.serialization.EzySimpleDeserializer")
				.property(EzySerializationConfig.MESSAGE_SERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleSerializer")
				.property(EzySerializationConfig.MESSAGE_DESERIALIZER, "com.tvd12.ezyfoxserver.codec.MsgPackSimpleDeserializer")
				.build();
	}
}
