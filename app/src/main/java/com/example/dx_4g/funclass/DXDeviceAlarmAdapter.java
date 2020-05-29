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

public class DXDeviceAlarmAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList linkDXDeice;

    public DXDeviceAlarmAdapter(LinkedList<DX_Device_Alarm> linkDXDeice, Context mContext){
        this.linkDXDeice=linkDXDeice;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        return linkDXDeice.size();
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
        ViewHolderAlarm viewHolder=null;
        DX_Device_Alarm dx_device_alarm= (DX_Device_Alarm) linkDXDeice.get(position);
        if (viewHolder==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.dx_alarm,parent,false);
            viewHolder=new ViewHolderAlarm();
            viewHolder.alarm_name=(TextView)convertView.findViewById(R.id.alarm_list_name);
            viewHolder.alarm_count=(TextView)convertView.findViewById(R.id.alarm_list_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderAlarm) convertView.getTag();
        }
        viewHolder.alarm_name.setText(dx_device_alarm.getAlarmName());
        viewHolder.alarm_count.setText(dx_device_alarm.getAlarmCount());
        return convertView;
    }
}
