package com.tvd12.ezyfoxserver.binding.testing.scan1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyReader;
import com.tvd12.ezyfoxserver.binding.annotation.EzyWriter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
@EzyObjectBinding
public class Scan1ClassA extends Scan1ClassA1 {
	private String a = "1";
	private String b = "1";
	private String c = "1";
	private int d = 10;
	
	private Scan1ClassB classB = new Scan1ClassB();
	
	@EzyWriter(Scan1ClassCWriterImpl.class)
	@EzyReader(Scan1ClassCReaderImpl.class)
	private Scan1ClassC classC = new Scan1ClassC();
	
	private Map<String, String> map = newMap();
	
	private List<String> list = Lists.newArrayList("a", "b", "c");
	
	private List<Data> dataList1 = Lists.newArrayList(new Data(), new Data());
	
	private Data[] dataArray1 = new Data[] {new Data(), new Data()};
	
	protected Map<String, String> newMap() {
		Map<String, String> map = new HashMap<>();
		map.put("hello", "world");
		return map;
	}
}
