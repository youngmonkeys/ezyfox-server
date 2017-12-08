package com.tvd12.ezyfoxserver.elasticsearch;

import java.io.Serializable;

import com.tvd12.ezyfoxserver.util.EzyEquals;
import com.tvd12.ezyfoxserver.util.EzyHashCodes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EzyIndexType implements Serializable {
	private static final long serialVersionUID = 1424057194255086651L;
	
	protected String index;
	protected String type;
	
	@Override
	public boolean equals(Object obj) {
		return new EzyEquals<EzyIndexType>()
				.function(o -> o.index)
				.function(o -> o.type)
				.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return new EzyHashCodes()
				.append(index, type)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return index + "/" + index;
	}
}
