package com.tvd12.ezyfoxserver.admintools.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {
		"com.tvd12.ezyfoxserver.admintools.controller"
})
public class EzyAdminToolsWebConfig extends WebMvcConfigurerAdapter {

	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public EzyAdminToolsWebConfig() {
		logger.debug("\ninit admintools web config\n");
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**")
				.addResourceLocations("/css/");
		registry.addResourceHandler("/js/**")
				.addResourceLocations("/js/");		
		registry.addResourceHandler("/fonts/**")
				.addResourceLocations("/fonts/");
		registry.addResourceHandler("/font-awesome/**")
				.addResourceLocations("/font-awesome/");
		registry.addResourceHandler("/img/**")
				.addResourceLocations("/img/");
	}
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.freeMarker();
	}
	
	@Bean
	public FreeMarkerViewResolver freemarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setCache(false);
		resolver.setPrefix("/WEB-INF/views/ftl/");
		resolver.setSuffix(".ftl");
		resolver.setContentType("text/html;charset=UTF-8");
		return resolver;
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfig() {
		FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
		freeMarkerConfigurer.setTemplateLoaderPath("/WEB-INF/views/ftl/");
		freeMarkerConfigurer.setDefaultEncoding("UTF-8");
		return freeMarkerConfigurer;
	}
}
