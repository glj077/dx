package com.example.dx_4g.funclass;


import android.app.Activity;
import android.util.Log;

import java.util.logging.Handler;

public class watchdog {
    private static int working;
    private  static Runnable runnable;
    public static void watchdogRun(final long watchTime, final watchdogCallbackListener listener){
        working=1;
        new android.os.Handler().postDelayed(runnable=new Runnable(){
            public void run() {

                if (working == 1) {
                    if (listener != null) {
                        listener.onWatchDogFinish(watchTime, "无网络或服务器无响应");
                    }

                }
            }
        }, watchTime);


    }
    public static void RemoveWatchDog(int work){
        //if(runnable!=null) {
           working=work;
            new android.os.Handler().removeCallbacks(runnable);
        //}
    }

}
