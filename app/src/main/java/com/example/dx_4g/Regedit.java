package com.example.dx_4g;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dx_4g.funclass.ActivityCollector;
import com.example.dx_4g.funclass.BaseActivity;
import com.example.dx_4g.funclass.HttpCallbackListener;
import com.example.dx_4g.funclass.HttpUtilPut;
import com.example.dx_4g.funclass.JsonUtil;
import com.example.dx_4g.funclass.RegValue;
import com.example.dx_4g.funclass.myApplication;
import com.example.dx_4g.funclass.watchdogCallbackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class Regedit extends BaseActivity implements View.OnClickListener {

    private static final int SEND_QUEST=1;
    private static final int SEND_QUEST_ERR=2;
    private static final int WATCHDOG_FINISH=3;

    private  int regAddr;
    private  int regType;
    private  String regName;
    private  String regValue;
    private  String regValuejsonString;
    private  EditText userValueEdit;
    private Toolbar toolbar;
    private Runnable runnable;
    private ImageView reportimage;

    /********************************************/
    //消息处理
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressBar5);
            progressBar.setVisibility(View.VISIBLE);
            if (msg.what == SEND_QUEST) {

                try {
                    if (msg.arg1==202) {
                        RemoveWatchDog(handler,runnable);
                        int deviceID=myApplication.getInstance().getDeviceID();
                        final Intent intent=new Intent(Regedit.this,Main3Activity.class);
                        intent.putExtra("deviceID",deviceID);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                startActivity(intent);

                            }
                        }, 2000);


                    }
                    else{
                        RemoveWatchDog(handler,runnable);
                        progressBar.setVisibility(View.GONE);
                        Toast mytoast=Toast.makeText(Regedit.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                        mytoast.setGravity(Gravity.CENTER,0,190);
                        mytoast.show();
                    }

                } catch (Exception e) {
                    RemoveWatchDog(handler,runnable);
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                    Toast mytoast=Toast.makeText(Regedit.this,"Code:0"+" Message:"+e.toString(),Toast.LENGTH_LONG);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();
                }
            }
            if (msg.what==SEND_QUEST_ERR){
                RemoveWatchDog(handler,runnable);
                progressBar.setVisibility(View.GONE);
                Toast mytoast=Toast.makeText(Regedit.this,"code:"+msg.arg1+" "+(String)msg.obj,Toast.LENGTH_SHORT);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }

            if(msg.what==WATCHDOG_FINISH){
                RemoveWatchDog(handler,runnable);
                progressBar.setVisibility(View.GONE);
                Toast mytoast=Toast.makeText(Regedit.this,"Code:"+msg.arg1+" Message:"+(String) msg.obj,Toast.LENGTH_LONG);
                mytoast.setGravity(Gravity.CENTER,0,190);
                mytoast.show();
            }

        }
    };
/**********************************************************/

