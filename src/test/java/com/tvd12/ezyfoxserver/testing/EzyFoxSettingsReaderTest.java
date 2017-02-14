package com.tvd12.ezyfoxserver.testing;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.config.EzyFoxSettings;

public class EzyFoxSettingsReaderTest {

	@Test
	public void test() throws Exception {
//		JAXBContext jaxbContext = JAXBContext.newInstance(EzyFoxSettings.class);
		JAXBContext jaxbContext = JAXBContext.newInstance("com.tvd12.ezyfoxserver", getClass().getClassLoader());
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		EzyFoxSettings settings = jaxbUnmarshaller
				.unmarshal(new StreamSource(inputStream()), EzyFoxSettings.class).getValue();
		System.out.println(settings);
	}
	
	private InputStream inputStream() {
		return getClass().getClassLoader()
				.getResourceAsStream("ezy-settings.xml");
	}
	
	
}
