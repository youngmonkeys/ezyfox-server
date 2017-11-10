package com.tvd12.ezyfoxserver.binding.testing;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReaderImpl;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractReader;
import com.tvd12.ezyfoxserver.entity.EzyArray;

@EzyReaderImpl
public class TestData1ReaderImpl extends EzyAbstractReader<EzyArray, EzyTestData1> {

	@Override
	public EzyTestData1 read(EzyUnmarshaller arg0, EzyArray array) {
		EzyTestData1 data = new EzyTestData1();
		data.setData1(((Integer)arg0.unmarshal((Object)array.getValue(0, int.class), int.class)).intValue());
		data.setData2(((Integer)arg0.unmarshal((Object)array.getValue(1, int.class), int.class)).intValue());
		data.setData3(((Integer)arg0.unmarshal((Object)array.getValue(2, int.class), int.class)).intValue());
		return data;
	}
	
}