/*********************************************************/
//加载界面

    @Override
    protected int getLayoutId() {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        return R.layout.activity_regedit;
    }
    /*********************************************************/

    /*********************************************************/
    //初始化
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        userValueEdit=(EditText)findViewById(R.id.editvalueshow);
        reportimage=(ImageView)findViewById(R.id.report_image);

        /************************************/
        //标题栏设置
        setSupportActionBar((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);//隐藏默认的Title
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setTitle("设备:"+myApplication.getInstance().getDeviceName());
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationIcon(R.drawable.rego);//设置导航图标

        // 以下动作让标题居中显地
        TextView textView = (TextView) ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).getChildAt(0);//主标题
        textView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;//填充父类
        textView.setGravity(Gravity.CENTER);
        /************************************/

        /************************************/
        //ToolBarr 控件的溢出菜单按钮监中事件
        ((androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar3)).setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /****************************************/

        /************************************/
        //注册按钮事件
        Button exitok=(Button)findViewById(R.id.exitok);
        Button exitcancel=(Button)findViewById(R.id.exitcancel);
        exitok.setOnClickListener(this);
        exitcancel.setOnClickListener(this);
        /****************************************/

        /****************************************/
        //添加EDITTEXT输入监听
        userValueEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                String editinput=s.toString();

                    switch (regType) {
                        case 0:
                            if (isInteger(editinput)) {
                                if (Integer.parseInt(editinput) < -32768) {
                                    userValueEdit.setText("-32768");
                                }
                                if (Integer.parseInt(editinput) > 32767) {
                                    userValueEdit.setText("32767");
                                }
                            }

                            break;

                    }
            }
        });

        /**
        注册图片点击事件
         */
        reportimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent intent=new Intent(Regedit.this,ReportActivity.class);
             startActivity(intent);
            }
        });



        /**************************************/
        //ToolBar点击事件监听
        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_exit:
                        ActivityCollector.killAllActivity();
                        break;
                    case R.id.action_test:
                        //Intent intent=new Intent(Main4Activity.this,Main3Activity.class);
                        //startActivity(intent);
                        //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
                return true;
            }
        });
    }
    /****************************************/

    /*********************************************************/
    //数据初始化
    @Override
    protected void initData() {

        regAddr=getIntent().getIntExtra("regaddr",0);
        regType=getIntent().getIntExtra("regaddrtype",0);
        regName=getIntent().getStringExtra("regname");
        regValue=getIntent().getStringExtra("regvalue");
        myApplication.getInstance().setRegID(regAddr);
        myApplication.getInstance().setValuetype(regType);
        TextView textView=(TextView)findViewById(R.id.editaddrshow);
        textView.setText(regName);
        userValueEdit.setHint(regValue);

    }
    /*****************************************/
    //按钮事件响应
    @Override
    public void onClick(View v) {
         String svvalue=userValueEdit.getText().toString();
         int DeviceID=myApplication.getInstance().getDeviceID();
        switch (v.getId()){
            case R.id.exitok:
                if (!svvalue.isEmpty()){
                        if(((regType==0)&&(isInteger(svvalue)))||((regType==1)&&(isDouble(svvalue)))){
                            regValuejsonString=JsonString(regAddr, regType, svvalue);


                            watchdog(handler,runnable,15000, new watchdogCallbackListener() {
                                @Override
                                public void onWatchDogFinish(long code, String message) {
                                    Message msg = Message.obtain();
                                    msg.what = WATCHDOG_FINISH;
                                    msg.arg1 =(int)(code);
                                    msg.obj=message;
                                    handler.sendMessage(msg);
                                }

                            });





                             String webAddr="https://api.diacloudsolutions.com.cn/devices/"+DeviceID+"/regs";
                             HttpUtilPut.sendHttpRequest(webAddr, myApplication.getInstance().getPasbas64(), regValuejsonString,new HttpCallbackListener() {
                                        @Override
                                        public void onFinish(String response, int httpcode) {
                                            Message msg=Message.obtain();
                                            msg.what=SEND_QUEST;
                                            msg.arg1=httpcode;
                                            msg.obj=(Object)response;
                                           handler.sendMessage(msg);
                                        }

                                        @Override
                                        public void onError(int httpcode, String httpmessage) {
                                            Message msg=Message.obtain();
                                            msg.what=SEND_QUEST;
                                            msg.arg1=httpcode;
                                            msg.obj=(Object)httpmessage;
                                            handler.sendMessage(msg);
                                        }
                                    });

                    }
                        else{

                            Toast mytoast=Toast.makeText(Regedit.this,"数值类型错误!",Toast.LENGTH_SHORT);
                            mytoast.setGravity(Gravity.CENTER,0,190);
                            mytoast.show();
                        }
                }else
                {
                    Toast mytoast=Toast.makeText(Regedit.this,"数值不能为空!",Toast.LENGTH_SHORT);
                    mytoast.setGravity(Gravity.CENTER,0,190);
                    mytoast.show();
                }

                break;
            case R.id.exitcancel:
                finish();
                break;

        }

    }
    /*****************************************/

    /****************************************/
    //加载Toolbar的溢出菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }
    /*****************************************/

    /*****************************************/
    //生成JSON格式的字符串
    private String JsonString(int regAddr_F,int regType_F,String regValue_SV_F){
        String jsonString;
        List<RegValue> list = new ArrayList<>() ;
       switch (regType_F){
           case 0:
               RegValue rV=new RegValue();
               rV.setReg_addr(regAddr_F);
               rV.setReg_value(Integer.parseInt(regValue_SV_F));
               list.add(rV);
               jsonString=JsonUtil.objectToString(list);
               break;
           case 1:
               String strRegValue=folatToHexString(Float.valueOf(regValue_SV_F));
               String H_Hex=strRegValue.substring(0,4);
               String L_Hex=strRegValue.substring(4,8);
               int L_Dec = Integer.parseInt(L_Hex, 16);//16进转十进制
               int H_Dec=Integer.parseInt(H_Hex, 16);//16进转十进制
               RegValue rV1=new RegValue();
               rV1.setReg_addr(regAddr_F);
               rV1.setReg_value(L_Dec);
               list.add(rV1);
               RegValue rV2=new RegValue();
               rV2.setReg_addr(regAddr_F+1);
               rV2.setReg_value(H_Dec);
               list.add(rV2);
               jsonString=JsonUtil.objectToString(list);
               break;
           default:
               throw new IllegalStateException("Unexpected value: " + regType_F);
       }
       return jsonString;
    }
    /*****************************************/

    /*****************************************/
    //浮点数转16进制，结果是字符串
    private   static  String folatToHexString(Float value){
        return  Integer.toHexString(Float.floatToIntBits(value));
    }
    /******************************************/

    /*****************************************/
    //判断是否为整型数
    private boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        //Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        Pattern pattern = Pattern.compile("^-?[0-9]\\d*$");
        return pattern.matcher(str).matches();
    }
   /******************************************/

   /******************************************/
   //判断是否为浮点数
   // 判断浮点数（double和float）
    private boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
        return pattern.matcher(str).matches();
    }

    @Override
    protected void onResume() {
        regAddr=getIntent().getIntExtra("regaddr",0);
        regType=getIntent().getIntExtra("regaddrtype",0);
        regName=getIntent().getStringExtra("regname");
        regValue=getIntent().getStringExtra("regvalue");
        TextView textView=(TextView)findViewById(R.id.editaddrshow);
        textView.setText(regName);
        userValueEdit.setHint(regValue);
        super.onResume();
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
