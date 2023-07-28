package com.zapp.marketapp.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileException extends RuntimeException{
   private boolean ok;
   private String msg;
   private String source;

   private int status;

   public FileException(String msg,int status) {
       super(msg);
       this.msg = msg;
       this.status = status;
   }
}
