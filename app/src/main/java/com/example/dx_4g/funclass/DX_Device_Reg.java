package com.example.dx_4g.funclass;

public class DX_Device_Reg {
    private String regName;
    private int regIcon;
    private String regValue;
    public DX_Device_Reg(){}
    public DX_Device_Reg(String regName,String regValue,int regIcon){
        this.regName=regName;
        this.regValue=regValue;
        this.regIcon=regIcon;
    }
    String getRegName(){
        return regName;
    }
    String getRegValue(){
        return regValue;
    }
    int getRegIcon(){return regIcon;}
    public void setRegName(String regName){
        this.regName=regName;
    }
    public void  setRegValue(String regValue){
        this.regValue=regValue;
    }
    public void setRegIcon(int regIcon){this.regIcon=regIcon;}
}
