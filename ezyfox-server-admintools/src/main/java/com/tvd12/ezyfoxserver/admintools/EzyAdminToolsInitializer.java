package com.tvd12.ezyfoxserver.admintools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.tvd12.ezyfoxserver.admintools.configure.EzyAdminToolsWebConfig;

public class EzyAdminToolsInitializer extends AbstractAnnotationConfigDispatcherServletInitializer  {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public EzyAdminToolsInitializer() {
		logger.debug("\ninit admintools initializer\n");
	}
	
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { EzyAdminToolsWebConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
}
