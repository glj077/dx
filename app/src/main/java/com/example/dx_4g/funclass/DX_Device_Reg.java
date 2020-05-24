package com.example.dx_4g.funclass;

public class DX_Device_Reg {
    private String regName;
    private int regIcon;
    private String regValue;
    private int regAddr;
    private int regsign;//0:整数；1：浮点数
    public DX_Device_Reg(){}
    public DX_Device_Reg(String regName,String regValue,int regAddr,int regIcon,int regsign){
        this.regName=regName;
        this.regValue=regValue;
        this.regIcon=regIcon;
        this.regAddr=regAddr;
        this.regsign=regsign;
    }
    public String getRegName(){
        return regName;
    }
    public String getRegValue(){
        return regValue;
    }
    public int getRegIcon(){return regIcon;}
    public int getRegAddr(){return regAddr;}
    public int getRegsign(){return regsign;}
    public void setRegName(String regName){
        this.regName=regName;
    }
    public void  setRegValue(String regValue){
        this.regValue=regValue;
    }
    public void setRegIcon(int regIcon){this.regIcon=regIcon;}
    public void setRegAddr(int regAddr){this.regAddr=regAddr;}
    public void setRegsign(int regsign){this.regsign=regsign;}
}
