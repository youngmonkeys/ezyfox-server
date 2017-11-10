package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.constant.EzyConstant;

import lombok.Getter;

public enum MsgPackType implements EzyConstant {

	POSITIVE_FIXINT(0),
	FIXMAP(1),
	FIXARRAY(2),
	FIXSTR(3),
	NIL(4),
	NEVER_USED(5),
	FALSE(6),
	TRUE(7),
	BIN8(8),
	BIN16(9),
	BIN32(10),
	EXT8(11),
	EXT16(12),
	EXT32(13),
	FLOAT32(14),
	FLOAT64(15),
	UINT8(16),
	UINT16(17),
	UINT32(18),
	UINT64(19),
	INT8(20),
	INT16(21),
	INT32(22),
	INT64(23),
	FIXEXT1(24),
	FIXEXT2(25),
	FIXEXT4(26),
	FIXEXT8(27),
	FIXEXT16(28),
	STR8(29),
	STR16(30),
	STR32(31),
	ARRAY16(32),
	ARRAY32(33),
	MAP16(34),
	MAP32(35),
	NEGATIVE_FIXINT(36);
	
	@Getter
	private final int id;
	
	private MsgPackType(final int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return toString();
	}

}
