package com.zapp.marketapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityException extends RuntimeException {

    private boolean ok;

    private String source;
    private String msg;
    private String field;
    private int status;


    public EntityException(boolean ok, String msg, String field, int status, String source) {
        super(msg);
        this.ok = ok;
        this.msg = msg;
        this.field = field;
        this.status = status;
        this.source = source;
    }
}
