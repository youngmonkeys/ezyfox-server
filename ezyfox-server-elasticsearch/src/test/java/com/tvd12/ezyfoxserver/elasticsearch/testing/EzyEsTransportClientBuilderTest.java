package com.tvd12.ezyfoxserver.elasticsearch.testing;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

public class EzyEsTransportClientBuilderTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Settings settings = Settings.builder()
		        .put("cluster.name", "elasticsearch").build();

		TransportClient transportClient = new PreBuiltTransportClient(settings)
		        .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
		
		XContentBuilder createIndexSource = jsonBuilder()
				.startObject()
					.startObject("settings")
						.field("number_of_shards", 1)
					.endObject()
					.startObject("mappings")
						.startObject("one")
							.startObject("properties")
								.startObject("foo")
									.field("type", "text")
								.endObject()
								.startObject("bar")
									.field("type", "text")
								.endObject()
							.endObject()
						.endObject()
					.endObject()
				.endObject();
		
		IndicesAdminClient indices = transportClient.admin().indices();
		
		try {
			indices.prepareDelete("study");
		}
		catch(Exception e) {
		}
		
		try {
			CreateIndexResponse createIndexResponse = indices.prepareCreate("study")
				.setSource(createIndexSource)
				.get();
			System.out.println(createIndexResponse);
		}
		catch(Exception e) {
		}
		
//		IndexResponse indexResponse = transportClient.prepareIndex()
//				.setIndex("study")
//				.setType("one")
//				.setSource(indexRequestSource)
//				.get();
//		System.out.println(indexResponse);
	}
	
	@SuppressWarnings("unused")
	private static SearchRequest newSearchRequest(String keyword) {
		SearchRequest searchRequest = new SearchRequest("study");
		searchRequest.types("one");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
		sourceBuilder.query(QueryBuilders.termQuery("sentence.word", "hello")); 
		sourceBuilder.from(0); 
		sourceBuilder.size(5); 
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS)); 
		searchRequest.source(sourceBuilder);
		return searchRequest;
	}
	
}
