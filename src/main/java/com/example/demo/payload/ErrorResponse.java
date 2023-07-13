package com.example.demo.payload;

import java.util.Date;

public class ErrorResponse {
   public  Date date;

  public   String message;
    public ErrorResponse(String message){
            this.message=message;
            this.date=new Date();
    }


}
