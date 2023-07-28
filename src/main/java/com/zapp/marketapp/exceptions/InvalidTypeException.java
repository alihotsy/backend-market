package com.zapp.marketapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvalidTypeException extends RuntimeException {
    private boolean ok;
    private String msg;
    private String source;

    public InvalidTypeException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
