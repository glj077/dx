package com.example.dx_4g.funclass;

public interface HttpCallbackListener {
    void onFinish(String response,int httpcode);
    void  onError(int httpcode,String httpmessage);

}
