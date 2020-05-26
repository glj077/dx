package com.example.dx_4g.funclass;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
                        connection.setRequestMethod("put");
                        connection.addRequestProperty("Accept", "application/json");
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.addRequestProperty("Authorization", pasBase64);
                        connection.setReadTimeout(80000);
                        connection.setConnectTimeout(80000);
                        //connection.setInstanceFollowRedirects(true);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        if (listener!=null){
                            listener.onFinish(response.toString());

                        }


                    } catch (Exception e) {
                        if (listener!=null){
                            listener.onError(e);
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
