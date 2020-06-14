//云端数据读取

package com.example.dx_4g.funclass;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.dx_4g.Main2Activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    private static int httpshowcode;
    private static String httpshowmessage;

    public static  int getHttpshowcode(){
        return httpshowcode;
    }
    public static String getHttpshowmessage(){
        return httpshowmessage;
    }
    public static void sendHttpRequest( final String address, final String pasBase64, final HttpCallbackListener listener)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.addRequestProperty("Accept", "application/json");
                    connection.addRequestProperty("Content-Type", "application/json");
                    connection.addRequestProperty("Authorization", pasBase64);
                    connection.setReadTimeout(30000);
                    connection.setConnectTimeout(30000);
                    connection.setUseCaches(false);
                    //connection.setDoInput(true);
                    //connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                   httpshowcode=connection.getResponseCode();
                   httpshowmessage=connection.getResponseMessage();
                    if (connection.getResponseCode() == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        if (listener != null) {
                            listener.onFinish(response.toString(),httpshowcode);

                        }
                    }
                    else{
                        if (listener != null) {
                            listener.onFinish(httpshowmessage,httpshowcode);
                        }

                    }

                    } catch(Exception e){
                        if (listener != null) {
                            try {
                                assert connection != null;
                                listener.onError(connection.getResponseCode(),connection.getResponseMessage());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                listener.onError(0,e.toString());
                            }
                        }

                    } finally{
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }

                }

        }).start();

        }
    }

