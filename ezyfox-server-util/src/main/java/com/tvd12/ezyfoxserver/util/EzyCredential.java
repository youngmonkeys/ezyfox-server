package com.tvd12.ezyfoxserver.util;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyCredential implements Serializable {
	private static final long serialVersionUID = 4525085403412972161L;
	
	protected String username;
	protected String password;

}
