package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.socket.EzySimpleBytesPackage;
import com.tvd12.test.base.BaseTest;

public class EzySimpleBytesPackageTest extends BaseTest {
	
	@Test
	public void test() {
		EzySimpleBytesPackage pack = new EzySimpleBytesPackage();
		pack.setTransportType(EzyTransportType.TCP);
		assert pack.getTransportType() == EzyTransportType.TCP;
	}

}
