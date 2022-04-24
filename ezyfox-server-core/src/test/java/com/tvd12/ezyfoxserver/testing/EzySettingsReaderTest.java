package com.tvd12.ezyfoxserver.testing;

import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import org.testng.annotations.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

public class EzySettingsReaderTest {

    @Test
    public void test() throws Exception {
//        JAXBContext jaxbContext = JAXBContext.newInstance(EzyFoxSettings.class);
        JAXBContext jaxbContext = JAXBContext.newInstance("com.tvd12.ezyfoxserver", getClass().getClassLoader());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        EzySimpleSettings settings = jaxbUnmarshaller
            .unmarshal(new StreamSource(inputStream()), EzySimpleSettings.class).getValue();
        System.out.println(settings);
    }

    private InputStream inputStream() {
        return getClass().getClassLoader()
            .getResourceAsStream("ezy-settings.xml");
    }


}
