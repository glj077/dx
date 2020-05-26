package com.example.dx_4g;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;

import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int SEND_REQUEST=1;
    private static final int SEND_REQUEST_ERR=2;
    private EditText user;
    private EditText pas;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == SEND_REQUEST) {
                Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
            if (msg.what==SEND_REQUEST_ERR){
                Toast.makeText(MainActivity.this,"code:"+msg.arg1+"  用户名或密码错误",Toast.LENGTH_SHORT).show();
            }
        }
    };





    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        user=(EditText)findViewById(R.id.user);
        pas=(EditText)findViewById(R.id.pas);

    }

    @Override
    protected void initData() {

    }
    @Override
    protected void onStop() {

        super.onStop();
        finish();
    }

    @Override
    public void onClick(View v) {
        String str=user.getText()+":"+pas.getText();
        String strBase64 = "Basic "+Base64.encodeToString(str.getBytes(), Base64.DEFAULT);//计算BASE64位加密
        myApplication.getInstance().setPasbas64(strBase64);

        HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com/devices", myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {

            @Override
            public void onFinish(String response,int httpcode) {
                if (httpcode==200) {
                    Message msg=Message.obtain();
                    msg.what=SEND_REQUEST;
                    msg.arg1=httpcode;
                    handler.sendMessage(msg);

                }

            }

            @Override
            public void onError(int httpcode,String httpmessage) {
                Message msg = Message.obtain();
                msg.what = SEND_REQUEST_ERR;
                msg.arg1=httpcode;
                handler.sendMessage(msg);
            }
        });

        }
}


