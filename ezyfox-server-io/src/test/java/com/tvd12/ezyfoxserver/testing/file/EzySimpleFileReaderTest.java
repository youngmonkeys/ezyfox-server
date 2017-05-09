package com.tvd12.ezyfoxserver.testing.file;

import java.io.File;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.file.EzyFileReader;
import com.tvd12.ezyfoxserver.file.EzyFileWriter;
import com.tvd12.ezyfoxserver.file.EzySimpleFileReader;
import com.tvd12.ezyfoxserver.file.EzySimpleFileWriter;
import com.tvd12.test.base.BaseTest;

public class EzySimpleFileReaderTest extends BaseTest {

	private File directory = new File("test");
	private EzyFileWriter writer = EzySimpleFileWriter.builder().build();
	private EzyFileReader reader = EzySimpleFileReader.builder().build();
	
	public EzySimpleFileReaderTest() {
		super();
		directory.mkdirs();
	}
	
	@Test
	public void test() {
		File file = new File(directory.getAbsolutePath() + File.separator + "EzySimpleFileWriterTest.txt");
		writer.write(file, new byte[] {'a', 'b', 'c'});
		reader.readBytes(file);
		reader.readLines(file, "UTF-8");
	}
	
	@Test(expectedExceptions = {IllegalArgumentException.class})
	public void test1() {
		reader.readBytes(new File("\\/xfav"));
	}
	
}
