package com.example.dx_4g.funclass;

public class httpopenException extends Exception {
    private int httpcode;
    public httpopenException(int httpcode){
        this.httpcode=httpcode;
    }
    public int getHttpcode(){
        return httpcode;
    }
}
