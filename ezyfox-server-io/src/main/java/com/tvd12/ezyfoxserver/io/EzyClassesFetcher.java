package com.tvd12.ezyfoxserver.io;

import java.util.List;
import java.util.Set;

public interface EzyClassesFetcher {
	
	@SuppressWarnings("rawtypes")
	Set<Class> asSet(String filePath);

	@SuppressWarnings("rawtypes")
	List<Class> asList(String filePath);

}