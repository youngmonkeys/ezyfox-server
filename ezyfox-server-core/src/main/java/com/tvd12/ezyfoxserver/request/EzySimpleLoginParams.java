package com.tvd12.ezyfoxserver.request;

import com.tvd12.ezyfox.entity.EzyArray;
import com.tvd12.ezyfox.entity.EzyData;

import lombok.Getter;

@Getter
public class EzySimpleLoginParams
        extends EzySimpleRequestParams
        implements EzyLoginParams {
    private static final long serialVersionUID = -2983750912126505224L;
    
    private String zoneName;
    private String username;
    private String password;
    private EzyData data;
    
    @Override
    public void deserialize(EzyArray t) {
        this.zoneName = t.get(0, String.class);
        this.username = t.get(1, String.class);
        this.password = t.get(2, String.class);
        this.data = t.get(3, EzyData.class, null);
    }
    
    @Override
    public void release() {
        super.release();
        this.data = null;
    }
    
}
