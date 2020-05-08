
//lsitview显示数据模板定义
package com.example.dx_4g;

public class DX_Device {
    private String dxName;
    private String dxIp;
    private int dxIcon;
    public DX_Device(){}
    public DX_Device(String dxName,String dxIp,int dxIcon){
        this.dxName=dxName;
        this.dxIp=dxIp;
        this.dxIcon=dxIcon;
    }
    public String getDxName(){
        return dxName;
    }
    public String getDxIp(){
        return dxIp;
    }
    public int getDxIcon(){
        return dxIcon;
    }
    public void setDxName(String dxName){
        this.dxName=dxName;
    }
    public  void setDxIp(String dxIp){
        this.dxIp=dxIp;
    }
    public void  setDxIcon(int dxIcon){
        this.dxIcon=dxIcon;
    }
}
