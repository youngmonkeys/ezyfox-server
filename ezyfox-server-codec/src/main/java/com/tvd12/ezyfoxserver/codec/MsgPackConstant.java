package com.tvd12.ezyfoxserver.codec;

import com.tvd12.ezyfoxserver.util.EzyMath;

public abstract class MsgPackConstant {

	private MsgPackConstant() {
	}
	
	public static final int MAX_POSITIVE_FIXINT 	= EzyMath.bin2int(7);
	public static final int MAX_FIXMAP_SIZE 		= EzyMath.bin2int(4);
	public static final int MAX_FIXARRAY_SIZE 		= EzyMath.bin2int(4);
	public static final int MAX_ARRAY16_SIZE 		= EzyMath.bin2int(16);
	public static final int MAX_ARRAY32_SIZE 		= EzyMath.bin2int(32);
	public static final int MAX_FIXSTR_SIZE 		= EzyMath.bin2int(5);
	public static final int MAX_STR8_SIZE 			= EzyMath.bin2int(8);
	public static final int MAX_STR16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_STR32_SIZE 			= EzyMath.bin2int(32);
	public static final int MAX_MAP16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_MAP32_SIZE 			= EzyMath.bin2int(32);
	public static final int MAX_BIN8_SIZE 			= EzyMath.bin2int(8);
	public static final int MAX_BIN16_SIZE 			= EzyMath.bin2int(16);
	public static final int MAX_BIN32_SIZE 			= EzyMath.bin2int(32);
}
