package com.example.dx_4g.funclass;

public interface HttpCallbackListener {
    void onFinish(String response);
    void  onError(Exception E);
}
