package com.tvd12.ezyfoxserver.elasticsearch.testing.data;

import com.tvd12.ezyfoxserver.annotation.EzyId;
import com.tvd12.ezyfoxserver.data.annotation.EzyIndexedData;
import com.tvd12.ezyfoxserver.elasticsearch.annotation.EzyDataIndex;
import com.tvd12.ezyfoxserver.elasticsearch.annotation.EzyDataIndexes;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EzyIndexedData
@EzyDataIndexes({
	@EzyDataIndex(index = "test1", types = {"person1", "men1", "women1"}),
	@EzyDataIndex(index = "test2", types = {"person2", "men2", "women2"})
})
public class Person1 {
	
	@EzyId
	private long id;
	
}
