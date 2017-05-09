package com.tvd12.ezyfoxserver.hazelcast.impl;

import com.tvd12.ezyfoxserver.annotation.EzyAutoImpl;
import com.tvd12.ezyfoxserver.annotation.EzyKeyValue;
import com.tvd12.ezyfoxserver.hazelcast.annotation.EzyMapServiceAutoImpl;

public class EzySimpleMapNameFetcher {

	public String getMapName(Class<?> clazz) {
		EzyAutoImpl autoImplAnno = clazz.getAnnotation(EzyAutoImpl.class);
		if(autoImplAnno != null) {
			return getMapName(autoImplAnno);
		}
		EzyMapServiceAutoImpl mapServiceAuto = clazz.getAnnotation(EzyMapServiceAutoImpl.class);
		if(mapServiceAuto != null) {
			return getMapName(mapServiceAuto);
		}
		throw new IllegalArgumentException("can't get map name, annotate your interface with:\n" + 
				"@EzyAutoImpl(properties = {\n" + 
						"\t@EzyKeyValue(key = \"map.name\", value = \"<your map name>\")\n" + 
				"})" + 
				"\nor\n" + 
				"@EzyMapServiceAutoImpl(\"<your map name>\")");
	}
	
	private String getMapName(EzyMapServiceAutoImpl anno) {
		return anno.value();
	}
	
	private String getMapName(EzyAutoImpl anno) {
		EzyKeyValue[] props = anno.properties();
		EzyKeyValue keyValue = null;
		if(props.length != 0) {
			for(EzyKeyValue pro : props) {
				if(pro.key().equals("map.name")) {
					keyValue = pro; break;
				}
			}
		}
		if(keyValue == null) {
			throw new IllegalArgumentException("can't get map name, annotate your interface with:\n" + 
					"@EzyAutoImpl(properties = {\n" + 
							"\t@EzyKeyValue(key = \"map.name\", value = \"<your map name>\")\n" + 
					"})");
		}
		return keyValue.value();
	}
	
}
