package com.tvd12.ezyfoxserver.elasticsearch.operation;

import org.apache.http.Header;
import org.elasticsearch.action.delete.DeleteRequest;

public interface EzyEsIndexDeleteOneOperation extends EzyEsOperation {

	void setId(Object id);
	
	void setHeaders(Header[] headers);
	
	void setDeleteRequest(DeleteRequest deleteRequest);
	
}
