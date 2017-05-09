package com.tvd12.ezyfoxserver.binding.testing;

import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.binding.impl.EzyAbstractReader;
import com.tvd12.ezyfoxserver.entity.EzyObject;

@SuppressWarnings("rawtypes")
public class EzyTestDataReaderImpl extends EzyAbstractReader {

	@Override
	public Object read(EzyUnmarshaller arg0, Object arg1) {
		EzyObject value = ((EzyObject)arg1);
		EzyTestData data = new EzyTestData();
		if(value.isNotNullValue("1"))
			data.setData1(((Integer)arg0.unmarshal((Object)value.getValue("1", int.class), int.class)).intValue());
		if(value.isNotNullValue("2"))
			data.setData2(((Integer)arg0.unmarshal((Object)value.getValue("2", int.class), int.class)).intValue());
		if(value.isNotNullValue("3"))
			data.setData2(((Integer)arg0.unmarshal((Object)value.getValue("3", int.class), int.class)).intValue());
		return data;
	}

	
	
}
