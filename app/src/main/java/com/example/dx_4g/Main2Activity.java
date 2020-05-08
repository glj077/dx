package com.example.dx_4g;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main2);
       //application=(myApplication)this.getApplication();



        //打开页面加载

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
    public void onClick(View v) {

    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson=new Gson();
        List<DX_4G.DataBean> dataBeans=gson.fromJson(jsonArray.toString(),new TypeToken<List<DX_4G.DataBean>>() {}.getType());

        //Log.d("glj077"," "+dataBeans.size());
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


