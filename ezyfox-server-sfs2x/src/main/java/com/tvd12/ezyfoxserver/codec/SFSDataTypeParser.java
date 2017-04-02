package com.tvd12.ezyfoxserver.codec;

public class SFSDataTypeParser {

	public SFSDataType parse(int typeId) {
		return SFSDataType.valueOf(typeId);
	}
	
}
