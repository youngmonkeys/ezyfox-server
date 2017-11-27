package com.tvd12.ezyfoxserver.hazelcast.entity;

import java.io.Serializable;
import java.util.Date;

import com.tvd12.ezyfoxserver.util.EzyHasIdEntity;

public interface EzyAccount extends EzyHasIdEntity<Long>, Serializable {

	String getType();
	
	Long getValue();
	
	Long update(long value);
	
	Long[] update(double percent, long initValue);
	
	boolean isAcceptNegativeValue();
	
	Date getCreationDate();
	
	Date getLastUpdatedDate();
	
	void setLastUpdatedDate(Date date);
	
}
