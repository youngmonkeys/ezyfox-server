package com.tvd12.ezyfoxserver.kafka.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EzyKafkaSimpleMessageConfig implements EzyKafkaMessageConfig {

	protected Class<?> keyType;
	protected Class<?> valueType;
	
}
