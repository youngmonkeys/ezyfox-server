package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfoxserver.entity.EzyArray;
import com.tvd12.ezyfoxserver.entity.EzyData;
import com.tvd12.ezyfoxserver.io.EzyArrayDeserializable;

import lombok.Getter;

@Getter
public class EzySimpleLoginParams implements EzyLoginParams, EzyArrayDeserializable {
    private static final long serialVersionUID = -2983750912126505224L;
    
    private String username;
    private String password;
    private EzyData data;
    
    @Override
    public void deserialize(EzyArray t) {
        this.username = t.get(0, String.class);
        this.password = t.get(1, String.class);
        this.data = t.get(2, EzyData.class);
    }
    
}
