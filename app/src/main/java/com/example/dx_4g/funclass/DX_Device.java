
//lsitview显示数据模板定义
package com.example.dx_4g.funclass;

public class DX_Device {
    private String dxName;
    private String dxIp;
    private int dxIcon;
    private int dxStatus;
    private int dxID;
    public DX_Device(){}
    public DX_Device(String dxName,String dxIp,int dxIcon,int dxStatus,int dxID){
        this.dxName=dxName;
        this.dxIp=dxIp;
        this.dxIcon=dxIcon;
        this.dxStatus=dxStatus;
        this.dxID=dxID;
    }
    public int getDxID(){return  dxID;}
    public String getDxName(){
        return dxName;
    }
    public String getDxIp(){
        return dxIp;
    }
    public int getDxIcon(){
        return dxIcon;
    }
    public int getDxStatus(){return dxStatus;}
    public void setDxName(String dxName){
        this.dxName=dxName;
    }
    public  void setDxIp(String dxIp){
        this.dxIp=dxIp;
    }
    public void  setDxIcon(int dxIcon){
        this.dxIcon=dxIcon;
    }
    public void setDxStatus(int dxStatus){this.dxStatus=dxStatus;}
    public void setDxID(int dxID){this.dxID=dxID;}
}
