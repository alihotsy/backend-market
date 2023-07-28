package com.zapp.marketapp.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileExceptionCaught {

    private boolean ok;
    private String msg;
    private String source;


}
