package com.example.dx_4g.funclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dx_4g.R;

import java.util.LinkedList;

public class DXDeviceRegAdapter extends BaseAdapter {
    private Context mContext;
    private LinkedList linkDXDeviceReg;

    public DXDeviceRegAdapter(LinkedList<DX_Device_Reg> linkDXDeviceReg, Context mContext) {
        this.linkDXDeviceReg = linkDXDeviceReg;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        return linkDXDeviceReg.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderReg viewHolder=null;
        DX_Device_Reg dx_device_reg= (DX_Device_Reg) linkDXDeviceReg.get(position);
        if (viewHolder==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.dx_reg,parent,false);
            viewHolder=new ViewHolderReg();
            viewHolder.reg_name=(TextView)convertView.findViewById(R.id.regName);
            viewHolder.reg_value=(TextView)convertView.findViewById(R.id.regValue);
            viewHolder.reg_icon=(ImageView)convertView.findViewById(R.id.regIcon);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderReg) convertView.getTag();
        }
        viewHolder.reg_name.setText(dx_device_reg.getRegName());
        viewHolder.reg_value.setText(dx_device_reg.getRegValue());
        viewHolder.reg_icon.setBackgroundResource(dx_device_reg.getRegIcon());
        return convertView;
    }
}