package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.DXDeviceAdapter;
import com.example.dx_4g.funclass.DX_4G;
import com.example.dx_4g.funclass.DX_Device;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Main3Activity extends BaseActivity implements View.OnClickListener {
    private SearchView searchView;
    private static final int SEND_REQUEST = 2;
    private LinkedList<DX_Device> mData;
    private List<DX_4G.DataBean> dataBeans;
    private DXDeviceAdapter DXAdapter;
    private ListView list2;
    private Context mContext;

    //消息处理
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



    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main3;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {

        list2 = (ListView) findViewById(R.id.reglist);
        mContext = this;

        //标题栏设置
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2)).setTitle("设备");
        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar2)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);

        //搜索栏设置
        searchView = (SearchView) findViewById(R.id.search_view1);
        searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);//增加提交按钮
        // TextView textView1=searchView.findViewById(android.support.
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
//获取到searchview TextView的控件
        TextView textView1 = (TextView) searchView.findViewById(id);
//设置字体大小为14sp
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);//14sp


        //标签栏设置
        //********************************************//

        final TabHost tabHost=(TabHost)findViewById(R.id.tab_host);//获取TabHost控件
        tabHost.setup();//初始化，如果类继承TabActivity，则初始化不能使用这个指令
        //设置标签名称，标签名称和图片不能同时设置，如果不使用自定义格式，可通过以下方法设置标签名或图标
        //tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("本地音乐",null).setContent(R.id.tab1));
        //tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("网络音乐",null).setContent(R.id.tab2));
        //tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("在线音乐",null).setContent(R.id.tab3));

        //采用自定义样式设定标签
        /*添加tab*/
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
                switch (tabId){

                    case "tab1":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.reg);

                        break;
                    case "tab2":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.report);

                        break;
                    case "tab3":
                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.alarm);

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
    }

    @Override
    protected void initData() {
        //********************************
        //读取设备ID,并读取网络数据
        //*******************************
        Intent intent=getIntent();
        int deviceID=intent.getIntExtra("deviceID",0);
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

        //*******************************

    }

    @Override
    public void onClick(View v) {


            Toast.makeText(Main3Activity.this,"1111",Toast.LENGTH_LONG).show();
    }

    //加载工具栏中的POPUP菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson = new Gson();
        dataBeans = gson.fromJson(jsonArray.toString(), new TypeToken<List<DX_4G.DataBean>>() {
        }.getType());

        mData = new LinkedList<DX_Device>();
        for (int i = 0; i < dataBeans.size(); i++) {
            mData.add(new DX_Device("设备名称:" + dataBeans.get(i).getName(), "IP地址:" + dataBeans.get(i).getIp(), R.mipmap.earth_foreground,dataBeans.get(i).getOnline()));
            if (dataBeans.get(i).getOnline()==1){


            }

        }
        DXAdapter = new DXDeviceAdapter((LinkedList<DX_Device>) mData, mContext);

        list2.setAdapter(DXAdapter);



    }
}
