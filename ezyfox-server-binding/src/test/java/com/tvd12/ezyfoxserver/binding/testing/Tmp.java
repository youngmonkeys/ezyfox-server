package com.tvd12.ezyfoxserver.binding.testing;

public class Tmp {

	@SuppressWarnings("rawtypes")
	public java.lang.Object read(com.tvd12.ezyfoxserver.binding.EzyUnmarshaller arg0, java.lang.Object arg1) {
		com.tvd12.ezyfoxserver.entity.EzyObject value = ((com.tvd12.ezyfoxserver.entity.EzyObject)(arg1));
		com.tvd12.ezyfoxserver.binding.testing.Point object = new com.tvd12.ezyfoxserver.binding.testing.Point();
		if(value.isNotNullValue("a1"))
			object.a1 = ((java.lang.String)(arg0.unmarshal((java.lang.Object)value.getValue("a1", java.lang.String.class), java.lang.String.class)));
		if(value.isNotNullValue("data"))
			object.setData(((com.tvd12.ezyfoxserver.binding.testing.EzyTestData)(arg0.unmarshal((java.lang.Object)value.getValue("data", com.tvd12.ezyfoxserver.binding.testing.EzyTestData.class), com.tvd12.ezyfoxserver.binding.testing.EzyTestData.class))));
		if(value.isNotNullValue("y"))
			object.setY(((java.lang.Integer)(arg0.unmarshal((java.lang.Object)value.getValue("y", int.class), int.class))).intValue());
		if(value.isNotNullValue("x"))
			object.setX(((java.lang.Integer)(arg0.unmarshal((java.lang.Object)value.getValue("x", int.class), int.class))).intValue());
		if(value.isNotNullValue("localDateTime"))
			object.setLocalDateTime(((java.time.LocalDateTime)(arg0.unmarshal((java.lang.Object)value.getValue("localDateTime", java.time.LocalDateTime.class), java.time.LocalDateTime.class))));
		if(value.isNotNullValue("date"))
			object.setDate(((java.util.Date)(arg0.unmarshal((java.lang.Object)value.getValue("date", java.util.Date.class), java.util.Date.class))));
		if(value.isNotNullValue("point"))
			object.setPoint(((com.tvd12.ezyfoxserver.binding.testing.Point)(arg0.unmarshal((java.lang.Object)value.getValue("point", com.tvd12.ezyfoxserver.binding.testing.Point.class), com.tvd12.ezyfoxserver.binding.testing.Point.class))));
		if(value.isNotNullValue("clazz"))
			object.setClazz(((java.lang.Class)(arg0.unmarshal((java.lang.Object)value.getValue("clazz", java.lang.Class.class), java.lang.Class.class))));
		if(value.isNotNullValue("3"))
			object.setZ(((java.lang.Integer)(arg0.unmarshal((java.lang.Object)value.getValue("3", int.class), int.class))).intValue());
		if(value.isNotNullValue("localDate"))
			object.setLocalDate(((java.time.LocalDate)(arg0.unmarshal((java.lang.Object)value.getValue("localDate", java.time.LocalDate.class), java.time.LocalDate.class))));
		if(value.isNotNullValue("data1"))
			object.setData1(((com.tvd12.ezyfoxserver.binding.testing.EzyTestData1)(arg0.unmarshal(com.tvd12.ezyfoxserver.binding.testing.TestData1ReaderImpl.class, (java.lang.Object)value.getValue("data1", com.tvd12.ezyfoxserver.binding.testing.EzyTestData1.class)))));
		return object;
	}
	
}
