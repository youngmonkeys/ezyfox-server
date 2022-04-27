package com.tvd12.ezyfoxserver.support.test.data;

import com.tvd12.ezyfox.binding.annotation.EzyObjectBinding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@EzyObjectBinding
@NoArgsConstructor
@AllArgsConstructor
public class GreetRequest {
    protected String who;
}
