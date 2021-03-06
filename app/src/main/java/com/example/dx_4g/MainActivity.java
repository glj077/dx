package com.example.dx_4g;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtil;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.myToast;
import com.example.dx_4g.funclass.watchdogCallbackListener;

import android.util.Base64;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final int SEND_REQUEST=1;
    private static final int SEND_REQUEST_ERR=2;
    private static final int WATCHDOG_FINISH=3;
    private EditText user;
    private EditText pas;
    private ProgressBar progressBar;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private  CheckBox userCheckBox;
    private CheckBox pasCheckBox;
    private Runnable runnable;


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(final Message msg) {

            if (msg.what == SEND_REQUEST) {
                if (msg.arg1==200) {
                    RemoveWatchDog(handler,runnable);
                    final Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                        }
                    }, 2000);

                }
                else{
                    RemoveWatchDog(handler,runnable);
                    progressBar.setVisibility(View.GONE);
                    Toast mytoast=Toast.makeText(MainActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();

                }

            }
            if (msg.what==SEND_REQUEST_ERR){
                RemoveWatchDog(handler,runnable);
                //rewatch(handler,runnable);
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        myToast mytoast=new myToast(getBaseContext(),R.layout.toast_style,R.id.toasttext,R.string.toast_dlerror);
                        mytoast.show(Gravity.CENTER,0,190);

                        //Toast.makeText(MainActivity.this,"code:"+msg.arg1+"  用户名或密码错误",Toast.LENGTH_SHORT).show();
                    }
                }, 2000);


            }
            if(msg.what==WATCHDOG_FINISH){
                RemoveWatchDog(handler,runnable);
                progressBar.setVisibility(View.GONE);
                Toast mytoast=Toast.makeText(MainActivity.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }
        }
    };





    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        user=(EditText)findViewById(R.id.user);
        pas=(EditText)findViewById(R.id.pas);
         userCheckBox = (CheckBox) findViewById(R.id.checkBox_use);
         pasCheckBox = (CheckBox) findViewById(R.id.checkBox_pas);
        progressBar=(ProgressBar)findViewById(R.id.progressBar2);
        pref=getPreferences(MODE_PRIVATE);


        //登录用户名和密码保存处理
        boolean isRememberPas=pref.getBoolean("remember_pas",false);
        boolean isRememberUser=pref.getBoolean("remember_user",false);
        if(isRememberUser){
            String username=pref.getString("user","");
            user.setText(username);
            userCheckBox.setChecked(true);
        }

        if(isRememberPas){
            String userpas=pref.getString("pas","");
            pas.setText(userpas);
            pasCheckBox.setChecked(true);
        }


    }

    @Override
    protected void initData() {

    }

    protected void onDestroy() {
        super.onDestroy();
        RemoveWatchDog(handler,runnable);
        handler.removeCallbacksAndMessages(MainActivity.this);
        //watchdog.RemoveWatchDog();
    }

    @Override
    protected void onStop() {

        super.onStop();
        finish();
    }

    @Override
    public void onClick(View v) {
        String account=user.getText().toString();
        String password=pas.getText().toString();
        if(isEmail(account)) {
            if ((!user.getText().toString().isEmpty()) && (!pas.getText().toString().isEmpty())) {
                String str = user.getText() + ":" + pas.getText();
                String strBase64 = "Basic " + Base64.encodeToString(str.getBytes(), Base64.DEFAULT);//计算BASE64位加密
                myApplication.getInstance().setPasbas64(strBase64);
                user.clearFocus();
                pas.clearFocus();
                editor = pref.edit();


                if (userCheckBox.isChecked()) {//检晒复选框是否被选中
                    editor.putBoolean("remember_user", true);
                    editor.putString("user", account);
                } else {
                    editor.putBoolean("remember_user", false);
                    editor.putString("user", "");
                }
                if (pasCheckBox.isChecked()) {//检晒复选框是否被选中
                    editor.putBoolean("remember_pas", true);
                    editor.putString("pas", password);
                } else {
                    editor.putBoolean("remember_pas", false);
                    editor.putString("pas", "");
                }
                editor.apply();

                watchdog(handler,runnable,15000, new watchdogCallbackListener() {
                    @Override
                    public void onWatchDogFinish(long code, String message) {
                        Message msg = Message.obtain();
                        msg.what = WATCHDOG_FINISH;
                        msg.arg1 = (int) (code);
                        msg.obj = message;
                        handler.sendMessage(msg);
                    }

                });


                //输入法处理
                InputMethodManager imm = (InputMethodManager) getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
                boolean isOpen = imm.isActive();
                if (isOpen) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                progressBar.setVisibility(View.VISIBLE);
                HttpUtil.sendHttpRequest("https://api.diacloudsolutions.com.cn/devices", myApplication.getInstance().getPasbas64(), new HttpCallbackListener() {

                    @Override
                    public void onFinish(String response, int httpcode) {

                        Message msg = Message.obtain();
                        msg.what = SEND_REQUEST;
                        msg.arg1 = httpcode;
                        msg.obj=response;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(int httpcode, String httpmessage) {
                        Message msg = Message.obtain();
                        msg.what = SEND_REQUEST_ERR;
                        msg.arg1 = httpcode;
                        msg.obj = httpmessage;
                        handler.sendMessage(msg);
                    }
                });
            } else {

                myToast mytoast = new myToast(getBaseContext(), R.layout.toast_style, R.id.toasttext, R.string.toast_dlshow);
                mytoast.show(Gravity.CENTER, 0, 190);
            }
        }else
        {
            Toast mytoast=Toast.makeText(MainActivity.this,R.string.user_format,Toast.LENGTH_LONG);
            mytoast.setGravity(Gravity.CENTER,0,190);
            mytoast.show();
        }

        }

    /**
     * 判断邮箱是否合法
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");//复杂匹配
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private void watchdog(Handler handler,Runnable runnable,final long watchtime, final watchdogCallbackListener listener){
        handler.postDelayed(runnable=new Runnable(){
            public void run() {


                    if (listener != null) {
                        listener.onWatchDogFinish(watchtime, "无网络或服务器无响应");
                    }


            }
        }, watchtime);
    }


    private void RemoveWatchDog(Handler handler,Runnable runnable){
        handler.removeCallbacksAndMessages(runnable);
    }
}


