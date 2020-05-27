package com.example.dx_4g.funclass;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(getLayoutId());
       ActivityCollector.addActivity(this);
       //setSupportTollBar();
       initView();

       try {
           initData();
       } catch (httpopenException e) {
           e.printStackTrace();
       }

   }
   protected abstract int getLayoutId();
   protected abstract void initView();
   protected abstract void initData() throws httpopenException;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}
