package com.example.dx_4g;

import android.app.Application;

public class myApplication extends Application {
    private  String pasbas64;
    public  String getPasbas64(){
        return pasbas64;
    }
    public   void setPasbas64(String pasbas64){
        this.pasbas64=pasbas64;
    }
}
