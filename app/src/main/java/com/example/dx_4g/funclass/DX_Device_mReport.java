package com.example.dx_4g.funclass;

public class DX_Device_mReport {

    private String reportTime;
    private float reportValue;

    public DX_Device_mReport(String reportTime,float reportValue){
        this.reportTime=reportTime;
        this.reportValue=reportValue;
    }

    public String getReportTime(){return reportTime;}
    public float getReportValue(){return reportValue;}

    public void setReportTime(String reportTime){this.reportTime=reportTime;}
    public void setReportValue(float reportValue){this.reportValue=reportValue;}
}
