package com.example.dx_4g.funclass;

import android.app.PendingIntent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtilPut {

        public static void sendHttpRequest(final String address,final String pasBase64,final String regValuejsonString,final HttpCallbackListener listener){
            new Thread(new Runnable() {
                @Override
                public void run() {

                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(address);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("PUT");
                        connection.addRequestProperty("Accept", "application/json");
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.addRequestProperty("Authorization", pasBase64);

                        connection.setReadTimeout(30000);
                        connection.setConnectTimeout(30000);
                        connection.setUseCaches(false);
                        connection.setChunkedStreamingMode(0);
                        //connection.setInstanceFollowRedirects(true);
                        //connection.setDoInput(false);
                        //connection.setDoOutput(false);
                        connection.connect();
                        OutputStream out=connection.getOutputStream();
                        out.write(regValuejsonString.getBytes());
                        out.flush();
                        out.close();
                        InputStream in = connection.getInputStream();

                        if (listener!=null){
                            listener.onFinish(connection.getResponseMessage(),connection.getResponseCode());

                        }


                    } catch (Exception e) {
                        if (listener!=null){
                            try {
                                listener.onError(connection.getResponseCode(),connection.getResponseMessage());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                                listener.onError(0,ex.toString());
                            }
                        }

                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            }).start();

        }
}
