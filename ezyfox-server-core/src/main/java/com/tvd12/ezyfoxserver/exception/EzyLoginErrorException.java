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

    public EzyLoginErrorException(EzyILoginError error, Exception e) {
        super(error.getMessage(), e);
        this.error = error;
    }

    public static EzyLoginErrorException maximumUsers(Exception e) {
        return new EzyLoginErrorException(EzyLoginError.MAXIMUM_USER, e);
    }

    public static EzyLoginErrorException zoneNotFound(Exception e) {
        return new EzyLoginErrorException(EzyLoginError.ZONE_NOT_FOUND, e);
    }

    public static EzyLoginErrorException serverError(Exception e) {
        return new EzyLoginErrorException(EzyLoginError.SERVER_ERROR, e);
    }

}
