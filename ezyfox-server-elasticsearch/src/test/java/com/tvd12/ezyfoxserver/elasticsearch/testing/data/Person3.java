package com.tvd12.ezyfoxserver.elasticsearch.testing.data;

import com.tvd12.ezyfoxserver.annotation.EzyId;
import com.tvd12.ezyfoxserver.data.annotation.EzyIndexedData;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyIndexedData
public class Person3 {
	
	@EzyId
	private long id;
	
}
