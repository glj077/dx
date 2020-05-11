package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;

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

public class Main2Activity extends BaseActivity implements View.OnClickListener {

    private static final int SEND_REQUEST = 1;
    private myApplication application;
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
        return R.layout.activity_main2;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        //((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setTextAlignment("center");
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).setTitle("设备");

        // 以下动作让标题居中显地
        TextView textView = (TextView)((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar1)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER_HORIZONTAL);//水平居中，CENTER，即水平也垂直


        //((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitle("副标题");
//        //((Toolbar)findViewById(R.id.toolbar)).setLogo(R.mipmap.ic_launcher);//设置标题LOGO
      // ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setSubtitleTextColor(Color.WHITE);//设置副标题文本颜色
 //      ((androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar1)).setNavigationIcon(R.mipmap.ic_launcher);//设置导航图标


    }



    @Override
    protected void initData() {
        HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com/devices", myApplication.getInstance().getPasbas64(),new HttpCallbackListener() {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu,menu);
        return true;
    }


    @Override
    public void onClick(View v) {

    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson=new Gson();
        List<DX_4G.DataBean> dataBeans=gson.fromJson(jsonArray.toString(),new TypeToken<List<DX_4G.DataBean>>() {}.getType());

        LinkedList<DX_Device> mData = new LinkedList<DX_Device>();
        ListView list1=(ListView)findViewById(R.id.dxlist);
        Context mContext=this;
        for (int i = 0; i< dataBeans.size(); i++){
            mData.add(new DX_Device("设备名称:"+dataBeans.get(i).getName(),"IP地址:"+dataBeans.get(i).getIp(),R.mipmap.earth_foreground));

        }
        DXDeviceAdapter DXAdapter=new DXDeviceAdapter((LinkedList<DX_Device>)mData,mContext);
        list1.setAdapter(DXAdapter);

    }
}


