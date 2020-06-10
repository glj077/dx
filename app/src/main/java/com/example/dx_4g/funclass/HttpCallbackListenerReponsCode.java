package com.example.dx_4g.funclass;

public interface HttpCallbackListenerReponsCode {
    void onFinish(String response,int httpcode,int reponscode);
    void  onError(int httpcode,String httpmessage,int reponsecode);
}
