package com.tvd12.ezyfoxserver.exception;

import com.tvd12.ezyfoxserver.constant.EzyILoginError;
import com.tvd12.ezyfoxserver.constant.EzyLoginError;

import lombok.Getter;

public class EzyLoginErrorException extends IllegalStateException {
    private static final long serialVersionUID = 1432595688787320396L;

    @Getter
    private final EzyILoginError error;
    
    public EzyLoginErrorException() {
        this(EzyLoginError.ALREADY_LOGIN);
    }
    
    public EzyLoginErrorException(EzyILoginError error) {
        super(error.getMessage());
        this.error = error;
    }
    
}
