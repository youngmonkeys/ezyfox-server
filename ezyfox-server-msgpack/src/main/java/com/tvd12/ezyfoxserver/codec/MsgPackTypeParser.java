package com.tvd12.ezyfoxserver.codec;

public class MsgPackTypeParser {

	public MsgPackType parse(int type) {
		if (0x00 <= type && type <= 0x7f)
			return MsgPackType.POSITIVE_FIXINT;
		if (0x80 <= type && type <= 0x8f)
			return MsgPackType.FIXMAP;
		if (0x90 <= type && type <= 0x9f)
			return MsgPackType.FIXARRAY;
		if (0xa0 <= type && type <= 0xbf)
			return MsgPackType.FIXSTR;
		if (type == 0xc0)
			return MsgPackType.NIL;
		if(type == 0xc1)
			return MsgPackType.NEVER_USED;
		if(type == 0xc2)
			return MsgPackType.FALSE;
		if(type == 0xc3)
			return MsgPackType.TRUE;
		if(type == 0xc4)
			return MsgPackType.BIN8;
		if(type == 0xc5)
			return MsgPackType.BIN16;
		if(type == 0xc6)
			return MsgPackType.BIN32;
		if(type == 0xc7)
			return MsgPackType.EXT8;
		if(type == 0xc8)
			return MsgPackType.EXT16;
		if(type == 0xc9)
			return MsgPackType.EXT32;
		if(type == 0xca)
			return MsgPackType.FLOAT32;
		if(type == 0xcb)
			return MsgPackType.FLOAT64;
		if(type == 0xcc)
			return MsgPackType.UINT8;
		if(type == 0xcd)
			return MsgPackType.UINT16;
		if(type == 0xce)
			return MsgPackType.UINT32;
		if(type == 0xcf)
			return MsgPackType.UINT64;
		if(type == 0xd0)
			return MsgPackType.INT8;
		if(type == 0xd1)
			return MsgPackType.INT16;
		if(type == 0xd2)
			return MsgPackType.INT32;
		if(type == 0xd3)
			return MsgPackType.INT64;
		if(type == 0xd4)
			return MsgPackType.FIXEXT1;
		if(type == 0xd5)
			return MsgPackType.FIXEXT2;
		if(type == 0xd6)
			return MsgPackType.FIXEXT4;
		if(type == 0xd7)
			return MsgPackType.FIXEXT8;
		if(type == 0xd8)
			return MsgPackType.FIXEXT16;
		if(type == 0xd9)
			return MsgPackType.STR8;
		if(type == 0xda)
			return MsgPackType.STR16;
		if(type == 0xdb)
			return MsgPackType.STR32;
		if(type == 0xdc)
			return MsgPackType.ARRAY16;
		if(type == 0xdd)
			return MsgPackType.ARRAY32;
		if(type == 0xde)
			return MsgPackType.MAP16;
		if(type == 0xdf)
			return MsgPackType.MAP32;
		if (0xe0 <= type && type <= 0xff)
			return MsgPackType.NEGATIVE_FIXINT;
		
		throw new IllegalArgumentException("has no type with type = " + String.format("%x", type));
	}
	
}
