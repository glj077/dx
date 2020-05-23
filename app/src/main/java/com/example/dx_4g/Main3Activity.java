package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.dx_4g.funclass.RegCallBackListener;
import com.example.dx_4g.tab2;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.DXDeviceAdapter;
import com.example.dx_4g.funclass.DXDeviceRegAdapter;
import com.example.dx_4g.funclass.DX_4G;
import com.example.dx_4g.funclass.DX_4G_Reg;
import com.example.dx_4g.funclass.DX_Device;
import com.example.dx_4g.funclass.DX_Device_Reg;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
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

public class Main3Activity extends BaseActivity implements View.OnClickListener {
    private SearchView searchView;
    private static final int SEND_REQUEST = 2;
    private LinkedList<DX_Device_Reg> mDataReg;
    private List<DX_4G_Reg.DataBean> dataBeansReg;
    private DXDeviceRegAdapter DXAdapterReg;
    private ListView list2;
    private Context mContext;
    private int regID;

    //消息处理
    //@SuppressLint("HandlerLeak")
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//
//            if (msg.what == SEND_REQUEST) {
//                String response = (String) msg.obj;
//
//                try {
//                    parseJSONWITHGSON(response);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };



    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main3;
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {

        //mContext = this.getApplicationContext();

        //View view1=this.getLayoutInflater().inflate(R.id.tab1, null);
        //list2=(ListView)view1.findViewById(R.id.reg_list);


//        //标题栏设置
//        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2));
//        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
//        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2)).setTitle("设备");
//        // 以下动作让标题居中显地
//        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2)).getChildAt(0);//主标题
//        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
//        textView.setGravity(Gravity.CENTER);
//
//        //搜索栏设置
//        searchView = (SearchView) findViewById(R.id.search_view1);
//        searchView.setIconifiedByDefault(false);
//        searchView.setSubmitButtonEnabled(true);//增加提交按钮
//        // TextView textView1=searchView.findViewById(android.support.
//        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
////获取到searchview TextView的控件
//        TextView textView1 = (TextView) searchView.findViewById(id);
////设置字体大小为14sp
//        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp


        //标签栏设置
        //********************************************//

       //初始化，如果类继承TabActivity，则初始化不能使用这个指令
        //设置标签名称，标签名称和图片不能同时设置，如果不使用自定义格式，可通过以下方法设置标签名或图标
        //tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("本地音乐",null).setContent(R.id.tab1));
        //tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("网络音乐",null).setContent(R.id.tab2));
        //tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("在线音乐",null).setContent(R.id.tab3));

        //****************************
        //采用自定义样式设定标签
        /*添加tab*/
        //****************************

        final TabHost tabHost=(TabHost)findViewById(R.id.tab_host);//获取TabHost控件
        tabHost.setup();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.tabview, null, false);
            TextView tabviewtextView = (TextView) view.findViewById(R.id.tabveiw_text);
            ImageView imageView = (ImageView) view.findViewById(R.id.tabview_icon);
            TextView tabviewtextView1=(TextView)view.findViewById(R.id.icontext);

            switch (i) {
                case 0:
                    tabviewtextView.setText("寄存器");
                    tabviewtextView1.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.reg1);
                    tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(view).setContent(R.id.tab1));
                    break;
                case 1:
                    tabviewtextView.setText("报表");
                    tabviewtextView1.setVisibility(View.GONE);
                    imageView.setImageResource(R.drawable.report1);
                    tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(view).setContent(R.id.tab2));
                    break;
                case 2:
                    tabviewtextView.setText("报警");
                    imageView.setImageResource(R.drawable.alarm1);
                    tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(view).setContent(R.id.tab3));
                    break;

            }

        }

        //设置事件监听
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //以下为动态改变文本颜色，可以使用选择器来实现
                //全部恢复初始色

                for(int i=0;i<3;i++){
                    ImageView imageView=(ImageView)tabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.tabview_icon);
                    switch (i){
                        case 0: imageView.setImageResource(R.drawable.reg1);
                            break;
                        case 1: imageView.setImageResource(R.drawable.report1);
                            break;
                        case 2: imageView.setImageResource(R.drawable.alarm1);
                            break;

                    }

                }
