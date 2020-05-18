package com.example.dx_4g;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.myApplication;

import android.util.Base64;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private EditText user;
    private EditText pas;

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
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);


        }
}


