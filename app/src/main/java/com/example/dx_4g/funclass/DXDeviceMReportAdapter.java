package com.example.dx_4g.funclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dx_4g.R;

import java.util.ArrayList;

public class DXDeviceMReportAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList linkDXDeviceReport;

    public DXDeviceMReportAdapter(ArrayList<DX_Device_mReport> linkDXDeviceReport, Context mContext) {
        this.linkDXDeviceReport = linkDXDeviceReport;
        this.mContext = mContext;
    }



    @Override
    public int getCount() {
        return linkDXDeviceReport.size();
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
        ViewHolderReport viewHolder=null;
        DX_Device_mReport dx_device_report= (DX_Device_mReport) linkDXDeviceReport.get(position);
        if (viewHolder==null){
            convertView= LayoutInflater.from(mContext).inflate(R.layout.dx_report,parent,false);
            viewHolder=new ViewHolderReport();
            viewHolder.report_name=(TextView)convertView.findViewById(R.id.report_list_name);
            viewHolder.report_count=(TextView)convertView.findViewById(R.id.report_list_count);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolderReport) convertView.getTag();
        }
        viewHolder.report_name.setText(dx_device_report.getReportTime());
        viewHolder.report_count.setText(String.valueOf(dx_device_report.getReportValue()));

        if (position%2==0) {
            //convertView.setBackgroundResource(R.color.color_list1);
            ((TextView) convertView.findViewById(R.id.report_list_name)).setTextColor(convertView.getResources().getColor(R.color.color_list1));
            ((TextView) convertView.findViewById(R.id.report_list_count)).setTextColor(convertView.getResources().getColor(R.color.color_list1));
        }else{
            //convertView.setBackgroundResource(R.color.color_list2);
            ((TextView) convertView.findViewById(R.id.report_list_name)).setTextColor(convertView.getResources().getColor(R.color.color_list2));
            ((TextView) convertView.findViewById(R.id.report_list_count)).setTextColor(convertView.getResources().getColor(R.color.color_list2));
        }
        return convertView;
    }
}
