package com.tvd12.ezyfoxserver.loader.impl;

import org.apache.commons.lang3.StringUtils;

import com.tvd12.ezyfoxserver.config.EzyFoxConfig;
import com.tvd12.ezyfoxserver.loader.EzyFoxConfigLoader;
import com.tvd12.properties.file.mapping.PropertiesMapper;
import com.tvd12.properties.file.reader.BaseFileReader;

public class EzyFoxConfigLoaderImpl implements EzyFoxConfigLoader {

	private String filePath;
	
	private EzyFoxConfigLoaderImpl() {
	}
	
	public static EzyFoxConfigLoaderImpl newInstance() {
		return new EzyFoxConfigLoaderImpl();
	}
	
	public EzyFoxConfigLoaderImpl filePath(final String filePath) {
		this.filePath = filePath;
		return this;
	}
	
	@Override
	public EzyFoxConfig load() {
		validateFilePath(filePath);
		return doLoad();
	}
	
	private EzyFoxConfig doLoad() {
		return new PropertiesMapper()
				.context(getClass())
				.clazz(EzyFoxConfig.class)
				.file(filePath)
				.reader(new BaseFileReader())
				.map();
	}
	
	private void validateFilePath(final String filePath) {
		if(StringUtils.isEmpty(filePath))
			throw new IllegalArgumentException("config file path can't be null");
	}
	
}
