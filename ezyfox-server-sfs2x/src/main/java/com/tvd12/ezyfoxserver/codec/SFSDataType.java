package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

public enum SFSDataType implements EzyConstant {

	NULL(0),
	BOOL(1),
	BYTE(2),
	SHORT(3),
	INT(4),
	LONG(5),
	FLOAT(6),
	DOUBLE(7),
	STRING(8),
	BOOL_ARRAY(9),
	BYTE_ARRAY(10),
	SHORT_ARRAY(11),
	INT_ARRAY(12),
	LONG_ARRAY(13),
	FLOAT_ARRAY(14),
	DOUBLE_ARRAY(15),
	STRING_ARRAY(16),
	SFS_ARRAY(17),
	SFS_OBJECT(18),
	CLASS(19),
	TEXT(20);
	
	@Getter
	private final int id;
	
	private SFSDataType(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}
	
	public static SFSDataType valueOf(int id) {
		for(SFSDataType type : values())
			if(type.getId() == id)
				return type;
		throw new IllegalArgumentException("has no type with id = " + id);
	}
	
}
