package com.tvd12.ezyfoxserver.elasticsearch.operation;

import org.apache.http.Header;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexedDataClasses;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetchersAware;

public interface EzyEsIndexUpdateOperation extends EzyEsOperation, EzyIdFetchersAware {
	
	void setHeaders(Header[] headers);
	
	void setMarshaller(EzyMarshaller marshaller);
	
	void setIndexedDataClasses(EzyIndexedDataClasses indexedDataClasses);
	
}
