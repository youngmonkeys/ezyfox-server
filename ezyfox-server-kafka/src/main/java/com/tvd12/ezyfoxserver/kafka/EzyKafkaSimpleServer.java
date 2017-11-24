package com.tvd12.ezyfoxserver.kafka;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import com.tvd12.ezyfoxserver.function.EzyApply;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessage;
import com.tvd12.ezyfoxserver.kafka.message.EzyKafkaMessageConfig;
import com.tvd12.ezyfoxserver.kafka.record.EzyKafkaConsumerRecordReader;
import com.tvd12.ezyfoxserver.util.EzyDataHandler;
import com.tvd12.ezyfoxserver.util.EzyDataHandlers;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandler;
import com.tvd12.ezyfoxserver.util.EzyExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyListDataHandlers;
import com.tvd12.ezyfoxserver.util.EzyListExceptionHandlers;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

import lombok.Setter;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzyKafkaSimpleServer
		extends EzyLoggable
		implements EzyKafkaServer, EzyKafkaConsumerAware {

	@Setter
	protected Consumer consumer;
	@Setter
	protected long poolTimeOut = 100;
	@Setter
	protected EzyApply<Consumer> commitScript;
	@Setter
	protected EzyKafkaMessageConfig messageConfig;
	@Setter
	protected EzyKafkaConsumerRecordReader recordReader;

	protected volatile boolean loop = false;
	
	protected EzyDataHandlers dataHandlers = new EzyListDataHandlers();
	protected EzyExceptionHandlers exceptionHandlers = new EzyListExceptionHandlers(); 
	
	@Override
	public void start() throws Exception {
		this.preloop();
		this.loop();
	}
	
	protected void preloop() {
		this.loop = true;
		if(this.commitScript == null)
			this.commitScript = newCommitScript();
	}
	
	protected void loop() {
		while (loop) {
			process();
		}
	}
	
	protected void process() {
		try {
			ConsumerRecords records = consumer.poll(poolTimeOut);
			if(records.isEmpty()) return;
			List<EzyKafkaMessage> messages = readRecords(records);
			dataHandlers.handleData(messages);
			commitScript.apply(consumer);
		}
		catch(Exception e) {
			exceptionHandlers.handleException(Thread.currentThread(), e);
		}
	}
	
	protected List<EzyKafkaMessage> readRecords(ConsumerRecords records) {
		List<EzyKafkaMessage> messages = new ArrayList<>();
		records.forEach(record -> messages.add(readRecord((ConsumerRecord) record)));
		return messages;
	}
	
	protected EzyKafkaMessage readRecord(ConsumerRecord record) {
		return recordReader.read(record, messageConfig);
	}

	@Override
	public void shutdown() {
		loop = false;
		consumer.close();
	}

	@Override
	public void addDataHandler(EzyDataHandler dataHandler) {
		dataHandlers.addDataHandler(dataHandler);
	}

	@Override
	public void addExceptionHandler(EzyExceptionHandler exceptionHandler) {
		exceptionHandlers.addExceptionHandler(exceptionHandler);
	}
	
	private EzyApply<Consumer> newCommitScript() {
		return consumer -> consumer.commitAsync();
	}
	
}
