package com.tvd12.ezyfoxserver.binding.testing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.tvd12.ezyfoxserver.binding.annotation.EzyIgnore;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReader;
import com.tvd12.ezyfoxserver.binding.annotation.EzyValue;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Point extends BasePoint {
 
	@EzyValue("3")
	public int z = 100;
	
	@EzyIgnore
	public String a = "a";
	
	@Getter(AccessLevel.NONE)
	@Setter(AccessLevel.NONE)
	public String a1 = "a1";
	
	private Point point;
	
	private EzyTestData data = new EzyTestData();
	
	@EzyWriter(TestData1WriterImpl.class)
	@EzyReader(TestData1ReaderImpl.class)
	private EzyTestData1 data1 = new EzyTestData1();
	
	private Date date = new Date();
	private LocalDate localDate = LocalDate.now();
	private LocalDateTime localDateTime = LocalDateTime.now();
	@SuppressWarnings("rawtypes")
	private Class clazz = Point.class;
	
	public Point() {
		this.point = new Point(1, 0);
	}
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public String getB() {
    	return "b";
    }
    
    @EzyIgnore
    public String getC() {
    	return "c";
    }
}
