package com.tvd12.ezyfoxserver.binding.testing.chat;


import com.tvd12.ezyfoxserver.binding.annotation.EzyObjectBinding;
import com.tvd12.ezyfoxserver.binding.annotation.EzyValue;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@EzyObjectBinding
public class ChatMessage extends ChatData {
	private static final long serialVersionUID = 6130168551127865806L;

	@EzyValue("1")
	private long id = 1;
	@EzyValue("6")
	private String clientMessageId = "1";
	@EzyValue("7")
	private String message = "1";
	@EzyValue("8")
	private String receiver = "1";
	@EzyValue("9")
	private String sender = "1";
	
	
}
