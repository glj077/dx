package com.example.dx_4g.funclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.R;

import java.util.LinkedList;

//定义listView数据适配器
public class DXDeviceAdapter extends BaseAdapter {
    private Context mContext;
    private LinkedList linkDXDeice;
    public DXDeviceAdapter(LinkedList<DX_Device> linkDXDeice, Context mContext){
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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        DX_Device dx_device= (DX_Device) linkDXDeice.get(position);
        if (viewHolder==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.dx_device,parent,false);
            viewHolder=new ViewHolder();
            viewHolder.dx_name=(TextView)convertView.findViewById(R.id.DX_name);
            viewHolder.dx_ip=(TextView)convertView.findViewById(R.id.DX_IP);
            viewHolder.dx_icon=(ImageView)convertView.findViewById(R.id.DX_image);
            viewHolder.dx_status=(TextView)convertView.findViewById(R.id.DX_status);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
         viewHolder.dx_name.setText(dx_device.getDxName());
        viewHolder.dx_ip.setText(dx_device.getDxIp());
        viewHolder.dx_icon.setBackgroundResource(dx_device.getDxIcon());
        String dxOnline;
        if (dx_device.getDxStatus()==0){
            dxOnline="离线";
            viewHolder.dx_status.setTextColor(convertView.getResources().getColor(R.color.color_on_off));

        }
        else {
            dxOnline="在线";
            viewHolder.dx_status.setTextColor(convertView.getResources().getColor(R.color.color_online));
            viewHolder.dx_ip.setTextColor(convertView.getResources().getColor(R.color.color_online_text));
        }
        viewHolder.dx_status.setText(dxOnline);

       return convertView;
    }
}
