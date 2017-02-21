package com.tvd12.ezyfoxserver.service.impl;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import com.tvd12.ezyfoxserver.exception.EzyReadingXmlException;
import com.tvd12.ezyfoxserver.service.EzyXmlReading;

public class EzyXmlReadingImpl implements EzyXmlReading {

	private Unmarshaller unmarshaller;
	
	private  EzyXmlReadingImpl(final Builder builder) {
		this.unmarshaller = builder.newUnmarshaller();
	}
	
	@Override
	public <T> T read(final File xmlFile, final Class<T> outputType) {
		try {
			return unmarshaller.unmarshal(new StreamSource(xmlFile), outputType).getValue();
		} catch (JAXBException e) {
			throw new EzyReadingXmlException("Can not read xml file " + xmlFile + " with " + outputType, e);
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
				throw new EzyReadingXmlException(e);
			}
		}
		
		public EzyXmlReadingImpl build() {
			return new EzyXmlReadingImpl(this);
		}
		
		private JAXBContext newJAXBContext() {
			try {
				return JAXBContext.newInstance(contextPath, classLoader);
			} catch (JAXBException e) {
				throw new EzyReadingXmlException(e);
			}
		}
	}
	
}
