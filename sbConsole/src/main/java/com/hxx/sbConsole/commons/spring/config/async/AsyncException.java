package com.hxx.sbConsole.commons.spring.config.async;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AsyncException extends RuntimeException{
    private static final long serialVersionUID = -7001746126625227781L;

    private int code;

    public AsyncException() {
        super();
    }

    public AsyncException(String msg) {
        super(msg);
    }

    public AsyncException(int code, String msg) {
        super(msg);
        this.code = code;
    }
}
