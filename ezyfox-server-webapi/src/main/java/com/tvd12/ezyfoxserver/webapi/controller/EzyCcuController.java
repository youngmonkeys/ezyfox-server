package com.tvd12.ezyfoxserver.webapi.controller;

public class EzyCcuController extends EzyAbstractController {

	public int getCcuCount() {
		return getServerSessionManger().getLoggedInSessionCount();
	}
	
}
