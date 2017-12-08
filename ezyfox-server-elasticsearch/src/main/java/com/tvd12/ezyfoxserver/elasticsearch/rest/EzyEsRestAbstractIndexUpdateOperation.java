package com.tvd12.ezyfoxserver.elasticsearch.rest;

import org.apache.http.Header;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.elasticsearch.EzyIndexedDataClasses;
import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexUpdateOperation;
import com.tvd12.ezyfoxserver.identifier.EzyIdFetchers;

import lombok.Setter;

@Setter
public abstract class EzyEsRestAbstractIndexUpdateOperation
		extends EzyEsRestAbstractOperation
		implements EzyEsIndexUpdateOperation {

	protected Header[] headers;
	protected EzyMarshaller marshaller;
	protected EzyIdFetchers idFetchers;
	protected EzyIndexedDataClasses indexedDataClasses;
	
}
