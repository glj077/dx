package com.example.dx_4g.funclass;

public class DX_Device_Alarm {
    private String alarmName;
    private String alarmCount;
    public DX_Device_Alarm(){}
    public DX_Device_Alarm(String alarmName,String alarmCount){
        this.alarmName=alarmName;
        this.alarmCount=alarmCount;
    }
    public String getAlarmName(){
        return alarmName;
    }
    public void setAlarmName(String alarmName){
        this.alarmName=alarmName;
    }
    public String getAlarmCount(){
        return alarmCount;
    }
    public void setAlarmCount(String alarmCount){
        this.alarmCount=alarmCount;
    }
}
