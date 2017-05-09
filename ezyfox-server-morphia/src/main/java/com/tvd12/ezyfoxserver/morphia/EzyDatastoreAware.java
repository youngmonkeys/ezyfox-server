package com.tvd12.ezyfoxserver.morphia;

import org.mongodb.morphia.Datastore;

public interface EzyDatastoreAware {

	void setDatastore(Datastore datastore);
	
}
