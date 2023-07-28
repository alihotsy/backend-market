package com.zapp.marketapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityExceptionCaught {
    private boolean ok;
    private String source;
    private String msg;
    private String field;
    private int status;
}
