package com.tvd12.ezyfoxserver.constant;

import com.tvd12.ezyfox.constant.EzyConstant;

import lombok.Getter;

@Getter
public enum EzyUserRemoveReason implements EzyConstant {

    EXIT_APP(1);
    
    private final int id;
    
    private EzyUserRemoveReason(int id) {
        this.id = id;
    }
    
    @Override
    public String getName() {
        return toString();
    }
    
}
