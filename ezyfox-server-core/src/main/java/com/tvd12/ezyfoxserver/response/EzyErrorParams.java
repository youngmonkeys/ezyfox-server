package com.tvd12.ezyfoxserver.response;

import com.tvd12.ezyfoxserver.builder.EzyArrayBuilder;
import com.tvd12.ezyfoxserver.constant.EzyIError;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EzyErrorParams extends EzySimpleResponseParams {
    private static final long serialVersionUID = -2295966289771483116L;
    
    protected int code;
    protected String message;
    
    public void setError(EzyIError error) {
        this.setCode(error.getId());
        this.setMessage(error.getMessage());
    }
    
    @Override
    protected EzyArrayBuilder serialize0() {
        return newArrayBuilder()
            .append(code)
            .append(message);
    }
    
}
