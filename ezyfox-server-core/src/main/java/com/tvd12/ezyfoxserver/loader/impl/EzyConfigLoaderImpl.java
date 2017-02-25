package com.tvd12.ezyfoxserver.loader.impl;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.config.EzyConfig;
import com.tvd12.ezyfoxserver.loader.EzyConfigLoader;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.BaseFileReader;

public class EzyConfigLoaderImpl implements EzyConfigLoader {

	private String filePath;
	
	private EzyConfigLoaderImpl() {
	}
	
	public static EzyConfigLoaderImpl newInstance() {
		return new EzyConfigLoaderImpl();
	}
	
	public EzyConfigLoaderImpl filePath(final String filePath) {
		this.filePath = filePath;
		return this;
	}
	
	@Override
	public EzyConfig load() {
		validateFilePath(filePath);
		return doLoad();
	}
	
	private EzyConfig doLoad() {
		return new PropertiesMapper()
				.context(getClass())
				.clazz(EzyConfig.class)
				.file(filePath)
				.reader(new BaseFileReader())
				.map();
	}
	
	private void validateFilePath(final String filePath) {
		if(StringUtils.isEmpty(filePath))
			throw new IllegalArgumentException("config file path can't be null");
	}
	
}
