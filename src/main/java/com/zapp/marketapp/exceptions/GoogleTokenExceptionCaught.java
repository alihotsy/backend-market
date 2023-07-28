package com.zapp.marketapp.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GoogleTokenExceptionCaught {

    private boolean ok;
    private String msg;
    private String source;
}
