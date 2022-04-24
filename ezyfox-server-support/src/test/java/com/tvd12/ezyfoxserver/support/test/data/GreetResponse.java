package com.tvd12.ezyfoxserver.support.test.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@EzyObjectBinding(read = false)
public class GreetResponse {

    protected String message;

}
