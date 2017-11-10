package com.tvd12.ezyfoxserver.binding.testing.scan2;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReaderImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractReader;
import com.tvd12.ezyfoxserver.entity.EzyObject;

@EzyReaderImpl
public class Scan2ObjectReader extends EzyAbstractReader<EzyObject, Scan2Object> {

	@Override
	public Scan2Object read(EzyUnmarshaller unmarshaller, EzyObject value) {
		Scan2Object object = new Scan2Object(); 
		return object;
	}
	
}
