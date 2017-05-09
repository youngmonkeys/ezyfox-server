package com.tvd12.ezyfoxserver.bean.testing.combine.pack0;

import com.tvd12.ezyfoxserver.bean.annotation.EzyPackagesScan;
import com.tvd12.ezyfoxserver.util.EzyPropertiesAware;

import java.util.Properties;

import com.tvd12.ezyfoxserver.bean.EzyBeanConfig;
import com.tvd12.ezyfoxserver.bean.annotation.EzyConfiguration;

@EzyConfiguration
@EzyPackagesScan({"com.tvd12.ezyfoxserver.bean.testing.combine.pack3"})
public class Configuration1 implements EzyBeanConfig, EzyPropertiesAware {

	@Override
	public void setProperties(Properties properties) {
	}
	
	@Override
	public void config() {
		
	}
	
}
