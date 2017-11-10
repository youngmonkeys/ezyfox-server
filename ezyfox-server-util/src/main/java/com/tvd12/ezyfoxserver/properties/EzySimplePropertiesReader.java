package com.tvd12.ezyfoxserver.properties;

import java.util.Map;

import com.tvd12.ezyfoxserver.io.EzySimpleValueConverter;
import com.tvd12.ezyfoxserver.io.EzyValueConverter;
import com.tvd12.ezyfoxserver.util.EzyLoggable;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class EzySimplePropertiesReader
		extends EzyLoggable
		implements EzyPropertiesReader {

	protected final EzyValueConverter conveter;
	
	public EzySimplePropertiesReader() {
		this(new EzySimpleValueConverter());
	}
	
	public EzySimplePropertiesReader(EzyValueConverter conveter) {
		this.conveter = conveter;
	}
	
	@Override
	public <T> T get(Map properties, Object key) {
		return (T) properties.get(key);
	}

	@Override
	public <T> T get(Map properties, Object key, Class<T> outType) {
		return conveter.convert(get(properties, key), outType);
	}
	
	@Override
	public <T> T get(Map properties, Object key, T defValue) {
		return containsKey(properties, key) ? get(properties, key) : defValue;
	}

	@Override
	public <T> T get(Map properties, Object key, Class<T> outType, T defValue) {
		return containsKey(properties, key) ? get(properties, key, outType) : defValue;
	}
	
	@Override
	public boolean containsKey(Map properties, Object key) {
		return properties.containsKey(key);
	}
	
}
