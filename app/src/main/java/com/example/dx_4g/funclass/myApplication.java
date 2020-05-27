package com.example.dx_4g.funclass;

import android.app.Application;
import android.content.Context;
import android.net.IpSecManager;

public class myApplication extends Application {
    private  static  myApplication mApp;
    private static Context context;
    private  String pasbas64;
    private int regID;
    public  static myApplication getInstance(){
        return mApp;
    }
    public  String getPasbas64(){
        return pasbas64;
    }
    public   void setPasbas64(String pasbas64){
        this.pasbas64=pasbas64;
    }
    public int getRegID(){return regID;}
    public  void setRegID(int regID){this.regID=regID;}


    @Override
    public void onCreate() {

        super.onCreate();
        mApp=this;
        myApplication.context=getApplicationContext();
    }
    public static Context getContext(){
        return myApplication.context;
    }
}
