package com.tvd12.ezyfoxserver.codec;

public class EzyMessageHeaderReader {
	
	protected boolean readBigSize(byte header) {
		return (header & 1 << 0) != 0;
	}
	
	protected boolean readEncrypted(byte header) {
		return (header & (1 << 1)) != 0;
	}
	
	protected boolean readCompressed(byte header) {
		return (header & (1 << 2)) != 0;
	}
	
	protected boolean readText(byte header) {
		return (header & (1 << 3)) != 0;
	}
	
	public EzyMessageHeader read(byte header) {
		return EzyMessageHeaderBuilder.newInstance()
				.bigSize(readBigSize(header))
				.encrypted(readEncrypted(header))
				.compressed(readCompressed(header))
				.text(readText(header))
				.build();
	}
	
}