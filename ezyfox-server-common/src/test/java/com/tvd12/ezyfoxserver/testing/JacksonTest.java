package com.tvd12.ezyfoxserver.testing;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;

import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.builder.EzyObjectBuilder;
import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyObject;
import com.tvd12.ezyfoxserver.factory.EzyLiteEntityFactory;
import com.tvd12.test.base.BaseTest;

public class JacksonTest extends BaseTest {

	@Test
	public void test() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(getJsonInputStream());
		Object value = parse(node);
		System.out.println("answer = " + value);
	}
	
	public Object parse(JsonNode node) {
		if(node.isArray())
			return parseArray(node);
		else if(node.isObject())
			return parseObject(node);
		return node.asText();
	}
	
	public EzyArray parseArray(JsonNode node) {
		EzyArrayBuilder arrayBuilder = newArrayBuilder();
		Iterator<JsonNode> iterator = node.iterator();
		while(iterator.hasNext())
			arrayBuilder.append(iterator.next());
		return arrayBuilder.build();
	}
	
	public EzyObject parseObject(JsonNode node) {
		EzyObjectBuilder objectBuilder = newObjectBuilder();
		Iterator<Entry<String, JsonNode>> fields = node.fields();
		while(fields.hasNext()) {
			Entry<String, JsonNode> field = fields.next();
			objectBuilder.append(field.getKey(), parse(field.getValue()));
		}
		return objectBuilder.build();
	}
	
	private EzyArrayBuilder newArrayBuilder() {
		return EzyLiteEntityFactory.create(EzyArrayBuilder.class);
	}
	
	private EzyObjectBuilder newObjectBuilder() {
		return EzyLiteEntityFactory.create(EzyObjectBuilder.class);
	}
	
	private InputStream getJsonInputStream() {
		return getClass()
				.getClassLoader()
				.getResourceAsStream("json_example.json");
	}
	
}
