package com.tvd12.ezyfoxserver.util;

import java.net.URL;

public final class EzyUriValidator {

	private EzyUriValidator() {
	}
	
	public static boolean validateUri(String uri) {
		try { 
	        new URL(uri);
	        return true;
	    } catch (Exception e1) {
	        return false; 
	    } 
	}
	
}
