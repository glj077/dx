package com.example.dx_4g.funclass;

public class DX_Device_Report {
    private String reportTime;
    private int reportValue;

    public DX_Device_Report(String reportTime,int reportValue){
        this.reportTime=reportTime;
        this.reportValue=reportValue;
    }

    public String getReportTime(){return reportTime;}
    public int getReportValue(){return reportValue;}

    public void setReportTime(String reportTime){this.reportTime=reportTime;}
    public void setReportValue(int reportValue){this.reportValue=reportValue;}

}
