package com.example.dx_4g.funclass;

import android.app.Application;
import android.content.Context;
import android.net.IpSecManager;

import androidx.annotation.InspectableProperty;

public class myApplication extends Application {
    private  static  myApplication mApp;
    private static Context context;
    private  String pasbas64;
    private int deviceID;
    private int regID;
    private int valuetype;

    private String deviceName;
    private String querytime;

    public String getDeviceName(){return  deviceName;}
    public String getQuerytime(){return  querytime;}
    public  static myApplication getInstance(){
        return mApp;
    }
    public  String getPasbas64(){
        return pasbas64;
    }
    public   void setPasbas64(String pasbas64){
        this.pasbas64=pasbas64;
    }
    public int getDeviceID(){return deviceID;}
    public  void setDeviceID(int deviceID){this.deviceID=deviceID;}
    public void setQuerytime(String querytime){this.querytime=querytime;}
    public void setDeviceName(String deviceName){this.deviceName=deviceName;}
    public int getRegID(){return regID;}
    public void setRegID(int regID){this.regID=regID;}
    public  int getValuetype(){return valuetype;}
    public void setValuetype(int valuetype){this.valuetype=valuetype;}


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
