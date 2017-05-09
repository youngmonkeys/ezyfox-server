package com.tvd12.ezyfoxserver.adt.action;

import com.opensymphony.xwork2.ActionSupport;
import com.tvd12.ezyfoxserver.hazelcast.service.EzyMaxIdService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HomeAction extends ActionSupport {
	private static final long serialVersionUID = -5885995144592628588L;
	
	private String random = "random";

	@Getter(AccessLevel.NONE)
	private transient EzyMaxIdService maxIdService;
	
	@Override
	public String execute() throws Exception {
		setRandom(String.valueOf(maxIdService.incrementAndGet("count")));
		return SUCCESS;
	}

}
