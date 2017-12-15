package com.tvd12.ezyfoxserver.elasticsearch.rest;

import org.apache.http.Header;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;

import com.tvd12.ezyfoxserver.elasticsearch.operation.EzyEsIndexDeleteOneOperation;

import lombok.Setter;

@Setter
@SuppressWarnings({ "unchecked" })
public abstract class EzyEsRestIndexDeleteOneOperation
		extends EzyEsRestAbstractOperation
		implements EzyEsIndexDeleteOneOperation {

	protected Object id;
	protected Header[] headers;
	protected DeleteRequest deleteRequest;
	
	@Override
	public Object execute() {
		try {
			DeleteResponse deleteResponse = highLevelClient.delete(deleteRequest, headers);
			return deleteResponse.getId();
		}
		catch(Exception e) {
			throw new IllegalArgumentException("can't execute delete request: " + deleteRequest, e);
		}
	}
	
}
