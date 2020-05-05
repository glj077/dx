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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SHOW_RESPONSE = 0;
    private Button sendRequest;
    private TextView responseText;
    private  Button send;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            if (msg.what == SHOW_RESPONSE) {
                String response = (String) msg.obj;

                responseText.setText(response);
                try {
                    parseJSONWITHGSON(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_main);




    }

    @Override
    public void onClick(View v) {

        }


//    private void sendRequestWithHttpURLConnection() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                HttpURLConnection connection = null;
//                try {
//                    URL url = new URL("https://api.diacloudsolutions.com/devices");
//                    connection = (HttpURLConnection) url.openConnection();
//                    connection.setRequestMethod("GET");
//                    connection.addRequestProperty("Accept", "application/json");
//                    connection.addRequestProperty("Content-Type", "application/json");
//                    connection.addRequestProperty("Authorization", "Basic Z2xqMDc3QHFxLmNvbTpneXd3ODE5Mg==");
//                    connection.setConnectTimeout(8000);
//                    connection.setReadTimeout(8000);
//                    InputStream in = connection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
//                    Message message = new Message();
//                    message.what = SHOW_RESPONSE;
//                    message.obj = response.toString();
//                    handler.sendMessage(message);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (connection != null) {
//                        connection.disconnect();
//                    }
//                }
//            }
//        }).start();
//    }

    private void parseJSONWITHGSON(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);//将读回的字符串转换成JSON对象
        JSONArray jsonArray = jsonObject.getJSONArray("data");//获取名称data的JSON数组

        Gson gson=new Gson();
        List<DX_4G.DataBean> dataBeans=gson.fromJson(jsonArray.toString(),new TypeToken<List<DX_4G.DataBean>>() {}.getType());
          Log.d("glj077"," "+dataBeans.size());

        for (int i = 0; i< dataBeans.size(); i++){
            Log.d("glj"," "+dataBeans.get(i).getName());

        }

    }
}


