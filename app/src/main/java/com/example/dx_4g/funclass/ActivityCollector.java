package com.example.dx_4g.funclass;

import android.app.Activity;
import android.net.wifi.aware.PublishConfig;

import java.util.ArrayList;
import java.util.List;

public class ActivityCollector {
 public static List<Activity> activitys=new ArrayList<Activity>();
 public static void addActivity(Activity activity){
 activitys.add(activity);
 }
 public static void removeActivity(Activity activity){
     activitys.remove(activity);
 }
    public static void killAllActivity(){
        for (Activity activity : activitys){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}
