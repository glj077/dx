package com.example.dx_4g;

public interface HttpCallbackListener {
    void onFinish(String response);
    void  onError(Exception E);
}
