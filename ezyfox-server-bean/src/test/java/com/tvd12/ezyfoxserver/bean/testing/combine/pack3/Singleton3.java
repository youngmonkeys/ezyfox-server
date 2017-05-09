package com.tvd12.ezyfoxserver.bean.testing.combine.pack3;

import java.util.ArrayList;

import com.tvd12.ezyfoxserver.bean.annotation.EzyAutoBind;
import com.tvd12.ezyfoxserver.bean.annotation.EzySingleton;

import lombok.Getter;
import lombok.Setter;

@Getter
@EzySingleton("s3")
public class Singleton3 {

	@Setter
	@EzyAutoBind
	private ArrayList<String> list;
	
}
