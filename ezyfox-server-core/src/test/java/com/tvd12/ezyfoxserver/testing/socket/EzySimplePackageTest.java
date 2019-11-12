package com.tvd12.ezyfoxserver.testing.socket;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.constant.EzyTransportType;
import com.tvd12.ezyfoxserver.socket.EzySimplePackage;
import com.tvd12.test.base.BaseTest;

public class EzySimplePackageTest extends BaseTest {
	
	@Test
	public void test() {
		EzySimplePackage pack = new EzySimplePackage();
		pack.setTransportType(EzyTransportType.TCP);
		assert pack.getTransportType() == EzyTransportType.TCP;
	}

}
