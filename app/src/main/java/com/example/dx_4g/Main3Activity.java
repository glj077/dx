package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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


    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main3;
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {


        //****************************
        //采用自定义样式设定标签
        /*添加tab*/
        //****************************

        final TabHost tabHost=(TabHost)findViewById(R.id.tab_host);//获取TabHost控件
        tabHost.setup();
        for (int i = 0; i < 2; i++) {
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
//                case 1:
//                    tabviewtextView.setText("报表");
//                    tabviewtextView1.setVisibility(View.GONE);
//                    imageView.setImageResource(R.drawable.report1);
//                    tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(view).setContent(R.id.tab2));
//                    break;
                case 1:
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

                for(int i=0;i<2;i++){
                    ImageView imageView=(ImageView)tabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.tabview_icon);
                    switch (i){
                        case 0: imageView.setImageResource(R.drawable.reg1);
//                            break;
//                        case 1: imageView.setImageResource(R.drawable.report1);
                            break;
                        case 1: imageView.setImageResource(R.drawable.alarm1);
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
//                    case "tab2":
//                        ((ImageView)tabHost.getCurrentTabView().findViewById(R.id.tabview_icon)).setImageResource(R.drawable.report);
//                       tab2 tab2fragment = new tab2();
//                       fragmentTransaction.replace(R.id.fram,tab2fragment,"tag2");
//                       fragmentTransaction.commit();
//                        break;
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
        for(int i=0;i<2;i++){
            ImageView imageView=(ImageView)tabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.tabview_icon);
            switch (i){
                case 0: imageView.setImageResource(R.drawable.reg);
                    break;
//                case 1: imageView.setImageResource(R.drawable.report1);
//                    break;
                case 1: imageView.setImageResource(R.drawable.alarm1);
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


    //*****************************
    //浮点数转换为整数表示形式
    //*****************************
    private   static  String folatToHexString(Float value){
        return  Integer.toHexString(Float.floatToIntBits(value));
    }

}
