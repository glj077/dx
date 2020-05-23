package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.DXDeviceRegAdapter;
import com.example.dx_4g.funclass.DX_4G_Reg;
import com.example.dx_4g.funclass.DX_Device_Reg;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.RegCallBackListener;
import com.example.dx_4g.funclass.myApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class tab1 extends Fragment {
   private SearchView searchView;
   private ListView listView;
   private static int regPosition;
    private static final int SEND_REQUEST = 3;

    private LinkedList<DX_Device_Reg> mDataReg;
    private List<DX_4G_Reg.DataBean> dataBeansReg;
    private DXDeviceRegAdapter DXAdapterReg;
    private ListView list2;
    private Context mContext;
    private TextView regCount;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tab1,container,false);
        listView=(ListView)view.findViewById(R.id.reg_list);
        regCount=(TextView)view.findViewById(R.id.reg_count);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView textView=view.findViewById(position).findViewById(R.id.regName);
                regPosition=position;

            }
        });

        androidx.appcompat.widget.Toolbar toolbar =(Toolbar)view.findViewById(R.id.toolbar2);
        toolbar.setTitle("设备");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        //Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title

        // 以下动作让标题居中显地
        TextView textView = (TextView) toolbar.getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);

        searchView = (SearchView) view.findViewById(R.id.search_view1);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
        // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
//获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
//设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                    case R.id.action_test:
                        //Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                return true;
            }
        });

        int devicdID= myApplication.getInstance().getRegID();
        readRegValue(devicdID);




        return view;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == SEND_REQUEST) {
                String response = (String) msg.obj;

                try {
                    parseJSONWITHGSON(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void readRegValue(int deviceID){
        String webAddr="https://api.diacloudsolutions.com/devices/"+deviceID+"/regs";
        HttpUtil.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST;
                msg.obj = response;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception E) {

            }
        });
    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson = new Gson();
        dataBeansReg = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G_Reg.DataBean>>() {
        }.getType());
        mDataReg = new LinkedList<DX_Device_Reg>();
        RegValueHandle(dataBeansReg);
        regCount.setText(String.valueOf(dataBeansReg.size()));
        mContext = this.getContext();
        DXAdapterReg = new DXDeviceRegAdapter((LinkedList<DX_Device_Reg>) mDataReg, mContext);

        listView.setAdapter(DXAdapterReg);

    }

    private  void RegValueHandle(List<DX_4G_Reg.DataBean> RegValue){
        mDataReg = new LinkedList<DX_Device_Reg>();

        for(int i=0;i<RegValue.size();i++){
            if(i==RegValue.size()-1){
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dataBeansReg.get(i).getValue()),0));
                break;
            }
            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()==null)){
                float dx_regvalue=intToFloat(dataBeansReg.get(i+1).getValue(),dataBeansReg.get(i).getValue());
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dx_regvalue),0));
                i=i+1;
                continue;
            }

            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()!=null)) {
                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(), String.valueOf(dataBeansReg.get(i).getValue()), 0));
                //mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i+1).getName(),String.valueOf(dataBeansReg.get(i+1).getValue()),0));

            }
        }
    }

    private static float intToFloat(int HValue,int LValue){
        String Hhex= Integer.toHexString(HValue);
        String Lhex=Integer.toHexString(LValue);
        String hex=Hhex+Lhex;
        return Float.intBitsToFloat(new BigInteger(hex, 16).intValue());
    }


    public  static  void setListPosition(RegCallBackListener regCallBackListener){
        if (regCallBackListener!=null){
             regCallBackListener.setRegPosition(regPosition);
        }

   }


}
