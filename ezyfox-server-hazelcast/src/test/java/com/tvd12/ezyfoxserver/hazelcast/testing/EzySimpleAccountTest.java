package com.tvd12.ezyfoxserver.hazelcast.testing;

import java.util.Date;

import org.testng.annotations.Test;

import com.tvd12.ezyfoxserver.hazelcast.entity.EzyAbstractAccount;
import com.tvd12.test.base.BaseTest;

import lombok.Getter;
import lombok.Setter;

public class EzySimpleAccountTest extends BaseTest {

	@Test
	public void test() {
		EzyAbstractAccount account = new EzyAbstractAccount() {
			private static final long serialVersionUID = -1100249149170475581L;
			
			@Setter
			@Getter
			private Long id;
			
		};
		account.setId(10L);
		account.setValue(100L);
		account.setType("MONEY");
		Date now = new Date();
		account.setCreationDate(now);
		account.setLastUpdatedDate(now);
		assert account.getType().equals("MONEY");
		assert account.getCreationDate().equals(now);
		assert account.getLastUpdatedDate().equals(now);
		assert account.getId() == 10L;
		assert account.getValue() == 100L;
	}
	
}
