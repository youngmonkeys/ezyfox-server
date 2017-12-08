package com.tvd12.ezyfoxserver.elasticsearch.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyEsSimpleResponse<M,B,O> implements EzyEsResponse<M, B, O> {

	B body;
	M metadata;
	O original;
	
	@Override
	public String toString() {
		return new StringBuilder()
				.append("<")
					.append("metatada: ").append(metadata)
					.append("body: ").append(body)
				.append(">")
				.toString();
	}
}
