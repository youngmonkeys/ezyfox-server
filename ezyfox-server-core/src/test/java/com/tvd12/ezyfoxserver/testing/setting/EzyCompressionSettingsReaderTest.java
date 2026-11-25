package com.tvd12.ezyfoxserver.testing.setting;

import com.tvd12.ezyfoxserver.setting.EzySimpleSettings;
import com.tvd12.test.assertion.Asserts;
import org.testng.annotations.Test;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class EzyCompressionSettingsReaderTest {

    private static final File SETTINGS_FILE = new File("src/main/resources/ezy-settings.xml");
    private static final File SCHEMA_FILE = new File("src/main/resources/ezy-settings-1.0.0.xsd");

    @Test
    public void validateDefaultSettingsBySchemaTest() throws Exception {
        // given
        Schema schema = SchemaFactory
            .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)
            .newSchema(SCHEMA_FILE);

        // when
        schema.newValidator().validate(new StreamSource(SETTINGS_FILE));

        // then
        Asserts.assertTrue(true);
    }

    @Test
    public void readDefaultSettingsTest() throws Exception {
        // given
        String xml = readSettingsXml();

        // when
        EzySimpleSettings settings = unmarshal(xml);

        // then
        Asserts.assertTrue(settings.getSocket().isCompressionEnable());
        Asserts.assertEquals(settings.getSocket().getCompressionThreshold(), 512);
        Asserts.assertTrue(settings.getWebsocket().isCompressionEnable());
    }

    @Test
    public void readDisabledCompressionSettingsTest() throws Exception {
        // given
        String xml = readSettingsXml()
            .replace(
                "<compression-enable>true</compression-enable>",
                "<compression-enable>false</compression-enable>"
            )
            .replace(
                "<compression-threshold>512</compression-threshold>",
                "<compression-threshold>2048</compression-threshold>"
            );

        // when
        EzySimpleSettings settings = unmarshal(xml);

        // then
        Asserts.assertFalse(settings.getSocket().isCompressionEnable());
        Asserts.assertEquals(settings.getSocket().getCompressionThreshold(), 2048);
        Asserts.assertFalse(settings.getWebsocket().isCompressionEnable());
    }

    private static String readSettingsXml() throws Exception {
        return new String(
            Files.readAllBytes(SETTINGS_FILE.toPath()),
            StandardCharsets.UTF_8
        );
    }

    private EzySimpleSettings unmarshal(String xml) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(
            "com.tvd12.ezyfoxserver",
            getClass().getClassLoader()
        );
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return unmarshaller
            .unmarshal(new StreamSource(new StringReader(xml)), EzySimpleSettings.class)
            .getValue();
    }
}
