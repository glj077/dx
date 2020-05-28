package com.example.dx_4g;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.myToast;

import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int SEND_REQUEST=1;
    private static final int SEND_REQUEST_ERR=2;
    private EditText user;
    private EditText pas;
    private ProgressBar progressBar;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(final Message msg) {
            progressBar.setVisibility(View.VISIBLE);
            if (msg.what == SEND_REQUEST) {
                if (msg.arg1==200) {
                    final Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                        }
                    }, 2000);

                }

            }
            if (msg.what==SEND_REQUEST_ERR){
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        myToast mytoast=new myToast(getBaseContext(),R.layout.toast_style,R.id.toasttext,R.string.toast_dlerror);
                        mytoast.show(Gravity.CENTER,0,190);

                        //Toast.makeText(MainActivity.this,"code:"+msg.arg1+"  用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }, 2000);


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
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);

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
        if ((!user.getText().toString().isEmpty())&&(!pas.getText().toString().isEmpty())) {
            String str = user.getText() + ":" + pas.getText();
            String strBase64 = "Basic " + Base64.encodeToString(str.getBytes(), Base64.DEFAULT);//计算BASE64位加密
            myApplication.getInstance().setPasbas64(strBase64);
            user.clearFocus();
            pas.clearFocus();
            //输入法处理
            InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
            boolean isOpen = imm.isActive();
            if (isOpen) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com/devices", myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {

                @Override
                public void onFinish(String response, int httpcode) {

                    Message msg = Message.obtain();
                    msg.what = SEND_REQUEST;
                    msg.arg1 = httpcode;
                    handler.sendMessage(msg);
                }

                @Override
                public void onError(int httpcode, String httpmessage) {
                    Message msg = Message.obtain();
                    msg.what = SEND_REQUEST_ERR;
                    msg.arg1 = httpcode;
                    msg.obj = httpmessage;
                    handler.sendMessage(msg);
                }
            });
        }else {

            myToast mytoast=new myToast(getBaseContext(),R.layout.toast_style,R.id.toasttext,R.string.toast_dlshow);
            mytoast.show(Gravity.CENTER,0,190);
        }

        }
}


