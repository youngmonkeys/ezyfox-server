package com.tvd12.ezyfoxserver.binding.testing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.tvd12.ezyfoxserver.binding.EzyReader;
import com.tvd12.ezyfoxserver.binding.EzyUnmarshaller;
import com.tvd12.ezyfoxserver.entity.EzyObject;

@SuppressWarnings("rawtypes")
public class EzyPointReaderImpl implements EzyReader {

	@Override
	public Object read(EzyUnmarshaller arg0, Object arg1) {
		EzyObject value = (EzyObject)arg1;
		Point object = new Point();
		if(value.isNotNullValue("a1"))
			object.a1 = (String)arg0.unmarshal((Object)value.getValue("a1", String.class), String.class);
		if(value.isNotNullValue("3"))
			object.setX(((Integer)arg0.unmarshal((Object)value.getValue("3", int.class), int.class)).intValue());
		if(value.isNotNullValue("x"))
			object.setX(((Integer)arg0.unmarshal((Object)value.getValue("x", int.class), int.class)).intValue());
		if(value.isNotNullValue("y"))
			object.setY(((Integer)arg0.unmarshal((Object)value.getValue("y", int.class), int.class)).intValue());
		if(value.isNotNullValue("point"))
			object.setPoint((Point)arg0.unmarshal((Object)value.getValue("point", Point.class), Point.class));
		if(value.isNotNullValue("data"))
			object.setData((EzyTestData)arg0.unmarshal((Object)value.getValue("data", EzyTestData.class), EzyTestData.class));
		if(value.isNotNullValue("date"))
			object.setDate((Date)arg0.unmarshal((Object)value.getValue("date", Date.class), Date.class));
		if(value.isNotNullValue("localDate"))
			object.setLocalDate((LocalDate)arg0.unmarshal((Object)value.getValue("localDate", LocalDate.class), LocalDate.class));
		if(value.isNotNullValue("localDateTime"))
			object.setLocalDateTime((LocalDateTime)arg0.unmarshal((Object)value.getValue("localDateTime", LocalDateTime.class), LocalDateTime.class));
		if(value.isNotNullValue("clazz"))
			object.setClazz((Class)arg0.unmarshal((Object)value.getValue("clazz", Class.class), Class.class));
		if(value.isNotNullValue("data1"))
			object.setData1((EzyTestData1)arg0.unmarshal(TestData1ReaderImpl.class, (Object)value.getValue("data1", EzyTestData1.class)));
		return object;
	}

}
