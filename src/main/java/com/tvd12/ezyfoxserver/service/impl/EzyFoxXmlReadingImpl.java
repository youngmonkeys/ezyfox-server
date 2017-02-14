package com.tvd12.ezyfoxserver.service.impl;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.tvd12.ezyfoxserver.exception.EzyFoxReadingXmlException;
import com.tvd12.ezyfoxserver.service.EzyFoxXmlReading;

public class EzyFoxXmlReadingImpl implements EzyFoxXmlReading {

	private Unmarshaller unmarshaller;
	
	private  EzyFoxXmlReadingImpl(final Builder builder) {
		this.unmarshaller = builder.newUnmarshaller();
	}
	
	@Override
	public <T> T read(final File xmlFile, final Class<T> outputType) {
		try {
			return unmarshaller.unmarshal(new StreamSource(xmlFile), outputType).getValue();
		} catch (JAXBException e) {
			throw new EzyFoxReadingXmlException("Can not read xml file " + xmlFile + " with " + outputType, e);
		}
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		private String contextPath;
		private ClassLoader classLoader;
		
		public Builder contextPath(final String contextPath) {
			this.contextPath = contextPath;
			return this;
		}
		
		public Builder classLoader(final ClassLoader classLoader) {
			this.classLoader = classLoader;
			return this;
		}
		
		public Unmarshaller newUnmarshaller() {
			try {
				return newJAXBContext().createUnmarshaller();
			} catch (JAXBException e) {
				throw new EzyFoxReadingXmlException(e);
			}
		}
		
		public EzyFoxXmlReadingImpl build() {
			return new EzyFoxXmlReadingImpl(this);
		}
		
		private JAXBContext newJAXBContext() {
			try {
				return JAXBContext.newInstance(contextPath, classLoader);
			} catch (JAXBException e) {
				throw new EzyFoxReadingXmlException(e);
			}
		}
	}
	
}
