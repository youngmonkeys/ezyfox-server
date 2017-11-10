package com.tvd12.ezyfoxserver.mapping.jaxb;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class EzySimplXmlMapper implements EzyXmlMapper {

	private Unmarshaller unmarshaller;
	
	private  EzySimplXmlMapper(Builder builder) {
		this.unmarshaller = builder.newUnmarshaller();
	}
	
	@Override
	public <T> T read(File xmlFile, Class<T> outputType) {
		try {
			return unmarshaller.unmarshal(new StreamSource(xmlFile), outputType).getValue();
		} catch (JAXBException e) {
			throw new IllegalArgumentException("Can not read xml file " + xmlFile.getAbsolutePath() + " with " + outputType, e);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String contextPath;
		private ClassLoader classLoader;
		
		public Builder contextClass(Class<?> clazz) {
		    this.classLoader = clazz.getClassLoader();
		    this.contextPath = clazz.getPackage().getName();
		    return this;
		}
		
		public Builder contextPath(String contextPath) {
			this.contextPath = contextPath;
			return this;
		}
		
		public Builder classLoader(ClassLoader classLoader) {
			this.classLoader = classLoader;
			return this;
		}
		
		protected Unmarshaller newUnmarshaller() {
			try {
				return newJAXBContext().createUnmarshaller();
			} catch (JAXBException e) {
				throw new IllegalArgumentException(e);
			}
		}
		
		public EzySimplXmlMapper build() {
			return new EzySimplXmlMapper(this);
		}
		
		protected JAXBContext newJAXBContext() throws JAXBException {
		    return JAXBContext.newInstance(contextPath, classLoader);
		}
	}
	
}