//                if (tabHost.getCurrentTabTag()==tabId){//被选中改变颜色
//                    ((TextView)tabHost.getCurrentTabView().findViewById(R.id.tabveiw_text))
//                            .setTextColor(getResources().getColor(R.color.clolorTab));
//                }

                FragmentManager fragmentManager=getSupportFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                switch (tabId){

                    case "tab1":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.reg);

                        tab1 tab1fragment = new tab1();
                        fragmentTransaction.replace(R.id.fram,tab1fragment,"tag1");
                        fragmentTransaction.commit();
                        break;
                    case "tab2":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.report);
                       tab2 tab2fragment = new tab2();
                       fragmentTransaction.replace(R.id.fram,tab2fragment,"tag2");
                       fragmentTransaction.commit();
                        break;
                    case "tab3":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.alarm);
                        tab3 tab3fragment = new tab3();
                        fragmentTransaction.replace(R.id.fram,tab3fragment,"tag3");
                        fragmentTransaction.commit();
                        break;

                }
            }
        });

        //初次进入第一个TAB颜色
        for(int i=0;i<3;i++){
            ImageView imageView=(ImageView)tabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.tabview_icon);
            switch (i){
                case 0: imageView.setImageResource(R.drawable.reg);
                    break;
                case 1: imageView.setImageResource(R.drawable.report1);
                    break;
                case 2: imageView.setImageResource(R.drawable.alarm1);
                    break;

            }

        }


        //**********************************************//

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        tab1 tab1fragment = new tab1();
        fragmentTransaction.add(R.id.fram,tab1fragment,"tag1");
        fragmentTransaction.commit();

    }

    @Override
    protected void initData() {
        //********************************
        //读取设备ID,并读取网络数据
        //*******************************
       // int devicdID=myApplication.getInstance().getRegID();
        //readRegValue(devicdID);

    }

    @Override
    public void onClick(View v) {

    }

    //****************************
    //加载Toolbar工具栏中的POPUP菜单
    //****************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    //******************************
    //对读取回来的数据进行处理
    //******************************

//    private void parseJSONWITHGSON(String jsonData) throws JSONException {
//        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
//        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组
//
//        Gson gson = new Gson();
//        dataBeansReg = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G_Reg.DataBean>>() {
//        }.getType());
//        list2=findViewById(R.id.reg_list);
//        mDataReg = new LinkedList<DX_Device_Reg>();
//        RegValueHandle(dataBeansReg);
//         TextView regcount=(TextView)findViewById(R.id.reg_count);
//        regcount.setText(String.valueOf(dataBeansReg.size()));
//        DXAdapterReg = new DXDeviceRegAdapter((LinkedList<DX_Device_Reg>) mDataReg, mContext);
//
//        list2.setAdapter(DXAdapterReg);
//
//    }
    //******************************
    //对读取回来的数据做进一步的处理
    //******************************
//    private  void RegValueHandle(List<DX_4G_Reg.DataBean> RegValue){
//        mDataReg = new LinkedList<DX_Device_Reg>();
//
//        for(int i=0;i<RegValue.size();i++){
//            if(i==RegValue.size()-1){
//                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dataBeansReg.get(i).getValue()),0));
//                break;
//            }
//            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()==null)){
//                float dx_regvalue=intToFloat(dataBeansReg.get(i+1).getValue(),dataBeansReg.get(i).getValue());
//                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(),String.valueOf(dx_regvalue),0));
//                i=i+1;
//                continue;
//            }
//
//            if ((RegValue.get(i).getTemplate()!=null)&&(RegValue.get(i+1).getTemplate()!=null)) {
//                mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i).getName(), String.valueOf(dataBeansReg.get(i).getValue()), 0));
//                //mDataReg.add(new DX_Device_Reg(dataBeansReg.get(i+1).getName(),String.valueOf(dataBeansReg.get(i+1).getValue()),0));
//
//            }
//        }
//    }

    //****************************
    //整数值转换为浮点数表示形式
    //****************************
//    private static float intToFloat(int HValue,int LValue){
//        String Hhex= Integer.toHexString(HValue);
//        String Lhex=Integer.toHexString(LValue);
//        String hex=Hhex+Lhex;
//        return Float.intBitsToFloat(new BigInteger(hex, 16).intValue());
//    }


    //*****************************
    //浮点数转换为整数表示形式
    //*****************************
    private   static  String folatToHexString(Float value){
        return  Integer.toHexString(Float.floatToIntBits(value));
    }

    //*******************************
    //读取DX云端寄存器数据
    //********************************
//    private void readRegValue(int deviceID){
//        String webAddr="https://api.diacloudsolutions.com/devices/"+deviceID+"/regs";
//        HttpUtil.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) {
//                Message msg = Message.obtain();
//                msg.what = SEND_REQUEST;
//                msg.obj = response;
//                handler.sendMessage(msg);
//            }
//
//            @Override
//            public void onError(Exception E) {
//
//            }
//        });
//    }


}
