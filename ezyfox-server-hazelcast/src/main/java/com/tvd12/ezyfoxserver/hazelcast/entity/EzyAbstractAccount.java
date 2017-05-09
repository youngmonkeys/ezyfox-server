package com.tvd12.ezyfoxserver.hazelcast.entity;

import java.util.Date;

import com.tvd12.ezyfoxserver.exception.EzyNegativeValueException;
import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class EzyAbstractAccount implements EzyAccount, EzyAccountTypeAware {
	private static final long serialVersionUID = -3802880651857320583L;
	
	protected Long value = 0L;
	protected String type = "";
	protected Date creationDate;
	protected Date lastUpdatedDate;
	protected boolean acceptNegativeValue;
	
	public abstract void setId(Long id);
	
	@Override
	public Long update(long value) {
		long newValue = this.value + value;
		this.checkAndSetValue(newValue);
		return newValue;
	}
	
	@Override
	public Long[] update(double percent, long initValue) {
		long oldValue = value;
		long updated = (long) (percent * value);
		long newValue = value + updated + initValue;
		this.checkAndSetValue(newValue);
		return new Long[] {updated, newValue, oldValue};
	}
	
	@Override
	public boolean equals(Object obj) {
		return new EzyEquals<EzyAbstractAccount>()
				.function(acc -> acc.getId())
				.isEquals(this, obj);
	}
	
	@Override
	public int hashCode() {
		return new EzyHashCodes()
				.append(getId())
				.toHashCode();
	}
	
	private void checkAndSetValue(long value) {
		if(value < 0 && !acceptNegativeValue)
			throw new EzyNegativeValueException(value);
		this.value = value;
	}
	
}
