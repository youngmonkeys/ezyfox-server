package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.io.EzyMath;

public final class MsgPackConstant {

	private MsgPackConstant() {
	}
	
	public static final long MAX_POSITIVE_FIXINT 	= EzyMath.bin2int(7);
	public static final long MAX_UINT8 				= EzyMath.bin2int(8);
	public static final long MAX_UINT16 				= EzyMath.bin2int(16);
	public static final long MAX_UINT32 				= EzyMath.bin2int(32);
	
	public static final long MIN_NEGATIVE_FIXINT 	= -EzyMath.bin2int(5);
	public static final long MIN_INT8 				= -EzyMath.bin2int(7);
	public static final long MIN_INT16 				= -EzyMath.bin2int(15);
	public static final long MIN_INT32 				= -EzyMath.bin2int(31);
	
	public static final int MAX_FIXMAP_SIZE 			= EzyMath.bin2int(4);
	public static final int MAX_FIXARRAY_SIZE 		= EzyMath.bin2int(4);
	public static final int MAX_ARRAY16_SIZE 		= EzyMath.bin2int(16);
	public static final int MAX_FIXSTR_SIZE 			= EzyMath.bin2int(5);
	public static final int MAX_STR8_SIZE 			= EzyMath.bin2int(8);
	public static final int MAX_STR16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_STR32_SIZE 			= EzyMath.bin2int(31);
	public static final int MAX_MAP16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_BIN8_SIZE 			= EzyMath.bin2int(8);
	public static final int MAX_BIN16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_BIN32_SIZE 			= EzyMath.bin2int(31);
	
	public static final int MAX_SMALL_MESSAGE_SIZE	= EzyMath.bin2int(16);
	
}
