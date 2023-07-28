package com.zapp.marketapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TypeExceptionCaught {

    private boolean ok;
    private String msg;
    private String source;


}
