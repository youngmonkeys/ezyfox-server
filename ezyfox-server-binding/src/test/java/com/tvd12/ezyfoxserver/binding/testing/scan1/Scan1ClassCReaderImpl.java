package com.tvd12.ezyfoxserver.binding.testing.scan1;

import com.tvd12.ezyfoxserver.binding.EzyMarshaller;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReaderImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractReader;
import com.tvd12.ezyfoxserver.entity.EzyArray;

@EzyReaderImpl
public class Scan1ClassCReaderImpl
		extends EzyAbstractReader<EzyArray, Scan1ClassC> {
	
	@Override
	public Scan1ClassC read(EzyUnmarshaller unmarshaller, EzyArray array) {
		Scan1ClassC answer = new Scan1ClassC();
		answer.setX(array.get(0, String.class));
		answer.setY(array.get(1, String.class));
		answer.setZ(array.get(2, String.class));
		return answer;
	}

	public EzyArray write(EzyMarshaller marshaller, Scan1ClassC object) {
		return newArrayBuilder()
				.append(object.getX())
				.append(object.getY())
				.append(object.getZ())
				.build();
	}
	
}
