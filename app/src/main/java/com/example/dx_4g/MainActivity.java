package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.util.Base64;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user;
    private EditText pas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);
        user=(EditText)findViewById(R.id.user);
        pas=(EditText)findViewById(R.id.pas);



    }

    @Override
    public void onClick(View v) {
        String str=user.getText()+":"+pas.getText();
        String strBase64 = "Basic "+Base64.encodeToString(str.getBytes(), Base64.DEFAULT);//计算BASE64位加密
        Toast.makeText(MainActivity.this,strBase64,Toast.LENGTH_LONG).show();
        }
}


