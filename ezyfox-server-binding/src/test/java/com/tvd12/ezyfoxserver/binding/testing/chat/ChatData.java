package com.tvd12.ezyfoxserver.binding.testing.chat;

import java.io.Serializable;
import java.util.Date;

import com.tvd12.ezyfoxserver.binding.annotation.EzyValue;
import com.tvd12.ezyfoxserver.io.EzyDates;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatData implements Serializable {
	private static final long serialVersionUID = -1053536008550958384L;

	@EzyValue("2")
	private Date creationDate = new Date();
	@EzyValue("3")
	private Date lastReadDate = new Date();
	@EzyValue("4")
	private Date lastModifiedDate = new Date();
	@EzyValue("5")
	private int day = EzyDates.formatAsInteger(new Date());
	
}
